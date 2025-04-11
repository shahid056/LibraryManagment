package userInterface;

import enums.Role;
import model.User;
import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.SerialNumberDao;
import repository.dao.UserDao;
import repository.daoimpl.BookDaoImpl;
import repository.daoimpl.BorrowDaoImpl;
import repository.daoimpl.SerialNumberDaoImpl;
import repository.daoimpl.UserDaoImpl;
import service.*;
import serviceImpl.*;
import userInterface.common.BookData;
import userInterface.common.UpdateUser;
import userInterface.dashboard.DashBoardAdmin;
import userInterface.dashboard.DashboardUser;
import utils.ConnectionDb;
import utils.Response;
import utils.ValidatorRegxUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Home extends AbstractUi {
    static Scanner sc = new Scanner(System.in);

    Connection connection = ConnectionDb.connectionDataBase();
    BookDao bookDao = new BookDaoImpl(connection);
    UserDao userDao = new UserDaoImpl(connection);
    SerialNumberDao serialNumberDao = new SerialNumberDaoImpl(connection);
    BorrowDao borrowDao = new BorrowDaoImpl(connection);

    UserService userService = new UserServiceImpl(userDao);
    AuthenticationService authenticationService = new AuthenticationServiceImpl(userService);
    BookService bookService = new BookServiceImpl(bookDao);
    AdminService adminService = new AdminServiceImpl(userService);
    SerialNumberService serialNumberService = new SerialNumberServiceImpl(serialNumberDao);
    BorrowedBookService borrowedBookService = new BorrowedBookServiceImpl(borrowDao, bookService, userService,serialNumberService);

    UpdateUser updateUser = new UpdateUser(userService);

    BookData bookData = new BookData(bookService);

    public Home() {
        BookData bookData1 = new BookData(bookService);
        //bookData1.loadBookData();
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
                        "Enter 1 for login:",
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
            email = sc.next().toLowerCase().trim();
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
        var password = sc.next().toLowerCase().trim();
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
            if (Boolean.FALSE.equals(name.isBlank()) && Boolean.FALSE.equals(email.isBlank()) && Boolean.FALSE.equals(password.isBlank())) {
                User user =  User.builder().name(name).email(email).role(Role.user).password(password).build();
                Response response = authenticationService.userRegistration(user);
                System.out.println(response.getMessage());
                return (User) response.getResponseObject();
            } else {
                System.out.println("Field should not be empty...");
            }
        } else {
            if(Boolean.FALSE.equals(email.isBlank()) && Boolean.FALSE.equals(password.isBlank())){
                Response response = authenticationService.userLogin(email, password);
                Object userResponse = response.getResponseObject();
                System.err.println(response.getMessage());
                return (User) userResponse;
            }else {
                System.err.println("Field should not be blank");
            }
        }
        return null;
    }
}
