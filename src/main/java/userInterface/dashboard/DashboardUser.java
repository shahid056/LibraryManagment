package userInterface.dashboard;

import entity.Book;
import entity.BookBorrowed;
import entity.User;
import enums.ResponseStatus;
import services.BookService;
import services.BorrowedBookService;
import services.UserService;
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
                case 3 -> bookBoorowAndReturn(3, user);
                case 4 -> bookBoorowAndReturn(4, user);
                case 5 -> isExit = false;
                default -> System.err.println("invalid operation");
            }
        }
    }

    private void displayAllBook() {
        List<Book> books = bookService.fetchBooks();
        displayBooks(books, false);
    }

    private void profileView(User user) {
        System.out.println("======================================================Profile====================================================================");
        System.out.println(user);
        updateUser.updateUser(user);
    }

    private void bookBoorowAndReturn(int caseInput, User user) {
        try {
            if (caseInput == 3) {
                while (true) {
                    displayAllBook();
                    System.out.println("Enter a BookId to Borrow (enter b for back to main menu ):");
                    String bookId = sc.next().toLowerCase();
                    if(bookId.equalsIgnoreCase("b")) break;
                    BookBorrowed bookBorrowed = BookBorrowed.builder().userId(user.getId()).bookId(bookId).borrowDate(LocalDate.now()).build();
                    Response response = borrowedBookService.borrowBook(bookBorrowed);
                    if (response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())) {
                        System.out.println(response.getMessage());
                    } else {
                        System.out.println(response.getMessage());
                    }
                }
            } else if (caseInput == 4) {
                if (displayBorrowedBook(user)) {
                    while (true) {

                        System.out.println("Enter a BookId to return (enter b for back to main menu ):");
                        String bookId = sc.next().toLowerCase();
                        if(bookId.equalsIgnoreCase("b")) break;
                        Response response = borrowedBookService.returnBook(bookId, user);
                        if (response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())) {
                            System.out.println(response.getMessage());
                        } else {
                            System.out.println(response.getMessage());
                            return;
                        }

                    }
                }
            }
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.err.println("Please enter proper inputs ");
        }
    }

    private boolean displayBorrowedBook(User user) {
        Object response = userService.userBorrowedBook(user).getResponseObject();
        List<Book> books = null;
        if (response instanceof List<?>) {
            books = (List<Book>) response;
        }
        if (!Objects.requireNonNull(books).isEmpty()) {
            displayBooks(books, true);
            return true;
        } else {
            System.out.println("you don't have any book to return");
        }
        return false;
    }

    private void displayBooks(List<Book> book, boolean hideCopy) {
        System.out.println(" ");
        System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
        System.out.println(String.format("|%-10s |%-30s | %-25s | %-28s |" + (hideCopy ? " " : "| %-20s |"), "BookId", "Name", "Category", "Author", (hideCopy ? " " : "Copies Available")));
        System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
        if (Objects.nonNull(book) && !book.isEmpty()) {
            AtomicInteger index = new AtomicInteger();
            book.forEach(bookPrint -> {
                String bookInfo = String.format("|%-10s |%-30s | %-25s | %-28s |" + (hideCopy ? " " : "| %-20s |"),
                        index.getAndIncrement(), bookPrint.getName(), bookPrint.getCategory(), bookPrint.getAuthor(), (hideCopy ? " " : bookPrint.getNumberOfCopy()));
                System.out.println(bookInfo);
            });
        } else {
            System.out.println("No Book Found");
        }
        System.out.println(" ");
    }


}
