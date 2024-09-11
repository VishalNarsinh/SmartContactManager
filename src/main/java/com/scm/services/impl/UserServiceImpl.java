package com.scm.services.impl;

import com.scm.dto.PasswordDto;
import com.scm.entities.User;
import com.scm.exceptions.ResourceNotFoundException;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.repositories.UserRepository;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class  UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailServiceImpl emailServiceImpl;
    @Autowired
    private ImageServiceImpl imageServiceImpl;


    @Override
    public User saveUser(User user) {
        user.getRoleList().add(AppConstants.ROLE_USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        user.setProfilePictureURL(imageServiceImpl.getUrlFromPublicIdWithoutFolder(AppConstants.DEFAULT_USER_IMAGE_ID));
        user.setImagePublicId(AppConstants.DEFAULT_USER_IMAGE_ID);
        user.setEmailToken(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);
        emailServiceImpl.sendHtmlEmail(
                user.getEmail(),
                "Email Verification",
                Helper.getHtmlBodyForEmailVerification(user.getEmailToken()));
        return savedUser;
    }


    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with id " + userId + " not found"));

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User updateUser(User user, MultipartFile file) {
        User oldUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + user.getUserId()));
        log.info("inside update service");
        if(file != null && !file.isEmpty()) {
            imageServiceImpl.deleteImage(oldUser.getImagePublicId());
            String filename = UUID.randomUUID().toString();
            String imageUrl = imageServiceImpl.uploadImage1(file, AppConstants.USER_IMAGE_FOLDER, filename);
            oldUser.setProfilePictureURL(imageUrl);
            oldUser.setImagePublicId(AppConstants.USER_IMAGE_FOLDER+"/"+filename);
            log.info("{}",file.getOriginalFilename());
        }
        oldUser.setName(user.getName());
        oldUser.setAbout(user.getAbout());
        oldUser.setContacts(user.getContacts());
        return userRepository.save(oldUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + userId));
        userRepository.delete(user);
    }

    @Override
    public boolean doesUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public void verifyEmailFromToken(String emailToken) {
        User user = userRepository.findByEmailToken(emailToken).orElseThrow(() -> new ResourceNotFoundException("User not found with email token : " + emailToken));
        user.setEmailVerified(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean changePassword(PasswordDto passwordDto, User user) {
        if(bCryptPasswordEncoder.matches(passwordDto.getOldPassword(),user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(passwordDto.getNewPassword()));
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }
}
