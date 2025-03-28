import entity.Book;
import enums.BookCategory;
import servicesImpl.BookServicesImpl;

public class BookData {

    public static void populatedBookData(String name,String author,String category,int copy){
        BookServicesImpl bookServices = BookServicesImpl.getBookInstance();
        bookServices.addBook(new Book.BookBuilder().
                setName(name).
                setAuthor(author).
                setCategory(BookCategory.valueOf(category.toUpperCase())).
                setNumberOfCopy(copy).
                build());
    }
}
