package service;

import model.User;
import utils.Response;

public interface AdminService {
    Response addUserByAdmin(User user);

    Response removeAdmin(String email);

    Response fetchUser();
}
