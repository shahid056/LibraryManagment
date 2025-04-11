package repository.dao;

import model.Book;
import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    User add(User user) throws Exception;

    User updateUser(User user,String columnName) throws Exception;

    Optional<List<User>> fetchUser() throws Exception;

    Optional<User> findUserById(String id) throws SQLException;

    Optional<User> fetchUserByEmail(String email) throws SQLException;

    User removeAdmin(String email) throws Exception;

    List<Book> userBorrowedBook(String userId) throws Exception;

}
