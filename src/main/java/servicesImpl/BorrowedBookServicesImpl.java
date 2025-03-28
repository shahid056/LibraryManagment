package servicesImpl;

import entity.BookBorrowed;
import entity.User;
import enums.ResponseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.dao.BorrowDao;
import repository.daoimpl.BorrowDoaImpl;
import server.BorrowedBookServices;
import utils.Response;

import java.util.Map;
import java.util.Objects;

public class BorrowedBookServicesImpl implements BorrowedBookServices {

    Logger log = LogManager.getLogger();
    private static BorrowedBookServices borrowedBookServices;
    private final BorrowDao borrowDao= BorrowDoaImpl.getInstance();

    private BorrowedBookServicesImpl() {
    }

    public  static BorrowedBookServices getInstance(){
        if(Objects.isNull(borrowedBookServices)){
            borrowedBookServices=new BorrowedBookServicesImpl();
        }
        return borrowedBookServices;
    }

    @Override
    public Response borrowBook(BookBorrowed bookBorrowed) {
        Response response = new Response(null, ResponseStatus.Error, "=======================================something went wrong=================================");
        if (Objects.isNull(bookBorrowed)) {
            return null;
        }
        try {
            BookBorrowed bookBorrowed1 = borrowDao.borrowBook(bookBorrowed);
            if(Objects.nonNull(bookBorrowed1)){
                response = new Response(bookBorrowed, ResponseStatus.SUCCESS, "=====================================Book Borrowed Successful===============================");
            }
            else {
                response = new Response(bookBorrowed, ResponseStatus.Error, "========================================Book not found==========================================");
            }
        } catch (Exception e) {
             log.info("error at book borrowed ");
        }
        return response;
    }

    @Override
    public Response returnBook(String bookId, User user) {
        Response response = new Response(null, ResponseStatus.Error, "=============================something went wrong==================================");
        try {
            BookBorrowed isRemoved = borrowDao.returnBook(bookId, user);
            if (Objects.nonNull(isRemoved)) {
                response = new Response(null, ResponseStatus.SUCCESS, "=============================Book removed Successful=============================");
            }
        } catch (Exception e) {
            log.info("Something Went Wrong During Return Book ",e.getMessage());
        }
        return response;
    }

    @Override
    public void transactionBook() {
        try {
            Map<String, BookBorrowed> borrowBook = borrowDao.getBorrowBook();
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            System.out.println(String.format("| %-32s | %-15s | %-16s |%-16s |%-16s  |%-16s ", "user", "Date of Borrow", "Due Date", "Return Date", "Fine", "Status"));
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            borrowBook.forEach((key, value) -> System.out.println(value));
        } catch (Exception e) {
           log.info("Something went wrong during print the borrowed book",e);
        }
    }
}