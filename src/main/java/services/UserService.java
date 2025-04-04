package services;

import entity.User;
import utils.Response;

public interface UserService {

    Response addUser(User user);

    Response updateUser(User user);

    Response fetchUser();

    Response fetchUserByEmail(String email);

    Response findUserById(String id);

    Response checkUserPrentOrNot(String email);

    Response removeUser(User user);

    Response userBorrowedBook(User user);
}
