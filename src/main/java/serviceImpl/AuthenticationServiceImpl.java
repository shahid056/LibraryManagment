package serviceImpl;

import model.User;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import repository.dao.AuthenticationDao;
import service.AuthenticationService;
import service.UserService;
import utils.Response;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;

    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response userRegistration(User user) {
        Response response;
        try {
            Object userRes = userService.addUser(user).getResponseObject();
            if (Objects.nonNull(userRes)) {
                response = Response.builder().responseObject(user).message("User account created successful...").statusCode(ResponseStatus.SUCCESS).build();
            } else {
                response = Response.builder().responseObject(user).message("User already exist...").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            log.error("Registration error", e);
            response = Response.builder().message("Something went wrong during registration...").statusCode(ResponseStatus.Error).build();
        }
        return response;
    }

    @Override
    public Response userLogin(String email, String password) {
        Response response = new Response();
        try {
            Object userResponse = userService.checkUserPrentOrNot(email).getResponseObject();
            if (userResponse instanceof User user) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    response = Response.builder().responseObject(user).message(user.getName() + " Welcome Back").statusCode(ResponseStatus.SUCCESS).build();
                }else {
                    response = Response.builder().responseObject(null).message("invalid credential").statusCode(ResponseStatus.Error).build();
                }
            } else {
                response = Response.builder().responseObject(null).message("invalid credential").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("Something went wrong during login...").statusCode(ResponseStatus.Error).build();
            log.error("Login error", e);
        }
        return response;
    }
}
