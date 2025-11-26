package com.example.bookinventory.services;

import com.example.bookinventory.model.Book;
import com.example.bookinventory.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Add a new book
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    // Filter books by title, author, genre safely
    public List<Book> filterBooks(String title, String author, String genre) {
        return bookRepository.findByTitleContainingIgnoreCase(title != null ? title : "")
                .stream()
                .filter(b -> (author == null || (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(author.toLowerCase()))) &&
                             (genre == null || (b.getGenre() != null && b.getGenre().toLowerCase().contains(genre.toLowerCase()))))
                .collect(Collectors.toList());
    }

    // Delete book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Filter by category safely
    public List<Book> filterByCategory(String category) {
        if (category == null) return getAllBooks();
        return bookRepository.findByCategory(category);
    }

    // Get books by title safely
    public List<Book> getBooksByTitle(String title) {
        if (title == null) return getAllBooks();
        return bookRepository.findAll().stream()
                .filter(book -> book.getTitle() != null && book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Sort books by title
    public List<Book> getBooksSortedByTitle() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    // Sort books by author
    public List<Book> getBooksSortedByAuthor() {
        return bookRepository.findAllByOrderByAuthorAsc();
    }
}