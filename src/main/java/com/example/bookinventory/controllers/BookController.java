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

    // View all books
    @GetMapping("/books")
    public String viewBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    // Search by title (optional)
    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String title, Model model) {
        List<Book> books = (title == null || title.isEmpty()) ?
                           bookService.getAllBooks() :
                           bookService.getBooksByTitle(title);
        model.addAttribute("books", books);
        return "books";
    }

    // Filter by category (optional)
    @GetMapping("/books/filter")
    public String filterBooksByCategory(@RequestParam(required = false) String category, Model model) {
        List<Book> books = (category == null || category.isEmpty()) ?
                           bookService.getAllBooks() :
                           bookService.filterByCategory(category);
        model.addAttribute("books", books);
        return "books";
    }

    // Show form to add a new book
    @GetMapping("/books/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book"; // points to add-book.html
    }

    // Add a new book (POST)
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/books";
    }

    // Advanced filter (POST)
    @PostMapping("/books/filter")
    public String filterBooks(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String author,
                              @RequestParam(required = false) String genre,
                              Model model) {
        model.addAttribute("books", bookService.filterBooks(title, author, genre));
        return "books";
    }

    // Delete a book
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // Export all books as CSV
    @GetMapping("/books/export/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=books.csv");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Title,Author,Genre,Publication Date,ISBN");

        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            writer.println(
                (book.getId() != null ? book.getId() : "") + "," +
                (book.getTitle() != null ? book.getTitle() : "") + "," +
                (book.getAuthor() != null ? book.getAuthor() : "") + "," +
                (book.getGenre() != null ? book.getGenre() : "") + "," +
                (book.getPublicationDate() != null ? book.getPublicationDate() : "") + "," +
                (book.getIsbn() != null ? book.getIsbn() : "")
            );
        }
        writer.flush();
    }

    // Export all books as JSON
    @GetMapping("/books/export/json")
    public void exportJSON(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=books.json");

        List<Book> books = bookService.getAllBooks();
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(books));
        writer.flush();
    }

    // Sort by title
    @GetMapping("/books/sort/title")
    public String sortByTitle(Model model) {
        model.addAttribute("books", bookService.getBooksSortedByTitle());
        return "books";
    }

    // Sort by author
    @GetMapping("/books/sort/author")
    public String sortByAuthor(Model model) {
        model.addAttribute("books", bookService.getBooksSortedByAuthor());
        return "books";
    }

    // Redirect root URL to /books
    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/books";
    }
}