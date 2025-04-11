package repository.dao;

import model.User;

public interface AuthenticationDao {
     boolean userRegistration(User user) throws Exception;
     User userLogin(String email,String password) throws Exception;
}
