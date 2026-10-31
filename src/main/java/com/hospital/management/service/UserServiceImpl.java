
package com.hospital.management.service;

import com.hospital.management.model.User;
import com.hospital.management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public User registerUser(
            String name,
            String email,
            String password,
            String role) {

        User user = new User(name, email, password, role);
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return null;
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            return null;
        }

        String storedPassword = user.getPassword();

        if (storedPassword == null) {
            return null;
        }

        // Support BCrypt passwords, including the fixed admin password.
        if (storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {

            if (!passwordEncoder.matches(password, storedPassword)) {
                return null;
            }

        } else {
            // Temporary compatibility for existing plain-text passwords.
            if (!storedPassword.equals(password)) {
                return null;
            }
        }

        return user;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
