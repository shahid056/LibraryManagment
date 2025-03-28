package entity;


import enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookBorrowed {
    private String borrowId;
    private Book book;
    private User user;
    private String bookId;
    private String userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Status status;
    private double fine;
    private String bookSerialNumber;


    public BookBorrowed( String bookId, String userId, LocalDate borrowDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(8);
        this.returnDate = borrowDate.plusDays(9);
        this.status = Status.Borrowed;
        this.fine = 0.0;
    }

    @Override
    public String toString() {
        return String.format(" | %-10s | %-16s | %-16s | %-16s | %-16s | %-16s |%n", user,borrowDate,dueDate ,returnDate,fine,status);
    }
}
