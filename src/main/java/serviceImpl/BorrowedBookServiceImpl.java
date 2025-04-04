package serviceImpl;

import entity.Book;
import entity.BookBorrowed;
import entity.User;
import enums.ResponseStatus;
import enums.Status;
import lombok.extern.slf4j.Slf4j;
import repository.dao.BorrowDao;
import services.BookService;
import services.BorrowedBookService;
import services.UserService;
import utils.Response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
public class BorrowedBookServiceImpl implements BorrowedBookService {


    private final BorrowDao borrowDao;
    private final BookService bookService;
    private final UserService userService;

    public BorrowedBookServiceImpl(BorrowDao borrowDao, BookService bookService, UserService userService) {
        this.borrowDao = borrowDao;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public Response borrowBook(BookBorrowed bookBorrowed) {
        Response response = new Response();
        try {
            List<Book> books = bookService.fetchBooks();
            if (books.size() > Integer.parseInt(bookBorrowed.getBookId())) {
                Book book = books.get(Integer.parseInt(bookBorrowed.getBookId()));
                if (Objects.isNull(book)) {
                    return response;
                }
                Object userRes = userService.findUserById(bookBorrowed.getUserId()).getResponseObject();
                if (Objects.nonNull(userRes) && userRes instanceof User user) {
                    bookBorrowed.setUser(user);
                    int id = borrowDao.getBorrowBook().size() + 1;
                    bookBorrowed.setBorrowId("br" + id);
                    Book bookByName = (Book) bookService.getBookById(book.getBookId()).getResponseObject();
                    bookBorrowed.setBook(bookByName);
                    bookBorrowed.setReturnDate(bookBorrowed.getBorrowDate().plusDays(10));
                    bookBorrowed.setStatus(Status.Borrowed);
                    BookBorrowed bookBorrowed1 = borrowDao.addBorrowedBook(book, bookBorrowed);
                    if (Objects.nonNull(bookBorrowed1) && book.getNumberOfCopy() >= 1) {
                        book.setNumberOfCopy(book.getNumberOfCopy() - 1);
                        bookBorrowed.setBookSerialNumber(book.getSerialNumber());
                        user.setBorrowedBooksById(book);
                        userService.addUser(user);
                        response = Response.builder().message("Book Borrowed successfully...").statusCode(ResponseStatus.SUCCESS).build();
                    } else {
                        response = Response.builder().message("Book not available").statusCode(ResponseStatus.Error).build();
                    }
                }
            } else {
                response = Response.builder().message("Book not found").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("Something went wrong during borrow book try again..").statusCode(ResponseStatus.Error).build();
            log.error("error at book borrowed ", e);
        }
        return response;
    }

    @Override
    public Response returnBook(String bookId, User user) {
        AtomicReference<Response> response = new AtomicReference<>();
        try {
            if (user.getBorrowedBooks().size() > Integer.parseInt(bookId)) {
                Book book = user.getBorrowedBooks().get(Integer.parseInt(bookId));
                if (Objects.nonNull(book)) {
                    Optional<BookBorrowed> borrowedBookById = borrowDao.findBorrowedBookById(book.getBookId());
                    borrowedBookById.ifPresentOrElse(bookBorrowed -> {
                        fineCalculate(borrowedBookById.get().getFine(), borrowedBookById.get());
                        bookBorrowed.setStatus(Status.Returned);
                        List<Book> borrowedBooks = user.getBorrowedBooks();
                        borrowedBooks.remove(book);
                        book.setNumberOfCopy(book.getNumberOfCopy()+1);
                        bookService.updateBook(book);
                        user.setBorrowedBooks(borrowedBooks);
                        userService.updateUser(user);
                        response.set(Response.builder().message("Book return successfully..").statusCode(ResponseStatus.SUCCESS).build());
                    }, () -> {
                        response.set(Response.builder().message("Book return failed..").statusCode(ResponseStatus.Error).build());
                    });
                }
            } else {
                response.set(Response.builder().message("Book not found").statusCode(ResponseStatus.Error).build());
            }
        } catch (Exception e) {
            response.set(Response.builder().message("Something went wrong during return book try again..").statusCode(ResponseStatus.Error).build());
            log.error("error during return book :", e);
        }
        return response.get();
    }

    private void fineCalculate(double fine, BookBorrowed bookBorrowed) {
        long fineAmount = 0;
        if (!bookBorrowed.getReturnDate().isBefore(LocalDate.now())) {
            return;
        }
        fineAmount = ChronoUnit.DAYS.between(bookBorrowed.getReturnDate(), LocalDate.now());
        fine += fineAmount * 50;
        System.out.println("Fine : " + fine);
        bookBorrowed.setFine(fine);
    }

    @Override
    public Response fetchBorrowedBook() {
        Response response;
        try {
            Map<String, BookBorrowed> borrowBook = borrowDao.getBorrowBook();
            if (Objects.nonNull(borrowBook)) {
                response = Response.builder().responseObject(borrowBook).message("Book successfully fetch..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().message("Book fetch failed..").statusCode(ResponseStatus.Error).build();
            }

        } catch (Exception e) {
            response = Response.builder().message("Something went wrong..").statusCode(ResponseStatus.Error).build();
            log.info("Something went wrong during print the borrowed book", e);
        }
        return response;
    }
}