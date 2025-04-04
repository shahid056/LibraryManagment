package entity;


import enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BookBorrowed {
    private String borrowId;
    private Book book;
    private User user;
    private String bookId;
    private String userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Status status = Status.Borrowed;
    private double fine =0.0;
    private String bookSerialNumber;
}
