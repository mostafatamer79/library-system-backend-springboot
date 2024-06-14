package com.example.logintest.service;

import com.example.logintest.model.Book;
import com.example.logintest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private List<String> allowedExtensions = List.of(".jpg", ".png", ".jpeg");
    private long maxAllowedPosterSize = 1048576;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByCountGreaterThan(0);
    }

    public Book getBookById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }
    public boolean isTitleExists(String title) {
        return bookRepository.existsByTitle(title);
    }
    public Book addBook(String author, String title, String genre, double rate,int count, MultipartFile poster) throws IOException {
        if (!allowedExtensions.contains(getFileExtension(poster.getOriginalFilename()).toLowerCase())) {
            throw new IllegalArgumentException("Only .png and .jpg are allowed.");
        }
        if (poster.getSize() > maxAllowedPosterSize) {
            throw new IllegalArgumentException("Max allowed size for poster is 1MB.");
        }

        String fileName = UUID.randomUUID() + getFileExtension(poster.getOriginalFilename());
        String path = "D:/se/library (12)/src/images/" + fileName;
        Files.copy(poster.getInputStream(), Paths.get(path));

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setGenre(genre);
        book.setRate(rate);
        book.setPoster(fileName);
        book.setStatus(true);
        book.setCount(count);

        return bookRepository.save(book);
    }

    public Book updateBook(Integer id, String author, String title, String genre, double rate,int count, MultipartFile poster) throws IOException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));

        if (poster != null) {
            String posterPath = "D:/se/library (12)/src/images/" + book.getPoster();
            Files.deleteIfExists(Paths.get(posterPath));

            String newFileName = UUID.randomUUID() + getFileExtension(poster.getOriginalFilename());
            String newPath = "D:/se/library (12)/src/images/" + newFileName;
            Files.copy(poster.getInputStream(), Paths.get(newPath));

            // Update the book details with the new poster
            book.setPoster(newFileName);
        }

        // Update other book details
        book.setAuthor(author);
        book.setTitle(title);
        book.setGenre(genre);
        book.setRate(rate);
        book.setCount(count);

        return bookRepository.save(book);
    }

    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));

        String posterPath = "D:/se/library (12)/src/images/" + book.getPoster();

        bookRepository.deleteById(id);

        try {
            Files.deleteIfExists(Paths.get(posterPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}
