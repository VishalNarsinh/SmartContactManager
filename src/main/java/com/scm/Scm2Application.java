package com.scm;

import com.scm.helper.AppConstants;
import com.scm.services.impl.ImageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Scm2Application implements CommandLineRunner {




	private static final Logger log = LoggerFactory.getLogger(Scm2Application.class);
    @Autowired
    private ImageServiceImpl imageServiceImpl;

	public static void main(String[] args) {
		SpringApplication.run(Scm2Application.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

	}
}
