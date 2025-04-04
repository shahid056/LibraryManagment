package repository.dao;

import entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> addBook(Book book) throws Exception;

    boolean deleteBook(Book book) throws Exception;

    List<Book> getBooks();

    Optional<Book> findBookById(String id);

    Optional<Book> isBookIsPresent(Book book) throws Exception;

}
