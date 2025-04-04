package services;

import entity.BookBorrowed;
import entity.User;
import utils.Response;

public interface BorrowedBookService {
     Response borrowBook(BookBorrowed bookBorrowed);

     Response returnBook(String bookId, User user);

     Response fetchBorrowedBook();
}
