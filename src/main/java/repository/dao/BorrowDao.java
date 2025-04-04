package repository.dao;

import entity.Book;
import entity.BookBorrowed;
import entity.User;

import java.util.Map;
import java.util.Optional;


public interface BorrowDao {

     BookBorrowed addBorrowedBook(Book book, BookBorrowed bookBorrowed) throws Exception;

     Map<String, BookBorrowed> getBorrowBook() throws Exception;

     BookBorrowed returnBook(String bookId, User userData) throws Exception ;

     Optional<BookBorrowed> findBorrowedBookById(String id);



}

