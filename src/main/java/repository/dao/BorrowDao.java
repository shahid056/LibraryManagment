package repository.dao;

import model.Book;
import model.BookBorrowed;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface BorrowDao {

    boolean addBorrowedBook(Book book, BookBorrowed bookBorrowed) throws Exception;

    List<BookBorrowed> getBorrowBook() throws Exception;

    Optional<BookBorrowed> returnBook(BookBorrowed bookBorrowed) throws Exception;

    Optional<BookBorrowed> fetchBorrowedUserIdBookId(String userId, String bookId) throws SQLException;

    List<BookBorrowed> fetchBorrowedUserId(String userId) throws SQLException;

}



