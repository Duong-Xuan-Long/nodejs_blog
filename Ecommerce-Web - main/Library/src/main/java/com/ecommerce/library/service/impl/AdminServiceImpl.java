package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.AdminService;
import com.ecommerce.library.utils.ImageUploadAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Base64;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ImageUploadAdmin imageUploadAdmin;

    //lưu admin
    @Override
    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        return adminRepository.save(admin);
    }

    //update admin
    @Override
    public Admin update(MultipartFile imageAdmin, Admin admin) {
        try {
            Admin admin1 = adminRepository.findById(admin.getId()).get();
            if (imageAdmin == null) {
                admin1.setImage(null);
            } else {
                if (!imageAdmin.getOriginalFilename().equals("")) {
                    if (imageUploadAdmin.checkExisted(imageAdmin) == false) {
                        imageUploadAdmin.uploadImage(imageAdmin);
                    }
                    admin1.setImage(Base64.getEncoder().encodeToString(imageAdmin.getBytes()));
                } else {
                    admin1.setImage(admin1.getImage());
                }
            }

            admin1.setFirstName(admin.getFirstName());
            admin1.setLastName(admin.getLastName());
            admin1.setUsername(admin.getUsername());
            admin1.setPassword(passwordEncoder.encode(admin.getPassword()));
            return adminRepository.save(admin1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
