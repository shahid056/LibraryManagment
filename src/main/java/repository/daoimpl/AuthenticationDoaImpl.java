package repository.daoimpl;

import entity.User;
import repository.dao.AuthenticationDao;
import repository.dao.UserDao;

import java.util.Objects;
import java.util.Optional;


public class AuthenticationDoaImpl implements AuthenticationDao {

    private final UserDao userDao;

    public AuthenticationDoaImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    //no use
    @Override
    public boolean userRegistration(User user) throws Exception {
        return Objects.isNull(userDao.add(user));
    }


    @Override
    public User userLogin(String email, String password) throws Exception {

        Optional<User> user = userDao.checkUserPrentOrNot(email);
        if (email != null && password != null) {
            try {
                 if(user.isPresent()){
                    return user.get();
                }
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        return null;
    }
}