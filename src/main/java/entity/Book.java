package entity;

import enums.BookCategory;
import lombok.Getter;
import lombok.Setter;
import utils.RandomID;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Getter
@Setter
public class Book {
    private String bookId;
    private String name;
    private String author;
    private BookCategory category;
    private List<String> srNo = new ArrayList<>();
    private int numberOfCopy;

    private Book(BookBuilder bookBuilder) {
        this.name = bookBuilder.name;
        this.author = bookBuilder.author;
        this.category = bookBuilder.category;
        this.numberOfCopy = bookBuilder.numberOfCopy;
        copyId(numberOfCopy);
    }

    private void copyId(int numberOfCopy) {
        for (int i = 0; i < numberOfCopy; i++) {
            srNo.add(new RandomID().srno());
        }
    }

    public synchronized String getSerialNumber() throws NoSuchElementException {
        try {
            srNo.remove(srNo.getFirst());
        } catch (Exception e) {
            throw new NoSuchElementException(e);
        }
        return srNo.getFirst();
    }

    public List<String> getAllSerialNumber() {
        return srNo;
    }



    public static class BookBuilder {
        private String name;
        private String author;
        private BookCategory category;
        private int numberOfCopy;

        public BookBuilder() {
        }

        public BookBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder setCategory(BookCategory category) {
            this.category = category;
            return this;
        }

        public BookBuilder setNumberOfCopy(int numberOfCopy) {
            this.numberOfCopy = numberOfCopy;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Book book)) return false;
        return Objects.equals(name, book.name) && Objects.equals(author, book.author) && category == book.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, category);
    }
}
