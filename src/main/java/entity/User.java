package entity;

import enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private List<Book> borrowedBooks;
    LocalDate dateOfJoin;

    public User(UserBuilder userBuilder) {
        this.name = userBuilder.name;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.role = userBuilder.role;
        dateOfJoin = LocalDate.now();
        borrowedBooks = new LinkedList<>();
    }

    public synchronized boolean removeBorrowedBook(String id) {
        return borrowedBooks.removeIf(book -> book.getBookId().equalsIgnoreCase(id));
    }

    public boolean setBorrowedBooksById(Book book) {
        return borrowedBooks.add(book);
    }

    public static class UserBuilder {
        private String userId;
        private String name;
        private String email;
        private String password;
        private Role role;

        public UserBuilder() {
        }

        public UserBuilder setUserId(String userId){
            this.userId = userId;
            return this;
        }
        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setRole(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }


    @Override
    public String toString() {
        StringBuilder table = new StringBuilder();

        table.append(String.format("| %-20s | %-30s | %-15s | %-10s | %-10s  |\n",
                "Name", "Email", "Role",(this.getRole().toString().equalsIgnoreCase("user")?"Books":" "), "Date of Joining"));

        table.append("|------------|----------------------|------------------------------|-----------------|------------|-----------\n");

        table.append(String.format("| %-20s | %-30s | %-15s | %-10s | %-10s  |\n",
                name, email, role != null ? role.toString() : "N/A",
                (this.getRole().toString().equalsIgnoreCase("user")?borrowedBooks != null ? borrowedBooks.size() : 0:" "),
                dateOfJoin != null ? dateOfJoin.toString() : "N/A"));

        return table.toString();
    }
}
