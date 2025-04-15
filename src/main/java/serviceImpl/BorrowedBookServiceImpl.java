package serviceImpl;

import enums.ResponseStatus;
import enums.Status;
import lombok.extern.slf4j.Slf4j;
import model.Book;
import model.BookBorrowed;
import model.User;
import repository.dao.BorrowDao;
import service.BookService;
import service.BorrowedBookService;
import service.SerialNumberService;
import service.UserService;
import utils.Response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
public class BorrowedBookServiceImpl implements BorrowedBookService {

    private final BorrowDao borrowDao;
    private final BookService bookService;

    public BorrowedBookServiceImpl(BorrowDao borrowDao, BookService bookService) {
        this.borrowDao = borrowDao;
        this.bookService = bookService;
    }

    @Override
    public Response borrowBook(BookBorrowed bookBorrowed) {
        Response response = new Response();
        try {
            if (Objects.nonNull(bookBorrowed)) {
                Book book = bookBorrowed.getBook();
                if (book.getNumberOfCopyAvailable() >= 1) {
                    bookBorrowed.setBookSerialNumber(serialNumberGenerator(book));
                    bookBorrowed.setReturnDate(bookBorrowed.getBorrowDate().plusDays(10));
                    bookBorrowed.setStatus(Status.Borrowed);
                    boolean bookBorrowed1 = borrowDao.addBorrowedBook(book, bookBorrowed);
                    if (bookBorrowed1) {
                        book.setNumberOfCopyAvailable(book.getNumberOfCopyAvailable() - 1);
                        bookService.updateBook(book, "number_of_available_copy");
                        response = Response.builder().message("Book Borrowed successfully...").statusCode(ResponseStatus.SUCCESS).build();
                    }
                } else {
                    response = Response.builder().message("Book not available").statusCode(ResponseStatus.Error).build();
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


    private synchronized String serialNumberGenerator(Book book) {
        try {
            return book.getName().substring(0, 2) +
                    book.getBookId() + book.getAuthor().substring(0, 2) +
                    book.getCategory().toString().substring(0, 2) +
                    (book.getEdition() == 0 ? "NA" : book.getEdition()) +
                    (book.getNumberOfCopyAvailable() - book.getTotalNumberOfCopy());
        } catch (Exception e) {
            log.error("some thing went wrong during serialNumberGenerator ", e);
        }
        return "";
    }

    @Override
    public Response returnBook(Book book, User user) {
        AtomicReference<Response> response = new AtomicReference<>();
        try {
            if (Objects.nonNull(book) && Objects.nonNull(user)) {
                List<BookBorrowed> borrowBook = borrowDao.getBorrowBook();
                if (Boolean.FALSE.equals(borrowBook.isEmpty())) {
                    Optional<BookBorrowed> bookBorrowed = borrowDao.fetchBorrowedUserIdBookId(user.getId(), book.getBookId());
                    if (bookBorrowed.isPresent()) {
                        bookBorrowed.get().setStatus(Status.Returned);
                        double fine = fineCalculate(bookBorrowed.get());
                        bookBorrowed.get().setFine(fine);
                        book.setNumberOfCopyAvailable(book.getNumberOfCopyAvailable() + 1);
                        try {
                            borrowDao.returnBook(bookBorrowed.get()).ifPresentOrElse(
                                    bookBorrowed3 -> {
                                        response.set(Response.builder()
                                                .message("Book returned successfully.")
                                                .statusCode(ResponseStatus.SUCCESS)
                                                .build());
                                        bookService.updateBook(book, "number_of_available_copy");
                                    },
                                    () -> {
                                        response.set(Response.builder()
                                                .message("Book not found or already returned.")
                                                .statusCode(ResponseStatus.Error)
                                                .build());
                                    }
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        response.set(Response.builder().message("Book not found").statusCode(ResponseStatus.Error).build());
                    }
                }
            }
        } catch (Exception e) {
            response.set(Response.builder().message("Something went wrong during return book try again..").statusCode(ResponseStatus.Error).build());
            log.error("error during return book :", e);
        }
        return response.get();
    }

    private double fineCalculate(BookBorrowed bookBorrowed) {
        double fineAmount = 0;
        if (!bookBorrowed.getReturnDate().isBefore(LocalDate.now())) {
            return 0.0;
        }
        fineAmount = ChronoUnit.DAYS.between(bookBorrowed.getReturnDate(), LocalDate.now());
        double fine = bookBorrowed.getFine() + fineAmount * 50;
        return fine;
    }

    @Override
    public Response fetchBorrowedBookByUserId(String userId) {
        Response response;
        try {
            List<BookBorrowed> borrowBook = borrowDao.fetchBorrowedUserId(userId);
            if (Boolean.FALSE.equals(borrowBook.isEmpty())) {
                response = Response.builder().responseObject(borrowBook).message("Book successfully fetch..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().message("Book fetch failed..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("no book found").statusCode(ResponseStatus.Error).build();
            log.info("Something went wrong during print the borrowed book", e);
        }
        return response;
    }

    @Override
    public Response fetchBorrowedBookById() {
        Response response;
        try {
            List<BookBorrowed> borrowBook = borrowDao.getBorrowBook();
            if (Boolean.FALSE.equals(borrowBook.isEmpty())) {
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