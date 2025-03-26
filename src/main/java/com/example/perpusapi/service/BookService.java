package com.example.perpusapi.service;

import com.example.perpusapi.model.Book;
import com.example.perpusapi.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private BookRepository bookRepository;

    public BookService() {
        this.bookRepository = new BookRepository();
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public Book getBookById(int bookId) {
        return bookRepository.getBookById(bookId);
    }

    public boolean addBook(Book book) {
        return bookRepository.addBook(book);
    }

    public boolean updateBook(Book book) {
        return bookRepository.updateBook(book);
    }

    public boolean deleteBook(int bookId) {
        return bookRepository.deleteBook(bookId);
    }

    public List<Book> getBooksByAuthor(String author) {
        return getAllBooks().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> getAvailableBooks() {
        return getAllBooks().stream()
                .filter(book -> book.getJml_tersedia() > 0)
                .collect(Collectors.toList());
    }
}