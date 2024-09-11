package com.scm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helper.AppConstants;
import com.scm.services.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file, String folderName,String filename) {
        try {
            cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folderName,
                    "public_id", filename
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUrlFromPublicId(filename);
    }


    public String uploadImage1(MultipartFile file, String folderName,String filename) {
        try {
            cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folderName,
                    "public_id", filename
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUrlFromPublicIdWithoutFolder(folderName+"/"+filename);
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url().generate(AppConstants.CONTACT_IMAGE_FOLDER+ "/"+ publicId);
    }

    public String getUrlFromPublicIdWithoutFolder(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    public void deleteImage(String publicId){

        if((publicId!=null) &&  (publicId.equals(AppConstants.DEFAULT_CONTACT_IMAGE_ID) || publicId.equals(AppConstants.DEFAULT_USER_IMAGE_ID))){
            return;
        }

        try {
            ApiResponse apiResponse = cloudinary.api().deleteResources(List.of(publicId),
                    ObjectUtils.asMap("type", "upload", "resource_type", "image"));
            System.out.println(apiResponse);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
