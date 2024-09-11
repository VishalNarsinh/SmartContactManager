package com.scm.security;

import com.scm.config.OAuthenticationSuccessHandler;
import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;
import com.scm.services.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.CustomUserDetailsService;


@Configuration
public class SecurityConfig {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    String[] PROTECTED_URLS = new String[] {
            "/user/**"
    };

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    OAuthenticationSuccessHandler oAuthenticationSuccessHandler;


    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, EmailServiceImpl emailServiceImpl) throws Exception {
        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PROTECTED_URLS).authenticated();
                    auth.anyRequest().permitAll();
                });

        http.formLogin(formLogin -> {
            formLogin.loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .defaultSuccessUrl("/user/profile")
                    // .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .failureHandler((request, response, exception) -> {
                        if(exception instanceof BadCredentialsException) {
                            response.sendRedirect("/login?error=true");
                        }
                        else if(exception instanceof DisabledException) {
                            String email = request.getParameter("email");
                            User user = userService.getUserByEmail(email);
                            emailServiceImpl.sendHtmlEmail(
                                    user.getEmail(),
                                    "Email Verification",
                                    Helper.getHtmlBodyForEmailVerification(user.getEmailToken()));
                            response.sendRedirect("/login?disabled=true");
                        }else{
                            response.sendRedirect("/login?error=true");
                        }
                    })
            ;
        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/login?logout=true");
        });

        http.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(oAuthenticationSuccessHandler);
        });
        return http.build();
    }



}
