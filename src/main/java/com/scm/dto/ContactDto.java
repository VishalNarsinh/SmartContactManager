package com.scm.dto;

import com.scm.validators.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactDto {

    private int contactId;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Contact number is required")
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;
    private String address;
    private String picture;
    private String imagePublicId;
    private String description;
    private boolean favourite;
    private String websiteLink;
    private String linkedinLink;

//    @ValidFile
    private MultipartFile contactImage;

    private UserDto user;
}
