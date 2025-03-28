package servicesImpl;

import entity.User;
import enums.ResponseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.dao.UserDao;
import repository.daoimpl.UserDaoImpl;
import server.UserServices;
import userInterface.AbstractUi;
import utils.Response;

import java.util.*;

public class UserServicesImpl implements UserServices {
    Logger log = LogManager.getLogger();
    private final UserDao userDao = UserDaoImpl.getInstance();
    private static UserServices userServices;
    Scanner sc = new Scanner(System.in);

    private UserServicesImpl() {
    }

    public static UserServices getInstance() {
        if (Objects.isNull(userServices)) {
            userServices = new UserServicesImpl();
        }
        return userServices;
    }

    @Override
    public void updateUser(User user) {
        boolean isExit=true;
        while (isExit) {
            AbstractUi.displayOption(List.of("Do u want to update", "enter 1 for name update : ", "enter 2 for  email update : ", "enter 3 for  update password : ", "enter 4 for  exit : "));
            int input = 0;
            try {
                input = sc.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Enter a proper input..");
                sc.nextLine();
                updateUser(user);
            }
            switch (input) {
                case 1 -> System.out.println(updateName(user).getMessage());
                case 2 -> System.out.println(updateEmail(user));
                case 3 -> System.out.println(passwordUpdate(user));
                case 4-> isExit=false;
                default -> System.out.println("Enter a proper input : ");
            }
        }
    }

    private Response updateName(User user) {
        Response response = new Response(null, ResponseStatus.Error, "something went wrong");
        sc.nextLine();
        System.out.println("Enter a name :");
        String name = sc.nextLine().trim().toLowerCase();
        user.setName(name);
        User isUserUpdate = null;
        try {
            isUserUpdate = userDao.add(user);
             response = new Response(user, ResponseStatus.Error, "=================================name update successful...===========================================");
            System.out.println(" ");
        } catch (Exception e) {
            log.error("something went wrong...");

        }
        if (Objects.nonNull(isUserUpdate)) {
            response = new Response(user, ResponseStatus.SUCCESS, "your name update successfully...");
        }
        return response;
    }

    private Response updateEmail(User user) {
        Response response = new Response(null, ResponseStatus.Error, "something went wrong");
        sc.nextLine();
        System.out.println("Enter a old email :");
        String oldEmail = sc.nextLine().trim().toLowerCase();
        System.out.println("Enter a new email :");
        String newEmail = sc.nextLine().trim().toLowerCase();
        if (user.getEmail().equalsIgnoreCase(oldEmail)) {
            user.setEmail(newEmail);
            User isUserUpdate = null;
            try {
                isUserUpdate = userDao.add(user);
                response = new Response(null, ResponseStatus.Error, "===========================================old email not match...========================================================");
                System.out.println(" ");
            } catch (Exception e) {
                log.error("something went wrong", e);
            }
            if (Objects.nonNull(isUserUpdate)) {
                response = new Response(user, ResponseStatus.SUCCESS, "=======================================your Email update successfully...=====================================================");
                System.out.println(" ");
            }
        }
        return response;
    }

    private Response passwordUpdate(User user) {
        Response response = new Response(null, ResponseStatus.Error, "somethingwent wrong");
        System.out.println("Enter a password : ");
        String password = sc.next();
        user.setPassword(password);
        try {
            if (Objects.nonNull(userDao.add(user))) {
                response = new Response(user, ResponseStatus.SUCCESS, "======================================================Successfully Updated Password=====================================");
                System.out.println(" ");
            }
        } catch (Exception e) {
            log.error("========================================================Something went wrong during update password===========================================", e);
        }
        return response;
    }

    @Override
    public Response addUserByAdmin(User user){
        Response response = new Response(null, ResponseStatus.Error, "=================================================================user already present===========================================");
        try {
            Optional<User> isUserPresent = userDao.checkUserPrentOrNot(user.getEmail());
            if(isUserPresent.isEmpty()){
                userDao.add(user);
                response = new Response(user, ResponseStatus.SUCCESS, "=================================================================user add successfully===========================================");
                System.out.println(" ");
            }
        } catch (Exception e) {
          log.error("Something went wrong during admin add",e);
        }
        return response;
    }

    @Override
    public Response removeAdmin(String email) {
        Response response = new Response(null, ResponseStatus.Error, "user is not admin");
        try {
            if(Objects.nonNull(userDao.removeAdmin(email))){
                response = new Response(null, ResponseStatus.SUCCESS, "=================================================================user removed successfully===========================================");
            }
            System.out.println(" ");
        } catch (Exception e) {
            log.error("Something went wrong during admin add",e);
        }
        return response;
    }

    @Override
    public void getUser() {
        try {
            userDao.getUser().forEach(System.out::println);
        } catch (Exception e) {
            log.error("Something went wrong...");
            System.out.println("something went wrong");
        }
    }
}
