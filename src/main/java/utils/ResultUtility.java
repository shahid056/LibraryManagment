package utils;

import enums.BookCategory;
import enums.Role;
import enums.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.Book;
import model.BookBorrowed;
import model.SerialNumber;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultUtility {

    public static Optional<List<User>> getUserFromResultSet(ResultSet resultSet, boolean isOnlyUserObject) throws SQLException {
        List<User> userList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        while (resultSet.next()) {
            userList.add(User.builder().id(resultSet.getString("user_id")).
                    name(resultSet.getString("user_name")).
                    email(resultSet.getString("email")).password(resultSet.getString("password")).
                    role(Role.valueOf(resultSet.getString("role"))).totalBookBorrowed(resultSet.getInt("total_book_borrowed")).
                    dateOfJoining(LocalDate.parse(resultSet.getString("date_of_join"), formatter)).
                    build());
        }
       return Optional.of(userList);
    }

    public static Optional<List<Book>> getBookFromResultSet(ResultSet resultSet) throws SQLException {
        List<Book> bookList = new ArrayList<>();
        while (resultSet.next()) {
            bookList.add(Book.builder().bookId(resultSet.getString("book_id")).
                    name(resultSet.getString("name")).
                    author(resultSet.getString("author")).edition(resultSet.getInt("edition")).category(BookCategory.valueOf(resultSet.getString("category"))).
                    totalNumberOfCopy(resultSet.getInt("total_number_of_copy")).numberOfCopyAvailable(resultSet.getInt("number_of_available_copy")).
                    build());
        }
        return Optional.of(bookList);
    }

    public static Optional<List<BookBorrowed>> getBorrowedBookFromResultSet(ResultSet resultSet) throws SQLException {
        List<BookBorrowed> borrowedList = new ArrayList<>();
        while (resultSet.next()) {
            borrowedList.add(BookBorrowed.builder().fine(resultSet.getInt("late_fine"))
                    .status((resultSet.getBoolean("borrowed_status") ? Status.Borrowed : Status.Returned)).borrowDate(resultSet.getDate("date_of_issue").toLocalDate())
                    .returnDate(resultSet.getDate("date_of_return").toLocalDate()).bookSerialNumber(resultSet.getString("book_serial_number")).
                    userId(String.valueOf(resultSet.getInt("user_id"))).
                    bookId(String.valueOf(resultSet.getInt("book_id"))).build());
        }
       return Optional.of(borrowedList);
    }
}
