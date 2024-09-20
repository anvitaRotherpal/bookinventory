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

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public List<Book> filterBooks(String title, String author, String genre) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
              .filter(b -> (author == null || b.getAuthor().contains(author)) &&
                           (genre == null || b.getGenre().contains(genre)))
              .collect(Collectors.toList());
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}