package repository.daoimpl;

import entity.Book;
import utils.RandomID;

import java.util.*;

public class BookDaoImpl implements repository.dao.BookDao {

    private final List<Book> bookDb = new ArrayList<>();
    private static BookDaoImpl bookInstance;

    private BookDaoImpl(){};

    public static BookDaoImpl getBookInstance(){
        if(Objects.isNull(bookInstance)){
          bookInstance = new BookDaoImpl();
        }
        return bookInstance;
    }

    @Override
    public Book addBook(Book book) throws Exception {
        Book bookData = null;
        try {
            if (Objects.isNull(book)) {
                return bookData;
            } else if (!bookDb.contains(book)) {
                book.setBookId(new RandomID().srno());
                bookDb.add(book);
                bookData = book;
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
        return bookData;
    }

    @Override
    public Optional<Book> findBookById(String id){
        return bookDb.stream().filter(book -> book.getBookId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public boolean deleteBook(String srNo, int noOfCopyDelete) throws Exception {
        if (Objects.isNull(srNo)) {
            return false;
        }
        try{
            bookDb.stream().map(Book::getSrNo).map(srno->srno.remove(srNo));
        } catch (Exception e) {
            throw new Exception(e);
        }
        return true;
    }

    @Override
    public List<Book> getBooks() {
        return bookDb;
    }
}
