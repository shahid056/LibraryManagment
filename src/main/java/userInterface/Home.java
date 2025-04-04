package userInterface;

import entity.User;
import enums.Role;
import repository.dao.AuthenticationDao;
import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.UserDao;
import repository.daoimpl.AuthenticationDoaImpl;
import repository.daoimpl.BookDaoImpl;
import repository.daoimpl.BorrowDaoImpl;
import repository.daoimpl.UserDaoImpl;
import services.*;
import serviceImpl.*;
import userInterface.common.BookData;
import userInterface.common.UpdateUser;
import userInterface.dashboard.DashBoardAdmin;
import userInterface.dashboard.DashboardUser;
import utils.ValidatorRegxUtil;
import utils.Response;


import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class Home extends AbstractUi {
    static Scanner sc = new Scanner(System.in);


    BookDao bookDao = new BookDaoImpl();
    UserDao userDao = new UserDaoImpl();
    AuthenticationDao authenticationDao = new AuthenticationDoaImpl(userDao);
    BorrowDao borrowDao = new BorrowDaoImpl(userDao, bookDao);

    UserService userService = new UserServiceImpl(userDao);
    AuthenticationService authenticationService = new AuthenticationServiceImpl(authenticationDao, userService);
    BookService bookService = new BookServiceImpl(bookDao);
    AdminService adminService = new AdminServiceImpl(userService);
    BorrowedBookService borrowedBookService = new BorrowedBookServiceImpl(borrowDao, bookService, userService);

    UpdateUser updateUser = new UpdateUser(userService);

    BookData bookData = new BookData(bookService);

    public Home() {
        BookData bookData1 = new BookData(bookService);
        bookData1.loadBookData();
    }

    static {
        System.out.println("*********************************Welcome****************************************************");
    }

    @Override
    public void homeScreen() {
        boolean isExit = true;
        int choice = 0;
        while (isExit) {
            try {
                displayOption(List.of("==========================Login========================================================",
                        "Enter 1 for login :",
                        "Enter 2 for SignUp:"));
                choice = sc.nextInt();
                switch (choice) {
                    case 1 -> isLoginUser(validateUser(choice));
                    case 2 -> validateUser(choice);
                    case -1 -> isExit = false;
                    default -> System.err.println("Invalid operation : ");
                }
            } catch (Exception ex) {
                System.err.println("please enter proper input");
                sc.nextLine();
            }
        }
    }

    private void isLoginUser(User user) {
        if (user != null) {
            if (user.getRole().toString().equalsIgnoreCase("user")) {
                System.out.println(" ");
                new DashboardUser(bookService, userService, borrowedBookService, updateUser).userScreen(user);
                System.out.println(" ");
            } else {
                new DashBoardAdmin(userService, bookService, adminService, borrowedBookService, updateUser).adminScreen(user);
            }
        }
    }

    //Method to Take user inputs
    private User validateUser(int option) {
        System.out.println("************Enter a Detail****************");
        boolean isNotValid = true;
        String email = null;
        while (isNotValid) {
            System.out.println("Enter a email (for back to menu type b): ");
            email = sc.next().trim();
            if (email.equalsIgnoreCase("b")) {
                return null;
            }
            if (!ValidatorRegxUtil.isEmailValid(email)) {
                System.err.println("Enter a Valid Email...");
            } else {
                isNotValid = false;
            }
        }
        System.out.println("Enter a password (for back to menu type b): ");
        var password = sc.next().trim();
        if (password.equalsIgnoreCase("b")) {
            return null;
        }
        if (password.length() < 4) {
            System.err.println("Password length should be grater then 3");
            return null;
        }
        if (option == 2) {
            System.out.println("Enter a name (for back to menu type b): ");
            var name = sc.next().trim();
            if (name.equalsIgnoreCase("b")) {
                return null;
            }
            if (Stream.of(name, email, password).allMatch(Objects::nonNull)) {
                User user = new User.UserBuilder().setName(name).setEmail(email).setRole(Role.user).setPassword(password).build();
                Response response = authenticationService.userRegistration(user);
                System.out.println(response.getMessage());
                return (User) response.getResponseObject();
            } else {
                System.out.println("Field should not be empty...");
            }
        } else {
            Response response = authenticationService.userLogin(email, password);
            System.err.println(response.getMessage());
            return (User) response.getResponseObject();
        }
        return null;
    }
}
