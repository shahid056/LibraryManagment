package serviceImpl;

import entity.User;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import services.AdminService;
import services.UserService;
import utils.Response;

import java.util.Objects;

@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserService userService;

    public AdminServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response addUserByAdmin(User user) {
        Response response;
        try {
            Object userPresentOrNot = userService.checkUserPrentOrNot(user.getEmail()).getResponseObject();
            if (Objects.isNull(userPresentOrNot)) {
                response =  userService.addUser(user);
            } else {
                response = Response.builder().message("user already present...").statusCode(ResponseStatus.Error).build();
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong try again...").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong during admin add", e);
        }
        return response;
    }

    @Override
    public Response removeAdmin(String email) {
        Response response = new Response();
        try {
            Object user = userService.fetchUserByEmail(email).getResponseObject();
            if(Objects.isNull(user)){
                response = Response.builder().responseObject(null).message("user not found").statusCode(ResponseStatus.Error).build();
            }
            if (user instanceof User userData) {
               response = userService.removeUser(userData);
            }
        } catch (Exception e) {
            response = Response.builder().message("something went wrong try again...").statusCode(ResponseStatus.Error).build();
            log.error("something went wrong at remove Admin", e);
        }
        return response;
    }

    @Override
    public Response fetchUser() {
        Response response;
        try {
            response =  userService.fetchUser();
        } catch (Exception e) {
            response = Response.builder().message("Something went wrong try again...").statusCode(ResponseStatus.Error).build();
            log.error("Something went wrong...");
        }
        return response;
    }
}
