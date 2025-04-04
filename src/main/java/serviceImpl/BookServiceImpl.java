package serviceImpl;

import entity.Book;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import repository.dao.BookDao;
import services.BookService;
import utils.Response;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Response addBook(Book book) {
        Response response = new Response();
        try {
            response = bookDao.isBookIsPresent(book).map(bookPresent -> {
                        int totalNumberOFCopy = bookPresent.getNumberOfCopy() + book.getNumberOfCopy();
                        bookPresent.setNumberOfCopy(totalNumberOFCopy);
                        List<String> allSerialNumber = book.getAllSerialNumber();
                        List<String> allSerialNumber1 = bookPresent.getAllSerialNumber();
                        allSerialNumber1.addAll(allSerialNumber);
                        return insertBookIntoDatabase(bookPresent);
                    }).
                    orElseGet(() -> insertBookIntoDatabase(book));
            return response;
        } catch (Exception e) {
            log.error("error during the  inserting book into database", e);
            response = Response.builder().
                    message("Error during inserting book into dataBase : ").
                    statusCode(ResponseStatus.Error).
                    build();
        }
        return response;
    }


    private Response insertBookIntoDatabase(Book book) {
        try {
            return bookDao.addBook(book).
                    map(savedBook -> Response.builder()
                            .responseObject(savedBook)
                            .message("Book Inserted Successful...")
                            .statusCode(ResponseStatus.SUCCESS)
                            .build()).
                    orElseGet(() -> Response.builder()
                            .message("Book Inserted failed...")
                            .statusCode(ResponseStatus.Error)
                            .build()
                    );
        } catch (Exception e) {
            log.error("Error during the book insert into dataBase : ", e);
            return Response.builder().
                    message("Error during inserting book into dataBase : ").
                    statusCode(ResponseStatus.Error).
                    build();
        }
    }

    @Override
    public Response deleteBook(String bookId, int noOfCopy, boolean deleteAllCopy) {
        Response response = new Response();
        if (Objects.isNull(bookId)) {
            return response;
        }
        try {
            List<Book> book = fetchBooks();
            Book books = book.get(Integer.parseInt(bookId));
            if (book.size() > Integer.parseInt(bookId)) {
                if (deleteAllCopy) {
                    if (bookDao.deleteBook(books)) {
                        response = Response.builder().statusCode(ResponseStatus.SUCCESS).message("Book Update Successful").build();
                    } else {
                        response = Response.builder().statusCode(ResponseStatus.Error).message("Book not Found").build();
                    }
                } else {
                    List<String> allSerialNumber = books.getAllSerialNumber();
                    List<String> updateSerialNumber = allSerialNumber.stream().skip(noOfCopy).collect(Collectors.toList());
                    books.setSrNo(updateSerialNumber);
                    books.setNumberOfCopy(books.getNumberOfCopy()-noOfCopy);
                    updateBook(books);
                    Optional<Book> bookOptional = bookDao.addBook(books);
                    if (bookOptional.isPresent()) {
                        response = Response.builder().statusCode(ResponseStatus.SUCCESS).message("Book Update Successful").build();
                    } else {
                        response = Response.builder().statusCode(ResponseStatus.Error).message("Book not Found").build();
                    }
                }
            }
        } catch (Exception e) {
            response = Response.builder().statusCode(ResponseStatus.Error).message("Something went wrong...").build();
            log.error("Error at delete book : ", e);
        }
        return response;
    }

    @Override
    public Response updateBook(Book book) {
        Response response = new Response();
        try {
            Optional<Book> bookOptional = bookDao.addBook(book);
            if (bookOptional.isPresent()) {
                response = Response.builder().responseObject(bookOptional.get()).statusCode(ResponseStatus.SUCCESS).message("Book Update Successful...").build();
            } else {
                response = Response.builder().statusCode(ResponseStatus.Error).message("Book not found...").build();
            }
        } catch (Exception e) {
            Response.builder().statusCode(ResponseStatus.Error).message("Something went wrong").build();
            log.info("Error during UpdateBook :", e);
        }
        return response;
    }

    @Override
    public List<Book> fetchBooks() {
        return bookDao.getBooks();
    }

    @Override
    public Response getBookById(String bookId) {
        Response response;
        Optional<Book> bookById = bookDao.findBookById(bookId);
        if (bookById.isPresent()) {
            response = Response.builder().responseObject(bookById.get()).statusCode(ResponseStatus.SUCCESS).message("Book found Successful...").build();
        } else {
            response = Response.builder().statusCode(ResponseStatus.Error).message("Book not found...").build();
        }
        return response;
    }
}

