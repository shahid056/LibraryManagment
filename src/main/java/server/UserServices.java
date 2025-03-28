package server;

import entity.User;
import utils.Response;

public interface UserServices {

    void updateUser(User user);

    void fetchUser();

    Response addUserByAdmin(User user);

    Response removeAdmin(String email);
}
