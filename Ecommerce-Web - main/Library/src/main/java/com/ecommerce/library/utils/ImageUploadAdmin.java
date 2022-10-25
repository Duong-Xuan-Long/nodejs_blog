package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUploadAdmin {

    //Path to image
    private final String UPLOAD_FOLDER = "E:\\Ecommerce-Web - main\\Admin\\src\\main\\resources\\static\\img\\image-admin";

    //update ảnh
    public boolean uploadImage(MultipartFile imageAdmin) {
        boolean isUpload = false;
        try {
            Files.copy(imageAdmin.getInputStream(),
                    Paths.get(UPLOAD_FOLDER + File.separator, imageAdmin.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    //Kiểm tra nếu ảnh có tồn tại hay không
    public boolean checkExisted(MultipartFile imageAdmin) {
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + imageAdmin.getOriginalFilename());
            isExisted = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExisted;
    }

}
