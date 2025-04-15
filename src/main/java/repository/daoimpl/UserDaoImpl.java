package repository.daoimpl;

import model.Book;
import enums.Role;
import model.User;
import repository.dao.UserDao;
import utils.ResultUtility;

import java.sql.*;
import java.util.*;


public class UserDaoImpl implements UserDao {

    private Map<String, User> usersDb = new HashMap<>();


    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public User add(User user) throws SQLException {
        String insertSql = "insert into users(user_name,email,password,role) values(?,?,?,?)";
        try (PreparedStatement insertUser = connection.prepareStatement(insertSql)) {
            insertUser.setString(1, user.getName());
            insertUser.setString(2, user.getEmail());
            insertUser.setString(3, user.getPassword());
            insertUser.setString(4, user.getRole().toString());
            insertUser.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return user;
    }

    @Override
    public User updateUser(User user,String columnName) throws Exception {
        List<String> column = List.of("user_name","email","password");
        if(column.stream().noneMatch(isColumnPresent-> isColumnPresent.equalsIgnoreCase(columnName))){
            throw new Exception("Column not match");
        }
        String insertSql = "update users set "+columnName+" = ? where user_id = ?";
        try (PreparedStatement updateUser = connection.prepareStatement(insertSql)) {
           if(columnName.equalsIgnoreCase("user_name")) updateUser.setString(1, user.getName());
           if(columnName.equalsIgnoreCase("email")) updateUser.setString(1, user.getEmail());
           if(columnName.equalsIgnoreCase("password")) updateUser.setString(1, user.getPassword());
           updateUser.setInt(2, Integer.parseInt(user.getId()));
          return updateUser.executeUpdate() > 0 ? user : null;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<User> findUserById(String id) throws SQLException {
        String insertSql = "select * from users where user_id = ?";
        try (PreparedStatement findUserQuery = connection.prepareStatement(insertSql)) {
            findUserQuery.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = findUserQuery.executeQuery();
         return  ResultUtility.getUserFromResultSet(resultSet,true).map(List::getFirst);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<User> fetchUserByEmail(String email) throws SQLException {
        String fetchQuery = "select * from users where email = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery)){
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return ResultUtility.getUserFromResultSet(resultSet, true).map(List::getFirst);
        }catch (SQLException e){
            throw new SQLException(e);
        }
    }

    @Override
    public boolean removeAdmin(String email) throws Exception {
        String deleteUser = "delete from users where email = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(deleteUser)){
            preparedStatement.setString(1,email);
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e){
            throw new SQLException(e);
        }
    }

    @Override
    public List<Book> userBorrowedBook(String userId) throws Exception {
        String fetchQuery = "select * from book as a" +" inner join borrowed_book b on a.book_id = b.book_id " +"where b.borrowed_status = true and b.user_id = ?; ";
        try(PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery)){
            preparedStatement.setInt(1,Integer.parseInt(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            return  ResultUtility.getBookFromResultSet(resultSet).orElse(new ArrayList<>());
        }catch (SQLException e){
            throw new SQLException(e);
        }
    }


    @Override
    public Optional<List<User>> fetchUser() throws Exception {
        String fetchQuery = "select * from users";
        try (Statement statement = connection.createStatement();
             ResultSet userData = statement.executeQuery(fetchQuery);
        ){
            return ResultUtility.getUserFromResultSet(userData, false);
        }catch (SQLException e){
            throw new SQLException(e);
        }
    }
}
