package services;

import entity.Book;
import utils.Response;

import java.util.List;

public interface BookService {
    Response addBook(Book book);

    Response deleteBook(String srNo, int noOfCopy,boolean deleteAllCopy);

    Response updateBook(Book book);

    List<Book> fetchBooks();

    Response getBookById(String bookID);

}
