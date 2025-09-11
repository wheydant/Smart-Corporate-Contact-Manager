package com.sccm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sccm.helpers.MessageType;
import com.sccm.helpers.Messages;
import com.sccm.services.implementation.SecurityCustomUserDetailService;

import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

    // user creation and login user with in memory service
    // @Bean
    // public UserDetailsService userDetailsService(){

    //     //User from .security
    //     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    //     UserDetails user1 = User
    //         .withUsername("admin")
    //         //Encoder is mandatory
    //         .password(encoder.encode("admin"))
    //         .roles("ADMIN", "USER")
    //         .build();

    //     var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);

    //     return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    //Configuration of authenticator provider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        //DaoAuthenticationProvider has all the methods to register the service
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        //User detail service object :
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        
        //Password encoder object but we are using direct
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        //url configuration - by using this it will not push login page are we are messing with default functionality
        httpSecurity.authorizeHttpRequests((authorize) -> {
            // authorize.requestMatchers("/home", "/register").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //To use form Login for with default configuration for secured paths
        //Customizer from security
        // httpSecurity.formLogin(Customizer.withDefaults());

        //We don't want to include default login page
        httpSecurity.formLogin(formLogin ->{
            formLogin.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            // Changed from successForwardUrl defaultSuccessUrl perform client side rendering which is better for login 
            // .successForwardUrl("/user/dashboard")
            //In successForwardUrl we use server side which makes the form to resubmit it again which causes error
            .defaultSuccessUrl("/user/profile", true)  
            .failureUrl("/login?error=true")
            .usernameParameter("name")
            .passwordParameter("password")
            // .failureHandler(new AuthenticationFailureHandler() {
            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
            // })
            // .successHandler(new AuthenticationSuccessHandler() {

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
            // })
            ;


            formLogin.failureHandler((request, response, exception) ->{
                HttpSession session = request.getSession();
                if(exception instanceof DisabledException){
                    //user is disabled
                    session.setAttribute("message", Messages.builder().content("User is disables, Email with verification link is sent to your email id").type(MessageType.red).build());

                    response.sendRedirect("/login");
                }else{
                    session.setAttribute("message", Messages.builder().content("Invalid Credentials").type(MessageType.red).build());
                    response.sendRedirect("/login");
                }
            });
        });

        //Disable CSRF token so that we can logout using default functionality
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
        //Configure Logout page
        httpSecurity.logout(logoutForm -> {
            //By default /logout uses get request.
            logoutForm.logoutUrl("/logout")
            .logoutSuccessUrl("/home");
        });

        //OAuth config
        //Default login page
        // httpSecurity.oauth2Login(Customizer.withDefaults());

        //Customized page
        httpSecurity.oauth2Login(oAuth -> {
            oAuth.loginPage("/login")
            .successHandler(handler)
            ;
        });


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
