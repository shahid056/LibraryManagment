package repository.daoimpl;

import enums.Role;
import entity.User;
import repository.dao.UserDao;

import java.util.*;

public class UserDaoImpl implements UserDao {

    private Map<String, User> usersDb =new HashMap<>();
    private static final String adminEmail = "admin@gmail.com";
    private static final String adminPassword = "1234";
    private static UserDaoImpl userDao;


    private UserDaoImpl(){
        userDBinitialize();
    }

    public static UserDaoImpl getInstance(){
        if(Objects.isNull(userDao)){
            userDao=new UserDaoImpl();
        }
        return userDao;
    }

    @Override
    public User add(User user) {
      if(Objects.isNull(usersDb)){
          userDBinitialize();
      }
        return  usersDb.put(user.getEmail(),user);
    }

    private void userDBinitialize(){
        User adminUser = new User.UserBuilder().setName("supderAdmin").setEmail(adminEmail).setPassword(adminPassword).setRole(Role.superadmin).build();;
        adminUser.setId("Super01");
        usersDb.put(adminEmail, adminUser);
    }

    @Override
    public Optional<User> findUserById(String id){
        return usersDb.values().stream().filter(data->data.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public Optional<User> checkUserPrentOrNot(String email) {
        return usersDb.values().stream().filter(data->data.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public User removeAdmin(String email) throws Exception {
        try {
            User user = usersDb.get(email);
            if(user.getRole().toString().equalsIgnoreCase(Role.admin.toString())){
                return usersDb.remove(email);
            }
            return null;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public List<User> getUser() throws Exception {
        try {
           return new ArrayList<>(usersDb.values());
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}
