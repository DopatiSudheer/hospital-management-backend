package com.hospital.management.service;

import com.hospital.management.model.User;

public interface UserService {
    
    User registerUser(String name, String email, String password, String role);
    
    User loginUser(String email, String password);
    
    boolean emailExists(String email);
    
    User updateUser(User user);
}

