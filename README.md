# SCM2 - Spring Boot Web Application

## Overview

SCM2 is a Spring Boot web application designed to manage [describe functionality, e.g., user contacts, etc.]. It is configured to run on port 9090 and connects to a MySQL database.

## Prerequisites

- Java 21
- MySQL Server
- SMTP Email Server (e.g., Gmail)
- Cloudinary Account (for media storage)
- OAuth2 Providers (Google, GitHub)

## Configuration

The application configuration is defined in the `src/main/resources/application.properties` file. Below are the key configurations:

```properties
spring.application.name=SCM2
server.port=9090

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/scm2
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# File Upload Configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=-1
spring.servlet.multipart.max-request-size=-1

# OAuth2 Client Configuration

## Google
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile

## GitHub
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=email,profile

# Cloudinary Configuration
cloudinary.cloudname=YOUR_CLOUDINARY_CLOUDNAME
cloudinary.api_key=YOUR_CLOUDINARY_API_KEY
cloudinary.api_secret=YOUR_CLOUDINARY_API_SECRET

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL_USERNAME
spring.mail.password=YOUR_EMAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Logging Configuration
logging.level.com.zaxxer.hikari=OFF
logging.level.org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean=ERROR
