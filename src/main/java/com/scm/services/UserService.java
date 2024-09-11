package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.dto.PasswordDto;
import com.scm.entities.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User saveUser(User user);

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    User updateUser(User user, MultipartFile file);

    void deleteUser(Integer userId);

    boolean doesUserExistByEmail(String email);

    List<User> getAllUsers();

    void verifyEmailFromToken(String emailToken);

    public boolean changePassword(PasswordDto passwordDto, User user);
}
