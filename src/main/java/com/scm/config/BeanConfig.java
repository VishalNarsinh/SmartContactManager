package com.scm.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Objects;

@Configuration
public class BeanConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Value("${cloudinary.cloudname}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    Cloudinary cloudinary() {
        Map map = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        );
        Cloudinary cloudinary = new Cloudinary(map);
        return cloudinary;
    }
}
