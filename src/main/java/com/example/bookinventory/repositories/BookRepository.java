package com.example.bookinventory.repositories;

import com.example.bookinventory.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search methods
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenreContainingIgnoreCase(String genre);

    // Category filter
    List<Book> findByCategory(String category);

    // Sorting
    List<Book> findAllByOrderByTitleAsc();
    List<Book> findAllByOrderByAuthorAsc();
}