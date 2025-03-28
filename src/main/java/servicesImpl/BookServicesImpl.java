package servicesImpl;

import entity.Book;
import entity.User;
import enums.ResponseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.dao.BookDao;
import repository.daoimpl.BookDaoImpl;
import server.BookService;
import utils.Response;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class BookServicesImpl implements BookService {
    private final BookDao bookDao = BookDaoImpl.getBookInstance();

    private static BookServicesImpl bookInstance;

    private BookServicesImpl() {
    }

    @Override
    public Response addBook(Book book) {
        Response response = new Response(null, ResponseStatus.Error, "==================================no book found please enter a proper book===============================");
        try {
            Optional<Book> bookData = bookDao.addBook(book);
            bookData.ifPresent(x -> response.    //);
            if(Objects.nonNull(bookData)) {
                response = new Response(bookData, ResponseStatus.SUCCESS, "==========================================Book inserted successful...==================================");
            }
            return response;
        } catch (Exception e) {
            System.out.println("exception while adding the book into database ");
            log.info(e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteBook(String srNo, int noOfCopy) {
        Response response = new Response(null, ResponseStatus.Error, "No Book Found");
        if (Objects.isNull(srNo)) {
            return response;
        }
        try {
            if (bookDao.deleteBook(srNo, noOfCopy)) {
                response = new Response(null, ResponseStatus.SUCCESS, "Book Delete Successful...");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateBook(Book book) {
        Response response = new Response(null, ResponseStatus.Error, "No Book Found");
        try {
            bookDao.addBook(book);
            response = new Response(book, ResponseStatus.SUCCESS, "Book Update Successful");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    @Override
    public void displayBook(User user) {
        List<Book> books = bookDao.getBooks();
        String role = user.getRole().toString();
        System.out.println(" ");
        System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
        System.out.println(String.format("|%-10s |%-30s | %-25s | %-28s | %-20s |", "BookId", "Name", "Category", "Author", "Copies Available"));
        System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
        if (Objects.nonNull(books) && !books.isEmpty()) {
            for (Book book : books) {
                String bookInfo = String.format("|%-10s |%-30s | %-25s | %-28s | %-20s |",
                        book.getBookId(), book.getName(), book.getCategory(), book.getAuthor(), book.getNumberOfCopy());
                System.out.println(bookInfo);
            }
            if (role.equalsIgnoreCase("superadmin") || role.equalsIgnoreCase("admin")) {
                checkSerialNumberOfBook();
            }
        } else {
            System.out.println("No Book Found");
        }
        System.out.println(" ");
    }

    @Override
    public Response getBookById(String bookId) {
        Response response = new Response(null, ResponseStatus.Error, "No Book Found");
        Optional<Book> bookById = bookDao.findBookById(bookId);
        if(bookById.isPresent()){
            response = new Response(bookById.get(), ResponseStatus.SUCCESS, "Book Found");
        }
        return response;
    }

    private void displaySerialNumberOfBook(String bookId) {
        Optional<Book> bookById = bookDao.findBookById(bookId);
        bookById.ifPresent(book -> {
            System.out.println("serial numbers for the " + book.getName() +" book is :");
            List<String> allSerialNumber = book.getAllSerialNumber();
            for (String serialList : allSerialNumber) {
                System.out.println(serialList);
            }
        });
    }

    private void checkSerialNumberOfBook(){
        System.err.println("Want to check the serial  number Y/N :");
        char op = sc.nextLine().charAt(0);
        if (op == 'y' || op == 'Y') {
            System.out.println("Enter a book id : ");
            String bookId = sc.nextLine();
            displaySerialNumberOfBook(bookId);
        }
    }
}

