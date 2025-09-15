package com.sccm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication){

        //Provider logic
        if(authentication instanceof OAuth2AuthenticationToken){
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oAuth2User = (OAuth2User)authentication.getPrincipal();
            String username = "";

            //Google
            if(clientId.equalsIgnoreCase("google")){
                username = oAuth2User.getAttribute("email").toString();
            }
            //Github
            else if(clientId.equalsIgnoreCase("github")){
                username = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString() + "@gmail.com";
            }

            return username;
        }else{
            //simple
            return authentication.getName();
        }
    }

    public static String getLinkforEmailVerification(String emailToken){
        String link = "http://SmartCorporateContactManager.ap-south-1.elasticbeanstalk.com/auth/verify-email?token=" + emailToken;
        return link;
    }
}
