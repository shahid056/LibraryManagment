package serviceImpl;

import lombok.NonNull;
import model.Book;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import repository.dao.BookDao;
import service.BookService;
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
            Optional<List<Book>> books = bookDao.getBooks();
            List<Book> bookList = null;
            if(books.isPresent()){
               bookList  = books.get();
            }

            if (Objects.nonNull(bookList)) {
               if(bookList.contains(book)){
                   response = Response.builder().
                           message("Book already exist").
                           statusCode(ResponseStatus.Error).
                           build();
               }else {
                   response = insertBookIntoDatabase(book);
               }
            }
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
    public Response deleteBook(Book book) {
        Response response = new Response();
        try {
            if (Objects.nonNull(book)) {
                if (bookDao.deleteBook(book)) {
                    response = Response.builder().responseObject(Optional.of(book)).statusCode(ResponseStatus.SUCCESS).message("Book Update Successful").build();
                } else {
                    response = Response.builder().responseObject(Optional.empty()).statusCode(ResponseStatus.Error).message("Book not Found").build();
                }
            }else {
                response = Response.builder().responseObject(Optional.empty()).statusCode(ResponseStatus.Error).message("Book not Found").build();
            }
        } catch (Exception e) {
            response = Response.builder().statusCode(ResponseStatus.Error).message("Something went wrong...").build();
            log.error("Error at delete book : ", e);
        }
        return response;
    }

    @Override
    public Response updateBook(Book book, String columName) {
        Response response = new Response();
        try {
            Optional<Book> bookOptional = bookDao.updateBook(book, columName);
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
    public Response fetchBooks() {
        Response response = new Response();
        try {
            Optional<List<Book>> bookOptional = bookDao.getBooks();
            if (bookOptional.isPresent()) {
                response = Response.builder().responseObject(bookOptional.get()).statusCode(ResponseStatus.SUCCESS).message("Book Update Successful...").build();
            } else {
                response = Response.builder().responseObject(null).statusCode(ResponseStatus.Error).message("Book not found...").build();
            }
        } catch (Exception e) {
            Response.builder().statusCode(ResponseStatus.Error).message("Something went wrong").build();
            log.info("Error during UpdateBook :", e);
        }
        return response;
    }

    @Override
    public Response getBookById(String bookId) {
        Response response;
        try {
            Book bookResp =bookDao.getBooksById(bookId).orElse(null);
            if (Objects.nonNull(bookResp)) {
                response = Response.builder().responseObject(bookResp).statusCode(ResponseStatus.SUCCESS).message("Book found Successful...").build();
            }else {
                response = Response.builder().statusCode(ResponseStatus.Error).message("Book not found...").build();
            }
        } catch (Exception e) {
            response = Response.builder().statusCode(ResponseStatus.Error).message("something went wrong during book update...").build();
            log.error("something went wrong during book update", e);
        }
        return response;
    }
}

