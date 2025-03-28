package userInterface.dashboard;

import entity.Book;
import entity.User;
import enums.BookCategory;
import enums.ResponseStatus;
import enums.Role;
import server.BorrowedBookServices;
import server.UserServices;
import servicesImpl.BookServicesImpl;
import servicesImpl.BorrowedBookServicesImpl;
import servicesImpl.UserServicesImpl;
import userInterface.AbstractUi;
import utils.Response;

import javax.management.openmbean.InvalidOpenTypeException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DashBoardAdmin extends AbstractUi {
    static Scanner sc = new Scanner(System.in);
    BookServicesImpl bookServices = BookServicesImpl.getBookInstance();
    UserServices userServicesImpl = UserServicesImpl.getInstance();
    BorrowedBookServices borrowedBookServices = BorrowedBookServicesImpl.getInstance();

    //admin
    public void AdminScreen(User user) {
        boolean isExit = true;
        while (isExit) {
            System.out.println(" ");
            displayOption(List.of("*************Library (Admin)***********************",
                    "Enter 1 for check profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Add Book",
                    "Enter 4 for remove Book",
                    "Enter 5 for transaction Details",
                    "Enter 6 for Display All Users",
                    "Enter 7 for add admin Users",
                    "Enter 8 for delete admin Users",
                    "Enter 9 for Book update",
                    "Enter 10 for sign out")
            );
            System.out.println(" ");
            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> profileView(user);
                case 2 -> bookServices.displayBook(user);
                case 3 -> bookAdd();
                case 4 -> removeBook();
                case 5 -> borrowedBookServices.transactionBook();
                case 6 -> displayUser();
                case 7 -> addAdmin();
                case 8 -> removeAdmin(user);
                case 9 -> bookUpdate(user);
                case 10 -> {
                    isExit = false;
                }
                default -> System.err.println("invalid operation");
            }
        }
    }

    private void bookUpdate(User user) {
        bookServices.displayBook(user);
        System.out.println("Enter a  book id to update : ");
        String bookId = sc.nextLine();
        Response bookById = bookServices.getBookById(bookId);
        Object book = bookById.getResponse();
        if (book instanceof Book bookData) {
            boolean isExit = true;
            while (isExit) {
                System.out.println(" ");
                AbstractUi.displayOption(List.of("What data u want to update :",
                        "Enter 1 for update book name : ",
                        "Enter 3 for update Number of copy : ",
                        "Enter 4 for Back to Main Menu : "));
                int op = sc.nextInt();
                switch (op) {
                    case 1 -> System.out.println(updateName(bookData).getMessage());
                    case 2 -> System.out.println(updateAuthor(bookData).getMessage());
                    case 3 -> System.out.println(updateCopy(bookData).getMessage());
                    case 4 -> isExit = false;
                    default -> System.err.println("Invalid operations...");
                }
            }
        }
    }

    private Response updateCopy(Book book) {
        sc.nextLine();
        try {
            System.out.println("Enter new Copy name of Book : ");
            System.out.println("Enter back to -1 to menu :");
            int bookCount = sc.nextInt();
            if (bookCount == -1) {
                return new Response(null, ResponseStatus.Error, "Back to menu");
            }
            book.setNumberOfCopy(bookCount);
        } catch (InputMismatchException e) {
            System.err.println("Enter a proper value ..");
        }
        return bookServices.updateBook(book);
    }

    private Response updateAuthor(Book book) {
        sc.nextLine();
        try {
            System.out.println("Enter  Author name of Book : ");
            System.out.println("Enter back to back to menu :");
            String bookAuthorName = sc.nextLine();
            if (bookAuthorName.equalsIgnoreCase("back")) {
                return new Response(null, ResponseStatus.Error, "Back to menu");
            }
            book.setAuthor(bookAuthorName);
        } catch (InputMismatchException e) {
            System.out.println("Enter a proper value ");
        }
        return bookServices.updateBook(book);
    }

    private Response updateName(Book book) {
        sc.nextLine();
        try {
            System.out.println("Enter new name of Book : ");
            System.out.println("Enter back to back to menu :");
            String bookName = sc.nextLine();
            if (bookName.equalsIgnoreCase("back")) {
                return new Response(null, ResponseStatus.Error, "Back to menu");
            }
            book.setName(bookName);
        } catch (InvalidOpenTypeException e) {
            System.err.println("Enter a proper input");
        }
        return bookServices.updateBook(book);
    }

    private void profileView(User user) {
        System.out.println("======================================================Profile====================================================================");
        System.out.println(user);
        userServicesImpl.updateUser(user);
    }

    private void displayUser() {
        try {
            userServicesImpl.getUser();
        } catch (Exception e) {
            System.out.println("something went wrong..");
        }
    }

    private void addAdmin() {
        try {
            System.out.println("Enter a Email of user:");
            String email = sc.nextLine();
            System.out.println("Enter a name of user :");
            String name = sc.nextLine();
            System.out.println("set a password  of user :");
            String password = sc.nextLine();
            User user = new User.UserBuilder().setName(name).setEmail(email).setPassword(password).setRole(Role.admin).build();
            Response response = userServicesImpl.addUserByAdmin(user);
            System.out.println(response.getMessage());
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Enter a proper value...");
        }
    }

    private void removeAdmin(User user){
        try {
            System.out.println("Enter a Email of user:");
            String email = sc.nextLine();
            if(user.getEmail().equalsIgnoreCase(email)){
                System.out.println(" ");
                System.err.println("user and delete user should not be same");
                System.out.println(" ");
                return;
            }
            Response response = userServicesImpl.removeAdmin(email);
            System.out.println(response.getMessage());
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Enter a proper value...");
        }
    }

    //Add Book
    private void bookAdd() {
        try {
            System.out.println("Enter a Book name : ");
            String bookName = sc.nextLine();
            System.out.println("Enter a Book Author : ");
            String bookAuthor = sc.nextLine();
            System.out.println("Enter a Book Category : ");
            displayCategoryEnumValue();
            String category = sc.next();
            if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
                sc.nextLine();
                System.out.println(" ");
                System.err.println("Enter a proper category...");
            }
            System.out.println("Enter a Book Copy : ");
            int copy = sc.nextInt();
            Book book = new Book.BookBuilder().
                    setName(bookName).setAuthor(bookAuthor).
                    setCategory(BookCategory.valueOf(category.toUpperCase().trim())).
                    setNumberOfCopy(copy).
                    build();
            Response bookResponse = BookServicesImpl.getBookInstance().addBook(book);
            System.out.println(bookResponse.getMessage());
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.err.println("Enter a proper value :");
        }
    }

    private static void displayCategoryEnumValue() {
        System.out.print("Categories :[");
        for (BookCategory e : BookCategory.values()) {
            System.out.print(e + ",");
        }
        System.out.println("]");
    }

    //delete book
    private void removeBook() {
        System.out.println("Enter a book srNo to delete :");
        String srNo = sc.nextLine();
        System.out.println("Enter a how many copy want to delete :");
        int noCopy = sc.nextInt();
        Response response = BookServicesImpl.getBookInstance().deleteBook(srNo, noCopy);
        System.out.println(response.getMessage());
    }
}


