package repository.dao;

import entity.BookBorrowed;
import entity.User;

import java.util.Map;


public interface BorrowDao {

     BookBorrowed borrowBook(BookBorrowed bookBorrowed) throws Exception;

     Map<String, BookBorrowed> getBorrowBook() throws Exception;

     BookBorrowed returnBook(String bookId, User userData) throws Exception ;

}

