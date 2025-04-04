package serviceImpl;

import entity.Book;
import entity.User;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import repository.dao.UserDao;
import services.UserService;
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
            Optional<User> isUserPresent = userDao.checkUserPrentOrNot(user.getEmail());
            if (isUserPresent.isEmpty()) {
                Random random = new Random();
                int id = 1000 + random.nextInt(9000);
                user.setId(user.getName() + id);
                user.setPassword((user.getPassword()));
            }
            if (Objects.nonNull(userDao.add(user))) {
                response = Response.builder().responseObject(user).message("user added Successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().message("Something went wrong try agiain..").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("Something went wrong try agiain..").statusCode(ResponseStatus.Error).build();
            log.error("something went wrong at user add ", e);
        }
        return response;
    }

    @Override
    public Response updateUser(User user) {
        Response response;
        try {
            if (Objects.nonNull(userDao.add(user))) {
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
            List<User> user = userDao.getUser();
            if (Objects.nonNull(user)) {
                response = Response.builder().responseObject(user).message("user fetch successful..").statusCode(ResponseStatus.SUCCESS).build();
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
            Optional<User> user = userDao.checkUserPrentOrNot(email);
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
            List<Book> userRes = userDao.userBorrowedBook(user.getEmail());
            if (Objects.nonNull(userRes)) {
                response = Response.builder().responseObject(userRes).message("Borrowed book fetch successful..").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().message("no book found").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong while inserting user..").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during fetching borrowed book", e);
        }
        return response;
    }
}