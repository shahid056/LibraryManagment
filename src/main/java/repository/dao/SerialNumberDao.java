package repository.dao;

import model.Book;
import model.SerialNumber;

import java.sql.SQLException;

public interface SerialNumberDao {

   boolean updateSerialNumber(SerialNumber SerialNumber , boolean isBorrowedBook) throws SQLException;

}
