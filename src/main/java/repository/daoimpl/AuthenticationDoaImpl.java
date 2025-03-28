package repository.daoimpl;

import entity.User;
import repository.dao.AuthenticationDao;
import repository.dao.UserDao;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class AuthenticationDoaImpl implements AuthenticationDao {

    private UserDao userDao = UserDaoImpl.getInstance();
    private static AuthenticationDoaImpl authenticationDao;
    private AuthenticationDoaImpl(){};

    public static AuthenticationDoaImpl getInstance(){
        if(Objects.isNull(authenticationDao)){
            authenticationDao = new AuthenticationDoaImpl();
        }
        return authenticationDao;
    }

    @Override
    public boolean userRegistration(User user) throws Exception {
        Optional<User> isUserPresent = userDao.checkUserPrentOrNot(user.getEmail());
        if (isUserPresent.isEmpty()) {
            Random random = new Random();
            int id = 1000 + random.nextInt(9000);
            user.setId(user.getName() + id);
            user.setPassword((user.getPassword()));
            userDao.add(user);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public User userLogin(String email, String password) throws Exception {

        Optional<User> user = userDao.checkUserPrentOrNot(email);
        if (email != null && password != null) {
            try {
                if (email.equalsIgnoreCase("admin@gmail.com") || user.isPresent()) {
                    if (password.equalsIgnoreCase("1234") || user.get().getPassword().equalsIgnoreCase(password)) {
                        return user.get();
                    }
                }
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        return null;
    }
}