package repository.daoimpl;

import entity.Book;
import entity.BookBorrowed;
import entity.User;
import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.UserDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BorrowDaoImpl implements BorrowDao {
    private Map<String, BookBorrowed> borrowDataDb = new HashMap<>();
    private final BookDao bookDao;
    private final UserDao userDao;

    public BorrowDaoImpl(UserDao userDao, BookDao bookDao) {
        this.userDao = userDao;
        this.bookDao=bookDao;
    }

    @Override
    public BookBorrowed addBorrowedBook(Book book,BookBorrowed bookBorrowed) throws Exception {
        try{
             borrowDataDb.put(book.getBookId(),bookBorrowed);
             return bookBorrowed;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Map<String, BookBorrowed> getBorrowBook() {
        return borrowDataDb;
    }

    @Override
    public BookBorrowed returnBook(String bookId, User userData) throws Exception {
        try {
          return borrowDataDb.remove(bookId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Optional<BookBorrowed> findBorrowedBookById(String id) {
        return Optional.of(borrowDataDb.get(id));
    }



}



