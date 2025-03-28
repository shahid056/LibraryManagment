package repository.daoimpl;

import entity.Book;
import entity.BookBorrowed;
import entity.User;
import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.UserDao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BorrowDoaImpl implements BorrowDao {
    private Map<String, BookBorrowed> borrowDataDb = new HashMap<>();
    private final BookDao bookDao = BookDaoImpl.getBookInstance();
    private final UserDao user=UserDaoImpl.getInstance();
    private static BorrowDoaImpl borrowDoa;
    private UserDao userDao=UserDaoImpl.getInstance();

    private BorrowDoaImpl() {
    }

    public static BorrowDoaImpl getInstance(){
        if(Objects.isNull(borrowDoa)){
            borrowDoa=new BorrowDoaImpl();
        }
        return borrowDoa;
    }


    @Override
    public BookBorrowed borrowBook(BookBorrowed bookBorrowed) throws Exception {
        Optional<Book> book = bookDao.findBookById(bookBorrowed.getBookId());
        if (Objects.isNull(book)) {
            return null;
        }
        try {
            Optional<User> userById = user.findUserById(bookBorrowed.getUserId());
            ;
            if (userById.isPresent()) {
                bookBorrowed.setUser(userById.get());

                int id = borrowDataDb.size() + 1;
                bookBorrowed.setBorrowId("br" + id);
                System.out.println(bookBorrowed.getBookId());
                Optional<Book> bookByName = bookDao.findBookById(bookBorrowed.getBookId());
                System.out.println(bookByName);
                bookBorrowed.setBook(bookByName.get());
                bookByName.ifPresent(bookFormDb -> borrowDataDb.put(bookFormDb.getBookId(), bookBorrowed));
                if (bookDao.findBookById(bookBorrowed.getBookId()).stream().anyMatch(bookCopyCheck -> bookCopyCheck.getNumberOfCopy() >= 1)) {
                    book.ifPresent(bookCopy -> bookCopy.setNumberOfCopy(bookCopy.getNumberOfCopy() - 1));
                        book.ifPresent(serialNuber-> {
                            try {
                                bookBorrowed.setBookSerialNumber(serialNuber.getSerialNumber());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    book.ifPresent(value -> userById.ifPresent(user -> {
                        try {
                            user.setBorrowedBooksById(value);
                            userDao.add(user);
                        } catch (Exception e) {
                            throw new RuntimeException("Error adding user to the database", e);
                        }
                    }));
                    return bookBorrowed;
                }
            }
            return null;
        }catch(Exception e){
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
            if (borrowDataDb.containsKey(bookId.toLowerCase())) {
                BookBorrowed borrowedBook = borrowDataDb.get(bookId);
                if (borrowedBook.getUserId().equalsIgnoreCase(userData.getId())) {
                    List<Book> borrowedBooksList = userData.getBorrowedBooks();
                    userData.removeBorrowedBook(bookId);
                    fineCalculate(borrowedBook.getFine(), borrowedBook);
                    return borrowDoa.borrowDataDb.remove(bookId);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    }


    //calculate the fine for late due
    private void fineCalculate(double fine, BookBorrowed bookBorrowed) {
        long fineAmount = 0;
        if (!bookBorrowed.getReturnDate().isBefore(LocalDate.now())) {
            return;
        }
        fineAmount = ChronoUnit.DAYS.between(bookBorrowed.getReturnDate(), LocalDate.now());
        fine += fineAmount * 50;
        bookBorrowed.setFine(fine);
    }
}



