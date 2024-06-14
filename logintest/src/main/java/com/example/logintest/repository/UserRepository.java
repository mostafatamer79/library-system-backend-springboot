package com.example.logintest.repository;

import com.example.logintest.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    List<UserModel> findByIsAcceptable(boolean acceptable);
    UserModel findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    void deleteById(Long id);
    @Query("SELECT u FROM UserModel u WHERE u.isAdmin <>true ")
    List<UserModel> findAllNonAdminUsers();
    UserModel findByEmail(String email);
    @Query("SELECT COUNT(u) FROM UserModel u WHERE u.isAcceptable = true")
    long countByAcceptableIsTrue();

}
