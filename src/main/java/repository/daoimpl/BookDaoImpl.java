package repository.daoimpl;

import enums.BookCategory;
import model.Book;
import model.User;
import utils.ConnectionDb;
import utils.RandomID;
import utils.ResultUtility;

import java.sql.*;
import java.util.*;

public class BookDaoImpl implements repository.dao.BookDao {

    private final Connection connection;

    public BookDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private final List<Book> bookDb = new ArrayList<>();

    @Override
    public Optional<Book> addBook(Book book) throws Exception {

        String insetQuery = "insert into book (name,author,category,number_of_copy,total_number_of_copy) values (?,?,?::book_category,?,?) RETURNING book_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insetQuery)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getCategory().name());
            preparedStatement.setInt(4, book.getTotalNumberOfCopy());
            preparedStatement.setInt(5, book.getTotalNumberOfCopy());
            ResultSet resultSet = preparedStatement.executeQuery();
            return ResultUtility.getBookFromResultSet(resultSet).map(List::getFirst);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public Optional<Book> isBookIsPresent(Book book) throws Exception {
        try (Statement statement = connection.createStatement()) {
            if (bookDb.contains(book)) {
                return getBookByNameAuthor(book.getName(), book.getAuthor());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteBook(Book book) throws Exception {
        if (Objects.isNull(book)) {
            return false;
        }
        try {
            return bookDb.remove(book);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Optional<Book> updateBook(Book book, String columnName) throws Exception {
        List<String> column = List.of("name", "author", "category", "number_of_available_copy");
        if (column.stream().noneMatch(isColumnPresent -> isColumnPresent.equalsIgnoreCase(columnName))) {
            throw new Exception("Column not match");
        }
        String insertSql = null;
        if (columnName.equalsIgnoreCase("category")) {
            insertSql = "UPDATE book SET " + columnName + " = ?::book_category WHERE book_id = ?";
        } else if (columnName.equalsIgnoreCase("number_of_available_copy")) {
            insertSql = "UPDATE book SET total_number_of_copy = ?, number_of_available_copy = ? WHERE book_id = ?";
        } else {
            insertSql = "UPDATE book SET " + columnName + " = ? WHERE book_id = ?";
        }

        try (PreparedStatement updateUser = connection.prepareStatement(insertSql)) {
            if (columnName.equalsIgnoreCase("name")) {
                updateUser.setString(1, book.getName());
                updateUser.setInt(2, Integer.parseInt(book.getBookId()));
            }
            if (columnName.equalsIgnoreCase("author")) {
                updateUser.setString(1, book.getAuthor());
                updateUser.setInt(2, Integer.parseInt(book.getBookId()));
            }
            if (columnName.equalsIgnoreCase("category")) {
                updateUser.setString(1, book.getCategory().name());
                updateUser.setInt(2, Integer.parseInt(book.getBookId()));
            }
            if (columnName.equalsIgnoreCase("number_of_available_copy")) {
                updateUser.setInt(1, book.getTotalNumberOfCopy());
                updateUser.setInt(2, book.getNumberOfCopyAvailable());
                updateUser.setInt(3, Integer.parseInt(book.getBookId()));
            }
            return updateUser.executeUpdate() > 0 ? Optional.of(book) : Optional.empty();

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<List<Book>> getBooks() {
        String fetchQuery = "select DISTINCT on (book.book_id) *  from book inner join serial_number " +
                "on book.book_id = serial_number.book_id;";
        try (Statement statement = connection.createStatement();
             ResultSet bookData = statement.executeQuery(fetchQuery);
        ) {
            return (Optional<List<Book>>) ResultUtility.getBookFromResultSet(bookData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> getBooksById(String bookId) throws Exception {
        String fetchQuery = "select * from book where book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(fetchQuery);
        ) {
            statement.setInt(1, Integer.parseInt(bookId));
            ResultSet resultSet = statement.executeQuery();
            return ResultUtility.getBookFromResultSet(resultSet).map(List::getFirst);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private Optional<Book> getBookByNameAuthor(String bookName, String authorName) {
        return bookDb.stream().filter(book -> book.getName().equalsIgnoreCase(bookName) && book.getAuthor().equalsIgnoreCase(authorName)).findFirst();
    }
}
