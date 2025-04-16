import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.UserDao;
import repository.daoimpl.BookDaoImpl;
import repository.daoimpl.BorrowDaoImpl;
import repository.daoimpl.UserDaoImpl;
import service.*;
import serviceImpl.*;
import userInterface.Home;
import userInterface.common.UpdateUser;
import utils.ConnectionDb;

import java.sql.Connection;
import java.util.Objects;


public class Main {


    public static void main(String[] args) {
        new Home().homeScreen();
    }
}