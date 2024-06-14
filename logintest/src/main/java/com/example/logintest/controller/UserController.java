package com.example.logintest.controller;

import com.example.logintest.model.UserModel;
import com.example.logintest.repository.UserRepository;
import com.example.logintest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/activate")
    public ResponseEntity<List<UserModel>> getUsersWithActivation() {
        List<UserModel> users = userService.getUsersWithActivation(true);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/login/{email}/{password}")
    public ResponseEntity<UserModel> login(@PathVariable String email, @PathVariable String password) {
        UserModel user = userService.login(email, password);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Optional<UserModel> userOptional = userRepository.findById(id); // Use UserRepository to find user

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel user) {
        try {
            UserModel createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle email already in use error
        }
    }


    @PutMapping("/activate/{id}")
    public ResponseEntity<Void> updateUserActivation(@PathVariable Integer id ) {
        userService.updateUserActivation(id, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        long userCount = userRepository.count();
        return ResponseEntity.ok(userCount-1);
    }


    @GetMapping("/count/activated")
    public ResponseEntity<Long> countActivatedUsers() {
        long activatedUserCount = userRepository.countByAcceptableIsTrue();
        return ResponseEntity.ok(activatedUserCount-1);
    }
}
