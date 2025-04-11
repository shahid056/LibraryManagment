package model;

import enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private Role role;
    @Builder.Default
    private int totalBookBorrowed = 0;
    private List<Book> borrowedBooks = new ArrayList<>();
    LocalDate dateOfJoining;

    @Override
    public String toString() {
        StringBuilder table = new StringBuilder();
        table.append(String.format("| %-20s | %-30s | %-15s | %-10s | %-10s  |\n",
                "Name", "Email", "Role",(this.getRole().toString().equalsIgnoreCase("user")?"Books":" "), "Date of Joining"));
        table.append("================================================================================================================================\n");

        table.append(String.format("| %-20s | %-30s | %-15s | %-10s | %-10s  |\n",
                name, email, role != null ? role.toString() : "N/A",
                (this.getRole().toString().equalsIgnoreCase("user")?totalBookBorrowed:" "),
                dateOfJoining != null ? dateOfJoining.toString() : "N/A"));

        return table.toString();
    }
}
