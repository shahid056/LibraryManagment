package server;

import entity.User;
import utils.Response;

public interface AuthenticationServices {

    Response userRegistration(User user);

    Response userLogin(String email, String password);

}
