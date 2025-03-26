package com.example.perpusapi.resource;

import com.example.perpusapi.model.Book;
import com.example.perpusapi.service.BookService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/books")
public class BookResource {
    private BookService bookService;

    public BookResource() {
        this.bookService = new BookService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return Response.ok(books).build();
    }

    @GET
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("bookId") int bookId) {
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            return Response.ok(book).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        boolean success = bookService.addBook(book);
        if (success) {
            return Response.status(Response.Status.CREATED).entity(book).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("bookId") int bookId, Book book) {
        book.setBuku_id(bookId);
        boolean success = bookService.updateBook(book);
        if (success) {
            return Response.ok(book).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{bookId}")
    public Response deleteBook(@PathParam("bookId") int bookId) {
        boolean success = bookService.deleteBook(bookId);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/author/{authorName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksByAuthor(@PathParam("authorName") String authorName) {
        List<Book> books = bookService.getBooksByAuthor(authorName);
        return Response.ok(books).build();
    }

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return Response.ok(books).build();
    }
}