package com.example.logintest.controller;

import com.example.logintest.model.Book;
import com.example.logintest.repository.BookRepository;
import com.example.logintest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository BookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/valid")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestParam String author,
                                     @RequestParam String title,
                                     @RequestParam String genre,
                                     @RequestParam double rate,
                                     @RequestParam int count,
                                     @RequestParam MultipartFile poster) throws IOException {
        // Check if the title is already in use
        if (bookService.isTitleExists(title)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Book with the title '" + title + "' already exists");
        }

        // If title is not in use, proceed to add the book
        Book book = bookService.addBook(author, title, genre, rate, count,poster);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id,
                                        @RequestParam String author,
                                        @RequestParam String title,
                                        @RequestParam String genre,
                                        @RequestParam double rate,
                                        @RequestParam int count,
                                        @RequestParam(required = false) MultipartFile poster) throws IOException {
        if (bookService.isTitleExists(title))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book with the title '" + title + "' already exists");


        Book book = bookService.updateBook(id, author, title, genre ,rate,count, poster);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Long> countBook(){
        long bookcount = BookRepository.count();
        return ResponseEntity.ok(bookcount);
    }
}

