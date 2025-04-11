package serviceImpl;

import model.Book;
import model.User;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import repository.dao.UserDao;
import service.UserService;
import utils.Response;

import java.util.*;

@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Response addUser(User user) {
        Response response;
        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
            User userRes = userDao.add(user);
            if (Objects.nonNull(userRes)) {
                response = Response.builder().responseObject(userRes).message("user added Successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().responseObject(null).message("Something went wrong try again..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("Something went wrong try again..").statusCode(ResponseStatus.Error).build();
            log.error("something went wrong at user add ", e);
        }
        return response;
    }

    @Override
    public Response updateUser(User user,String columnName) {
        Response response;
        try {
            if(columnName.equalsIgnoreCase("password")) user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
            if (Objects.nonNull(userDao.updateUser(user,columnName))) {
                response = Response.builder().responseObject(user).message("user update successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().responseObject(user).message("user not found...").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            log.error("Something went wrong  while inserting user..", e);
            response = Response.builder().responseObject(user).message("something went wrong while inserting user..").statusCode(ResponseStatus.SUCCESS).build();
        }
        return response;
    }

    @Override
    public Response fetchUser() {
        Response response;
        try {
            Optional<List<User>> user = userDao.fetchUser();
            if (user.isPresent()) {
                response = Response.builder().responseObject(user.get()).message("user fetch successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().responseObject(null).message("user not found...").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching user", e);
        }
        return response;
    }

    @Override
    public Response fetchUserByEmail(String email) {
        Response response;
        try {
            Optional<User> user = userDao.fetchUserByEmail(email);
            if (user.isPresent()) {
                response = Response.builder().responseObject(user.get()).message("user fetch successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().responseObject(null).message("user not found..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching user", e);
        }
        return response;
    }

    @Override
    public Response findUserById(String id) {
        Response response;
        try {
            Optional<User> user = userDao.findUserById(id);
            if (user.isPresent()) {
                response = Response.builder().responseObject(user.get()).message("user find successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().message("user not found..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching user", e);
        }
        return response;
    }

    @Override
    public Response checkUserPrentOrNot(String email) {
        Response response;
        try {
            Optional<User> user = userDao.fetchUserByEmail(email);
            if (user.isPresent()) {
                response = Response.builder().responseObject(user.get()).message("user find successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().responseObject(null).message("user not found..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching user", e);
        }
        return response;
    }

    @Override
    public Response removeUser(User user) {
        Response response;
        try {
            User userRes = userDao.removeAdmin(user.getEmail());
            if (Objects.nonNull(userRes)) {
                response = Response.builder().responseObject(userRes).message("user delete successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().message("user not found..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching user", e);
        }
        return response;
    }

    @Override
    public Response userBorrowedBook(User user) {
        Response response;
        try {
            List<Book> userRes = userDao.userBorrowedBook(user.getId());
            if (userRes.isEmpty()) {
                response = Response.builder().responseObject(null).message("no book found").statusCode(ResponseStatus.Error).build();
            } else {
                response = Response.builder().responseObject(userRes).message("Borrowed book fetch successful..").statusCode(ResponseStatus.SUCCESS).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching borrowed book", e);
        }
        return response;
    }
}