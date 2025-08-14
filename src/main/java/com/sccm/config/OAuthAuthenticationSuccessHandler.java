package com.sccm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sccm.entities.Providers;
import com.sccm.entities.User;
import com.sccm.helpers.AppConstants;
import com.sccm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                logger.info("OAuthAuthenticationSuccessHandler");

                //Save Data before redirect

                //Identify the user first each provider gives different type of credential

                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

                String authorizedClientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

                logger.info(authorizedClientId);

                DefaultOAuth2User oAuthUser = (DefaultOAuth2User)authentication.getPrincipal();

                oAuthUser.getAttributes().forEach((key, value) ->{
                    logger.info("{} => {}", key, value);
                });

                User dbUser = new User();
                dbUser.setUserId(UUID.randomUUID().toString());
                dbUser.setEnabled(true);
                dbUser.setEmailVerified(true);
                dbUser.setRoleList(List.of(AppConstants.ROLE_USER));
                dbUser.setAbout("User created using OAuth");
                dbUser.setProviderUserId(oAuthUser.getName());
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                dbUser.setPassword(encoder.encode("password"));

                String email = "";
                String picture = "";
                String name = "";
                Providers provider = Providers.SELF;
                //Google attributes
                if(authorizedClientId.equalsIgnoreCase("google")){
                    email = oAuthUser.getAttribute("email").toString();
                    name = oAuthUser.getAttribute("name").toString();
                    picture = oAuthUser.getAttribute("picture").toString();
                    provider = Providers.GOOGLE;
                }
                //Github attributes
                else if(authorizedClientId.equalsIgnoreCase("github")){
                    email = oAuthUser.getAttribute("email") != null ? oAuthUser.getAttribute("email") : oAuthUser.getAttribute("login").toString() + "@gmail.com";

                    picture = oAuthUser.getAttribute("avatar_url").toString();

                    name = oAuthUser.getAttribute("login").toString();

                    provider = Providers.GITHUB;

                }
                else{
                    logger.info(authorizedClientId + " is unknown provider");
                }
                dbUser.setEmail(email);
                dbUser.setName(name);
                dbUser.setProfilePic(picture);
                dbUser.setProvider(provider);

                User existingUser = userRepo.findByEmail(email).orElse(null);
                if(existingUser == null){
                    userRepo.save(dbUser);
                    logger.info("User saved: " + email);
                }

                //Using response
                // response.sendRedirect("/user/dashboard");
                //Using RedirectStrategy
                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
