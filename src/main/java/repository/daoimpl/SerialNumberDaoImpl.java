package repository.daoimpl;

import model.SerialNumber;
import repository.dao.SerialNumberDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SerialNumberDaoImpl implements SerialNumberDao {

    private final Connection connection;

    public SerialNumberDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean updateSerialNumber(SerialNumber serialNumber, boolean isBorrowedBook) throws SQLException {
        String insetQuery = "update serial_number set isborrowed = ? where serial_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insetQuery)) {
            preparedStatement.setBoolean(1, isBorrowedBook);
            preparedStatement.setInt(2, serialNumber.getSerialNumber());
            int isInserted = preparedStatement.executeUpdate();
            return isInserted > 0;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
