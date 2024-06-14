package com.example.logintest.service;
import org.mindrot.jbcrypt.BCrypt;

import com.example.logintest.model.UserModel;
import com.example.logintest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<UserModel> getAllUsers() {
        return userRepository.findAllNonAdminUsers();
    }

    public List<UserModel> getUsersWithActivation(boolean acceptable) {
        return userRepository.findByIsAcceptable(acceptable);
    }

    public UserModel login(String email, String password) {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    public UserModel createUser(UserModel user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Hash the user's password before saving
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
    public void updateUserActivation(Integer id, boolean acceptable) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setAcceptable(acceptable);
            userRepository.save(user);
        }
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
