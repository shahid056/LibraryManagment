package userInterface.common;

import entity.User;
import services.UserService;
import userInterface.AbstractUi;
import utils.Response;
import utils.ValidatorRegxUtil;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UpdateUser {
    Scanner sc = new Scanner(System.in);

    private final UserService userService;

    public UpdateUser(UserService userService) {
        this.userService = userService;
    }

    public void updateUser(User user) {
        boolean isExit = true;
        while (isExit) {
            AbstractUi.displayOption(List.of("Do u want to update", "enter 1 for name update : ", "enter 2 for  email update : ", "enter 3 for  update password : ", "enter 4 for back to menu : "));
            int input = 0;
            try {
                input = sc.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Enter a proper input..");
                sc.nextLine();
                updateUser(user);
            }
            switch (input) {
                case 1 -> updateData(1, user);
                case 2 -> updateData(2, user);
                case 3 -> updateData(3, user);
                case 4 -> isExit = false;
                default -> System.out.println("Enter a proper input : ");
            }
        }
    }

    private void updateData(int op, User user) {
        switch (op) {
            case 1->nameUpdate(user);
            case 2->emailUpdate(user);
            case 3->passswordUpdate(user);
            default -> System.err.println("Enter a valid input :");
        }
    }

    private void nameUpdate(User user){
        sc.nextLine();
        System.out.println("Enter a name :");
        String name = sc.nextLine();
        user.setName(name);
        Response response = userService.updateUser(user);
        System.out.println(response.getMessage());
        System.out.println(user);
    }

    private void emailUpdate(User user){
        sc.nextLine();
        String oldEmail = takeValidEmail("Enter a old email :");
        if (Objects.nonNull(oldEmail)) {
            String newEmail = takeValidEmail("Enter a new email: ");
            if (user.getEmail().equalsIgnoreCase(oldEmail)) {
                user.setEmail(newEmail);
                Response response = userService.updateUser(user);
                System.out.println(response.getMessage());
                System.out.println(user);
            } else {
                System.out.println(" ");
                System.err.println("old Email not match");
                System.out.println(" ");
            }
        } else {
            System.out.println(" ");
            System.out.println("Back to main menu");
            System.out.println(" ");
        }

    }

    private void passswordUpdate(User user){
        sc.nextLine().trim().toLowerCase();
        System.out.println("Enter a password : ");
        String password = sc.next().trim();
        user.setPassword(password);
        Response response = userService.updateUser(user);
        System.out.println(response.getMessage());
        System.out.println(user);
    }

    private String takeValidEmail(String msg) {
        String email = null;
        while (true) {
            System.out.println(msg + "Enter b for back to menu : ");
            email = sc.nextLine().trim().toLowerCase();
            if (email.equalsIgnoreCase("b")) {
                return null;
            }
            if (ValidatorRegxUtil.isEmailValid(email)) {
                break;
            } else {
                System.err.println("Enter a valid email ");
            }
        }
        return email;
    }
}
