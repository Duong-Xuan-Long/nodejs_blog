package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUploadCustomer {
    //Đường dẫn folder
    private final String UPLOAD_FOLDER = "E:\\Ecommerce-Web - main\\Customer\\src\\main\\resources\\static\\img\\imageCustomer";

    //update ảnh
    public boolean uploadImage(MultipartFile imageCustomer) {
        boolean isUpload = false;
        try {
            Files.copy(imageCustomer.getInputStream(),
                    Paths.get(UPLOAD_FOLDER + File.separator, imageCustomer.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    //Kiểm tra nếu ảnh có tồn tại hay không
    public boolean checkExisted(MultipartFile imageCustomer) {
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + imageCustomer.getOriginalFilename());
            isExisted = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExisted;
    }
}
