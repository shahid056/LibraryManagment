package model;

import enums.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Book {
    private String bookId;
    private String name;
    private String author;
    private BookCategory category;
    @Builder.Default
    private int edition = 0;
    private int totalNumberOfCopy;
    private int numberOfCopyAvailable;


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
