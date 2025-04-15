package service;

import model.Book;
import utils.Response;

import java.util.List;

public interface BookService {
    Response addBook(Book book);

    Response deleteBook(Book book);

    Response updateBook(Book book,String columnName);

    Response fetchBooks();

    Response getBookById(String bookID);

}
