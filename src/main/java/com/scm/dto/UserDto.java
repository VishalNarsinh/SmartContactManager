package com.scm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scm.helper.Providers;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private int userId;
    @NotBlank(message = "Name is required")
    @Size(min = 2,message = "Minimum 2 characters are required")
    private String name;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long"
    )
    private String password;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "About is required")
    private String about;

    @NotBlank(message = "Contact number is required")
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;


    private boolean enabled = true;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    private Providers provider = Providers.SELF;
    private String providerUserId;

    private MultipartFile userImage;
    private String imagePublicId;
    private String profilePictureURL;
}
