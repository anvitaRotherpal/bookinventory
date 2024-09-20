package com.example.bookinventory.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookinventory.model.Book;
import com.example.bookinventory.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public String viewBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/books";
    }

    @PostMapping("/books/filter")
    public String filterBooks(@RequestParam String title, @RequestParam String author, @RequestParam String genre, Model model) {
        model.addAttribute("books", bookService.filterBooks(title, author, genre));
        return "books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/books/export/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {
        // Set the content type and header
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=books.csv");

        // Create a PrintWriter for writing the response
        PrintWriter writer = response.getWriter();

        // Write the header row
        writer.println("ID,Title,Author,Genre,Publication Date,ISBN");

        // Retrieve all books from the database
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            writer.println(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," +
                           book.getGenre() + "," + book.getPublicationDate() + "," + book.getIsbn());
        }

        writer.flush();
    }

    @GetMapping("/books/export/json")
    public void exportJSON(HttpServletResponse response) throws IOException {
        // Set the content type and header
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=books.json");

        // Retrieve all books from the database
        List<Book> books = bookService.getAllBooks();

        // Create an ObjectMapper to convert the list to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter writer = response.getWriter();
        
        // Write the JSON data
        writer.write(objectMapper.writeValueAsString(books));

        writer.flush();
    }
}