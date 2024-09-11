package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication) {
        String email="";
        if(authentication instanceof OAuth2AuthenticationToken){
        //google || facebook || github
            var oAuth2Token =(OAuth2AuthenticationToken) authentication;
            String client = oAuth2Token.getAuthorizedClientRegistrationId();
            var oAuth2User = (OAuth2User) authentication.getPrincipal();
            if(client.equalsIgnoreCase("google")) {
                email = oAuth2User.getAttribute("email").toString();
            }
            else if(client.equalsIgnoreCase("facebook")) {}
        }else{
        //self
            email = authentication.getName();
        }
       return email;
    }

    public static String getLinkForEmailVerification(String emailToken) {
        return AppConstants.BASE_URL+"/api/auth/verify-email?token=" + emailToken;
    }

    public static String getHtmlBodyForEmailVerification(String emailToken) {
        return String.format(
                "<!DOCTYPE html><html><head><title>Email Verification</title><style>body {font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;}.container {width: 100%%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}.header {text-align: center; padding: 20px 0;}.header img {width: 100px;}.content {margin-top: 20px;}.content h1 {color: #333333;}.content p {color: #666666; line-height: 1.6;}.content a {display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007BFF; color: #ffffff; text-decoration: none; border-radius: 5px;}.content a:hover {background-color: #0056b3;}.footer {text-align: center; margin-top: 20px; padding: 20px 0; border-top: 1px solid #eeeeee; color: #999999;}</style></head><body><div class=\"container\"><div class=\"header\"><img src=\"https://res.cloudinary.com/dzew8rxxw/image/upload/v1722927658/scm/verifyemail_svnt20.png\" alt=\"Smart Contact Manager\"></div><div class=\"content\"><h1>Email Verification</h1><p>Dear User,</p><p>Thank you for registering with Smart Contact Manager. To complete your registration, please verify your email address by clicking the link below:</p><a href=\"%s\">Verify Email</a><p>If you did not create an account, please ignore this email.</p><p>Thank you,<br>Smart Contact Manager Team</p></div><div class=\"footer\">&copy; 2024 Smart Contact Manager. All rights reserved.</div></div></body></html>",
                getLinkForEmailVerification(emailToken)
        );
    }
}
