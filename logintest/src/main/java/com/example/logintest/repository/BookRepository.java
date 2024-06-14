package com.example.logintest.repository;

import com.example.logintest.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByTitle(String title);
    List<Book> findByCountGreaterThan(int i);
}

