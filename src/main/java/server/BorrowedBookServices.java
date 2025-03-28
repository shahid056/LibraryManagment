package server;

import entity.BookBorrowed;
import entity.User;
import utils.Response;

public interface BorrowedBookServices {
     Response borrowBook(BookBorrowed bookBorrowed);

     Response returnBook(String bookId, User user);

     void transactionBook();
}
