package serviceImpl;

import entity.User;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import repository.dao.AuthenticationDao;
import services.AuthenticationService;
import services.UserService;
import utils.Response;

import java.util.Objects;

@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationDao authenticationDoa;
    private final UserService userService;

    public AuthenticationServiceImpl(AuthenticationDao authenticationDoa, UserService userService) {
        this.authenticationDoa = authenticationDoa;
        this.userService = userService;
    }

    @Override
    public Response userRegistration(User user) {
        Response response ;
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
    public Response userLogin(String email,String password) {
        Response response ;
        try {
            User user = authenticationDoa.userLogin(email, password);
            if(Objects.nonNull(user)) {
              response =  Response.builder().responseObject(user).message("Login successful...").statusCode(ResponseStatus.SUCCESS).build();
            }else {
                response =  Response.builder().message("User not found...").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("Something went wrong during login...").statusCode(ResponseStatus.Error).build();
           log.error("Login error",e);
        }
        return response;
    }
}
