package server;

import entity.Book;
import entity.User;
import utils.Response;

public interface BookService {
    Response addBook(Book book);

    Response deleteBook(String srNo, int noOfCopy);

    Response updateBook(Book book);

    void displayBook(User user);

    Response getBookById(String bookID);
}
