package service;

import model.Book;
import model.BookBorrowed;
import model.User;
import utils.Response;

public interface BorrowedBookService {
     Response borrowBook(BookBorrowed bookBorrowed);

     Response returnBook(Book book, User user);

     Response fetchBorrowedBook();
}
