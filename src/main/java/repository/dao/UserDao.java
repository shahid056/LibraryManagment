package repository.dao;

import entity.Book;
import entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User add(User user) throws Exception;

    List<User> getUser() throws Exception;

    Optional<User> findUserById(String id);

    Optional<User> checkUserPrentOrNot(String email);

    Optional<User> fetchUserByEmail(String email) throws Exception;

    User removeAdmin(String email) throws Exception;

    List<Book> userBorrowedBook(String email) throws Exception;

}
