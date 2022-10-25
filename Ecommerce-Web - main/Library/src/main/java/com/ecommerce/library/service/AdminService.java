package com.ecommerce.library.service;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    //TÌm admin theo tên
    Admin findByUsername(String username);

    //Lưu admin
    Admin save(AdminDto adminDto);

    Admin update(MultipartFile imageAdmin, Admin admin);
}
