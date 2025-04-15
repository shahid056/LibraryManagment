package repository.daoimpl;

import enums.BookCategory;
import enums.Status;
import model.Book;
import model.BookBorrowed;
import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.UserDao;
import utils.ResultUtility;

import java.sql.Date;
import java.sql.*;
import java.util.*;

import static utils.ResultUtility.getBorrowedBookFromResultSet;

public class BorrowDaoImpl implements BorrowDao {
    private final Connection connection;

    public BorrowDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addBorrowedBook(Book book, BookBorrowed bookBorrowed) throws Exception {
        String insetQuery = "insert into borrowed_book (late_fine,borrowed_status,book_serial_number,date_of_return,book_id,user_id) values (?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insetQuery)) {
            preparedStatement.setDouble(1, bookBorrowed.getFine());
            preparedStatement.setBoolean(2, bookBorrowed.getStatus().toString().equalsIgnoreCase("borrowed"));
            preparedStatement.setString(3, bookBorrowed.getBookSerialNumber());
            preparedStatement.setDate(4, Date.valueOf(bookBorrowed.getReturnDate()));
            preparedStatement.setInt(5, Integer.parseInt(book.getBookId()));
            preparedStatement.setInt(6, Integer.parseInt(bookBorrowed.getUserId()));
            int isInserted = preparedStatement.executeUpdate();
            return isInserted > 0;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<BookBorrowed> getBorrowBook() throws SQLException {
        String getQuery = "select * from borrowed_book";
        try (Statement statement = connection.createStatement();
             ResultSet borrowedData = statement.executeQuery(getQuery);
        ) {
            return getBorrowedBookFromResultSet(borrowedData).orElse(new ArrayList<>());
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<BookBorrowed> returnBook(BookBorrowed bookBorrowed) throws Exception {
        String returnQuery = "UPDATE borrowed_book SET borrowed_status = ? WHERE book_serial_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(returnQuery)) {
            preparedStatement.setBoolean(1, false);
            System.out.println(bookBorrowed.getBookSerialNumber());
            preparedStatement.setString(2, bookBorrowed.getBookSerialNumber());
            int i = preparedStatement.executeUpdate();
            System.out.println(i);
            return i > 0 ? Optional.of(bookBorrowed) : Optional.empty();

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Optional<BookBorrowed> fetchBorrowedUserIdBookId(String userId, String bookId) throws SQLException {
        String getQuery = "select * from borrowed_book where book_id = ? and user_id =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery)) {
            preparedStatement.setInt(1, Integer.parseInt(bookId));
            preparedStatement.setInt(2, Integer.parseInt(userId));
            ResultSet borrowedData = preparedStatement.executeQuery();
            return getBorrowedBookFromResultSet(borrowedData).map(List::getFirst);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<BookBorrowed> fetchBorrowedUserId(String userId) throws SQLException {

        String getQuery = "select * from borrowed_book a inner join book b on b.book_id=a.book_id where a.user_id = ?;";
        List<BookBorrowed> borrowedList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery)) {
            preparedStatement.setInt(1, Integer.parseInt(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                borrowedList.add(BookBorrowed.builder().fine(resultSet.getInt("late_fine")).book(setBook(resultSet)).borrowId("borrowed_book_id")
                        .status((resultSet.getBoolean("borrowed_status") ? Status.Borrowed : Status.Returned)).borrowDate(resultSet.getDate("date_of_issue").toLocalDate())
                        .returnDate(resultSet.getDate("date_of_return").toLocalDate()).bookSerialNumber(resultSet.getString("book_serial_number")).
                        userId(String.valueOf(resultSet.getInt("user_id"))).
                        bookId(String.valueOf(resultSet.getInt("book_id"))).build());
            }
            return borrowedList;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private Book setBook(ResultSet resultSet) throws SQLException {
      return   Book.builder().bookId(resultSet.getString("book_id")).
                name(resultSet.getString("name")).
                author(resultSet.getString("author")).edition(resultSet.getInt("edition")).category(BookCategory.valueOf(resultSet.getString("category"))).
                totalNumberOfCopy(resultSet.getInt("total_number_of_copy")).numberOfCopyAvailable(resultSet.getInt("number_of_available_copy")).
                build();
    }

}



