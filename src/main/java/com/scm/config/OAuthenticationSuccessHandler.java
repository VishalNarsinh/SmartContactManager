package com.scm.config;

import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.helper.Providers;
import com.scm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private UserRepository userRepository;

   Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("onAuthenticationSuccess");
        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        var oAuth2User=  (DefaultOAuth2User) authentication.getPrincipal();
//        oAuth2User.getAttributes().forEach((key, value) -> logger.info("{} : {}", key, value));

            User user1 = new User();

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
            String email = oAuth2User.getAttribute("email").toString();
            String name = oAuth2User.getAttribute("name").toString();
            String picture = oAuth2User.getAttribute("picture").toString();
            Boolean isEmailVerified = (Boolean)oAuth2User.getAttribute("email_verified");
            user1.setEmail(email);
            user1.setName(name);
            user1.setProfilePictureURL(picture);
            user1.setPassword(null);
            user1.setEmailVerified(isEmailVerified);
            user1.getRoleList().add(AppConstants.ROLE_USER);
            user1.setProvider(Providers.GOOGLE);
            user1.setProviderUserId(oAuth2User.getName());
            user1.setEnabled(true);
            user1.setAbout("...");


        }else if(authorizedClientRegistrationId.equalsIgnoreCase("facebook")){
//            find key for other clients
        }else {
            logger.info("Unauthorized Client");
        }
            User checkingUser = userRepository.findByEmail(user1.getEmail()).orElse(null);
            if(checkingUser==null){
                userRepository.save(user1);
                logger.info("User saved");
            }

/*
//        logger.info(user.getName());
//        user.getAttributes().forEach((key, value) -> logger.info("{} : {}", key, value));
//        logger.info(user.getAuthorities().toString());



*/
        response.sendRedirect("/user/profile");


    }
}
