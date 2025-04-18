package service;

import model.User;
import utils.Response;

public interface AuthenticationService {

    Response userRegistration(User user);

    Response userLogin(String email, String password);

}
