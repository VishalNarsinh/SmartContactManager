package com.scm.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 5;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file!=null && file.getSize()>MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Maximum file size is 5 MB").addConstraintViolation();
            return false;
        }
//        try {
//            BufferedImage image = ImageIO.read(file.getInputStream());
//            image.getHeight();
//            image.getWidth();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }
}
