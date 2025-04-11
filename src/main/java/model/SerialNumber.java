package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SerialNumber {
    private int serialNumber;
    private int book_id;
    private boolean isBorrowed;
}
