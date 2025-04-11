package userInterface.dashboard;

import model.Book;
import model.BookBorrowed;
import model.User;
import enums.ResponseStatus;
import service.BookService;
import service.BorrowedBookService;
import service.UserService;
import userInterface.AbstractUi;
import userInterface.common.UpdateUser;
import utils.Response;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DashboardUser extends AbstractUi {
    static Scanner sc = new Scanner(System.in);


    private final BookService bookService;
    private final UserService userService;
    private final BorrowedBookService borrowedBookService;
    private final UpdateUser updateUser;

    public DashboardUser(BookService bookService, UserService userService, BorrowedBookService borrowedBookService, UpdateUser updateUser) {
        this.bookService = bookService;
        this.userService = userService;
        this.borrowedBookService = borrowedBookService;
        this.updateUser = updateUser;
    }


    public void userScreen(User user) {
        boolean isExit = true;
        while (isExit) {
            displayOption(List.of("*************Library***********************",
                    "Enter 1 for profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Borrow Book",
                    "Enter 4 for Return Book",
                    "Enter 5 for sign out"));
            int choice;
            try {
                System.out.println("Enter Your Options:");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("please enter proper input");
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> profileView(user);
                case 2 -> displayAllBook();
                case 3 -> bookBorrow(user);
                case 4 -> bookReturn(user);
                case 5 -> isExit = false;
                default -> System.err.println("invalid operation");
            }
        }
    }

    private Map<Integer, Book> displayAllBook() {
        Object bookObject = bookService.fetchBooks().getResponseObject();
        List<Book> books = null;
        if (bookObject instanceof List<?>) {
            books = (List<Book>) bookObject;
        }
        return displayBooks(books, false, true);
    }


    private void profileView(User user) {
        System.out.println("======================================================Profile====================================================================");
        System.out.println(user);
        updateUser.updateUser(user);
    }

    private void bookBorrow(User user) {

        while (true) {
            try {
                Map<Integer, Book> bookMap = displayAllBook();
                System.out.println("Enter a BookId to Borrow (enter -1 for back to main menu ):");
                int userInputBookId = sc.nextInt();
                if (userInputBookId == -1) break;
                if (Boolean.TRUE.equals(bookMap.isEmpty())) {
                    System.out.println("no book found");
                    return;
                }
                Book book = bookMap.get(userInputBookId);
                BookBorrowed bookBorrowed = BookBorrowed.builder().userId(user.getId()).book(book).bookId(book.getBookId()).borrowDate(LocalDate.now()).build();
                Response response = borrowedBookService.borrowBook(bookBorrowed);
                System.out.println(response.getMessage());
            } catch (Exception e) {
                System.err.println("Please enter proper inputs ");
            }
        }
    }

    private void bookReturn(User user) {
        Map<Integer, Book> bookMap = displayBorrowedBook(user);
        while (true) {
            try {
                if(bookMap.isEmpty()){
                    System.err.println("no book found");
                    return;
                }
                System.out.println("Enter a BookId to return (enter -1 for back to main menu ):");
                int bookId = sc.nextInt();
                if (bookId == -1) break;
                Book book = bookMap.get(bookId);
                Response response = borrowedBookService.returnBook(book, user);
                System.out.println(response.getMessage());
                bookMap = displayBorrowedBook(user);
                if (bookMap.isEmpty()) {
                    System.err.println("don't have any book to return");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Please enter proper inputs ");
            }
        }
    }

private Map<Integer, Book> displayBorrowedBook(User user) {
    Object response = userService.userBorrowedBook(user).getResponseObject();
    if (response instanceof List<?> book) {
        List<Book> books = (List<Book>) book;
        if (Boolean.FALSE.equals(books.isEmpty())) {
            return displayBooks(books, true, true);
        } else {
            System.out.println("you don't have any book to return");
        }
    }
    return Collections.emptyMap();
}

private Map<Integer, Book> displayBooks(List<Book> book, boolean hideCopy, boolean isPrint) {
    System.out.println(" ");
    Map<Integer, Book> bookMap = new HashMap<>();
    if (isPrint) {
        System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
        System.out.println(String.format("|%-10s |%-30s | %-25s | %-28s |" + (hideCopy ? " " : "| %-20s |"), "BookId", "Name", "Category", "Author", (hideCopy ? " " : "Copies Available")));
        System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
        if (Objects.nonNull(book) && !book.isEmpty()) {
            AtomicInteger index = new AtomicInteger();

            book.forEach(books ->
                    bookMap.put(index.getAndIncrement() + 1, books)
            );
            bookMap.entrySet().forEach(bookPrint -> {
                String bookInfo = String.format("|%-10s |%-30s | %-25s | %-28s |" + (hideCopy ? " " : "| %-20s |"),
                        bookPrint.getKey(), bookPrint.getValue().getName(), bookPrint.getValue().getCategory(), bookPrint.getValue().getAuthor(), (hideCopy ? " " : bookPrint.getValue().getNumberOfCopyAvailable()));
                System.out.println(bookInfo);
            });
        } else {
            System.out.println("No Book Found");
        }
    }
    System.out.println(" ");
    return bookMap;
}

}
