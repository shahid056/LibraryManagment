package userInterface;

import entity.User;
import enums.Role;
import server.AuthenticationServices;
import servicesImpl.AuthenticationServicesImpl;
import userInterface.dashboard.DashBoardAdmin;
import userInterface.dashboard.DashboardUser;
import utils.ValidatorRegx;
import utils.Response;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Home extends AbstractUi {
    static Scanner sc = new Scanner(System.in);
    AuthenticationServices authenticationServices = AuthenticationServicesImpl.getAuthenticationInstance();

    static {
        System.out.println("*********************************Welcome****************************************************");
    }

    @Override
    public void HomeScreen() {
        boolean isExit = true;
        int choice = 0;
        while (isExit) {
            displayOption(List.of("==========================Login========================================================",
                    "Enter 1 for login :",
                    "Enter 2 for SignUp:"));
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("please enter proper input");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> isLoginUser(signUpUser(choice));
                case 2 -> signUpUser(choice);
                case 3 -> isExit = false;
                default -> System.err.println("Invalid operation : ");
            }
        }
    }

    private void isLoginUser(User user) {
        if (user != null) {
            if (user.getRole().toString().equalsIgnoreCase("user")) {
                System.out.println(" ");
                new DashboardUser().UserScreen(user);
                System.out.println(" ");
            } else {
                new DashBoardAdmin().AdminScreen(user);
            }
        }
    }

    //Method to Take user inputs
    private User signUpUser(int option) {
        System.out.println("************Enter a Detail****************");
        System.out.println("Enter a email : ");
        var email = sc.next();
        if (! ValidatorRegx.isEmailValid(email)) {
            System.err.println("Enter a Valid Email...");
            return null;
        }
        System.out.println("Enter a password : ");
        var password = sc.next();
        if (password.length() < 4) {
            System.err.println("Password length should be grater then 3");
            return null;
        }
        if (option == 2) {
            System.out.println("Enter a name : ");
            var name = sc.next();
            User user = new User.UserBuilder().setName(name).setEmail(email).setRole(Role.user).setPassword(password).build();
            Response response =  authenticationServices.userRegistration(user);
            System.out.println(response.getMessage());
            return (User) response.getResponse();
        } else {
            Response response =authenticationServices.userLogin(email, password);
            System.err.println(response.getMessage());
            return (User) response.getResponse();
        }
    }
}
