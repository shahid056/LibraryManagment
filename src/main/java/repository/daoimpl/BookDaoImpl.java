package repository.daoimpl;

import entity.Book;
import utils.RandomID;

import java.util.*;

public class BookDaoImpl implements repository.dao.BookDao {

    private final List<Book> bookDb = new ArrayList<>();

    @Override
    public Optional<Book> addBook(Book book) throws Exception {
        Optional<Book> bookData ;
        try {
            if (Objects.isNull(book)) {
                return Optional.empty();
            }
            else {
                book.setBookId(new RandomID().srno());
                bookDb.add(book);
                bookData = Optional.of(book);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return bookData;
    }

    @Override
    public Optional<Book> isBookIsPresent(Book book) throws Exception{
          try{
            if(bookDb.contains(book)){
                return getBookByNameAuthor(book.getName(), book.getAuthor());
            }
          } catch (Exception e) {
              throw new Exception(e);
          }
          return Optional.empty();
    }

    @Override
    public Optional<Book> findBookById(String id){
        return bookDb.stream().filter(book -> book.getBookId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public boolean deleteBook(Book book) throws Exception {
        if (Objects.isNull(book)) {
            return false;
        }
        try{
            return bookDb.remove(book);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<Book> getBooks() {
        return bookDb;
    }


    private Optional<Book> getBookByNameAuthor(String bookName,String authorName){
        return bookDb.stream().filter(book->book.getName().equalsIgnoreCase(bookName) && book.getAuthor().equalsIgnoreCase(authorName)).findFirst();
    }
}
