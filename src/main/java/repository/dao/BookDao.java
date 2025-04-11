package repository.dao;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> addBook(Book book) throws Exception;

    boolean deleteBook(Book book) throws Exception;

    Optional<Book> updateBook(Book book , String columnName) throws Exception;

    Optional<List<Book>> getBooks();

    Optional<Book> getBooksById(String bookId) throws Exception;

    Optional<Book> isBookIsPresent(Book book) throws Exception;

}
