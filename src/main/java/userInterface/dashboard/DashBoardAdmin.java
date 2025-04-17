package userInterface.dashboard;

import enums.BookCategory;
import enums.ResponseStatus;
import enums.Role;
import model.Book;
import model.BookBorrowed;
import model.User;
import service.AdminService;
import service.BookService;
import service.BorrowedBookService;
import service.UserService;
import userInterface.AbstractUi;
import userInterface.common.UpdateUser;
import utils.FieldValidator;
import utils.Response;
import utils.ValidatorRegxUtil;

import javax.management.openmbean.InvalidOpenTypeException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class DashBoardAdmin extends AbstractUi {
    static Scanner sc = new Scanner(System.in);


    private final UserService userService;
    private final BookService bookService;
    private final AdminService adminService;
    private final BorrowedBookService borrowedBookService;
    private final UpdateUser updateUser;

    public DashBoardAdmin(UserService userService, BookService bookService, AdminService adminService, BorrowedBookService borrowedBookService, UpdateUser updateUser) {
        this.userService = userService;
        this.bookService = bookService;
        this.adminService = adminService;
        this.borrowedBookService = borrowedBookService;
        this.updateUser = updateUser;
    }


    //admin
    public void adminScreen(User user) {
        boolean isExit = true;
        while (isExit) {
            System.out.println(" ");
            displayOption(List.of("*************Library (" + user.getRole() + ")***********************",
                    "Enter 1 for check profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Add Book",
                    "Enter 4 for remove Book",
                    "Enter 5 for transaction Details",
                    "Enter 6 for Display All Users",
                    "Enter 7 for Book update",
                    "Enter 0 for sign out")
            );
            if (user.getRole().toString().equalsIgnoreCase("superAdmin")) {
                displayOption(List.of(
                        "Enter 8 for add admin Users", "Enter 9 for delete admin Users"));
            }
            System.out.println(" ");
            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> profileView(user);
                case 2 -> displayBook();
                case 3 -> bookAdd();
                case 4 -> removeBook();
                case 5 -> displayUserRecord();
                case 6 -> displayUser(false);
                case 7 -> bookUpdate();
                case 8 -> isSuperAdmin(user, 8);
                case 9 -> isSuperAdmin(user, 9);
                case 0 -> {
                    isExit = false;
                }
                default -> System.err.println("invalid operation");
            }
        }
    }

    @Override
    public void userScreen(User user) {
    }

    private void isSuperAdmin(User user, int choice) {
        if (user.getRole().toString().equalsIgnoreCase("superAdmin")) {
            if (choice == 8) {
                addAdmin();
            } else if (choice == 9) {
                removeAdmin(user);
            }
        }
    }

    private void bookUpdate() {
        while (true) {
            try {
                Map<Integer, Book> bookMap = displayBook();
                System.out.println("Enter a  book id to update (for back to main menu enter -1: ");
                int bookId = sc.nextInt();
                Book book = bookMap.get(bookId);
                if (bookId == -1) return;
                if (bookId > -1) {
                    updateBookFields(book);
                } else {
                    System.err.println("enter a proper book id..");
                }
            } catch (Exception e) {
                System.err.println("Enter a proper input");
                sc.nextLine();
            }
        }
    }


    private void updateBookFields(Book book) {
        try {
            System.out.println(" ");
            System.out.println("Book you want to update is : " + book.getName());
            System.out.println(" ");
            AbstractUi.displayOption(List.of("What data you want to update :",
                    "Enter 1 for update book name : ",
                    "Enter 2 for update book author : ",
                    "Enter 3 for update Number of copy : ",
                    "Enter 4 for Back to Main Menu : "));
            int op = sc.nextInt();
            switch (op) {
                case 1 -> updateName(book);
                case 2 -> updateAuthor(book);
                case 3 -> updateCopy(book);
                case 4 -> {
                    return;
                }
                default -> System.err.println("Invalid operations...");
            }
        } catch (Exception e) {
            System.err.println("Enter a proper input :");
        }
    }

    private void updateCopy(Book book) {
        sc.nextLine();
        try {
            displayOption(List.of("To add more copy enter 1: ", "To remove  copy enter 2: ", "Enter 3 back  to menu :"));
            int input = sc.nextInt();
            switch (input) {
                case 1: {
                    System.out.println("Enter the number of book copy you want  to add : ");
                    int bookCount = sc.nextInt();
                    if (bookCount <= 0) {
                        System.out.println("Enter a copy should be greater then 0 :");
                        break;
                    }
                    book.setTotalNumberOfCopy(book.getTotalNumberOfCopy() + bookCount);
                    book.setNumberOfCopyAvailable(book.getNumberOfCopyAvailable() + bookCount);
                    System.out.println(bookService.updateBook(book, "number_of_available_copy").getMessage());
                    sc.nextLine();
                    displayBook();
                    return;
                }
                case 2: {
                    System.out.println("Enter the number of book copy you want  to remove : ");
                    int bookCount = sc.nextInt();
                    if (bookCount <= 0) {
                        System.out.println("Enter a copy should be greater then 0 :");
                        break;
                    } else if (book.getNumberOfCopyAvailable() > bookCount) {
                        book.setTotalNumberOfCopy(book.getTotalNumberOfCopy() - bookCount);
                        book.setNumberOfCopyAvailable(book.getNumberOfCopyAvailable() - bookCount);
                        System.out.println(bookService.updateBook(book, "number_of_available_copy").getMessage());
                        sc.nextLine();
                        displayBook();
                    } else {
                        System.out.println("Book are not available to remove/book are on rent");
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Enter a proper value ..");
        }
    }

    private void updateAuthor(Book book) {
        sc.nextLine();
        try {
            System.out.println("Enter  Author name of Book (Enter back to b to menu :): ");
            System.out.println();
            String bookAuthorName = sc.nextLine();
            if (bookAuthorName.equalsIgnoreCase("b")) {
                return;
            }
            book.setAuthor(bookAuthorName);
            displayBook();
        } catch (InputMismatchException e) {
            System.out.println("Enter a proper value ");
        }
        System.out.println(bookService.updateBook(book, "author").getMessage());
    }


    private void updateName(Book book) {
        sc.nextLine();
        try {
            System.out.println("Enter new name of Book (Enter b  to back to menu ) : ");
            String bookName = sc.nextLine();
            if (bookName.equalsIgnoreCase("b")) {
                return;
            }
            book.setName(bookName);
            displayBook();
        } catch (InvalidOpenTypeException e) {
            System.err.println("Enter a proper input");
        }
        System.out.println(bookService.updateBook(book, "name").getMessage());
    }

    private void profileView(User user) {
        System.out.println("======================================================Profile====================================================================");
        System.out.println(user);
        updateUser.updateUser(user);
    }

    private void displayUser(boolean isAdmin) {
        try {
            System.out.println(" ");
            System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
            System.out.println(String.format("|%-10s |%-20s | %-30s | %-15s | %-10s | %-10s |%n", "id", "Name", "Email", "Role", (isAdmin) ? " " : "Book", "Date of Joining"));
            System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
            Object response = adminService.fetchUser().getResponseObject();
            if (response instanceof List<?> user) {
                List<User> userList = (List<User>) user;
                AtomicInteger index = new AtomicInteger();
                if (!user.isEmpty()) {
                    for (User userData : userList) {
                        if (isAdmin && userData.getRole().toString().equalsIgnoreCase(Role.user.toString())) {
                            continue;
                        }
                        String bookInfo = String.format("|%-10s |%-20s | %-30s | %-15s | %-10s | %-10s  |%n",
                                index.getAndIncrement() + 1, userData.getName(), userData.getEmail(), userData.getRole(), (userData.getRole() == Role.user) ?
                                        (Objects.isNull(userData.getBorrowedBooks()) ? "0" : userData.getBorrowedBooks().size()) : " ", userData.getDateOfJoining());
                        System.out.println(bookInfo);
                    }
                } else {
                    System.out.println("No user Found");
                }
            }
        } catch (Exception e) {
            System.out.println("something went wrong..");
        }
    }

    private void addAdmin() {
        try {
            while (true) {
                System.out.println("Enter a Email of user (enter b for back to main menu):");
                String email = sc.nextLine().trim().toLowerCase();
                if (email.equalsIgnoreCase("b")) return;
                if (!ValidatorRegxUtil.isEmailValid(email)) {
                    System.err.println("Enter a valid email...");
                    continue;
                }
                System.out.println("Enter a name of user (enter b for back to main menu):");
                String name = sc.nextLine().trim();
                if (name.equalsIgnoreCase("b")) return;
                System.out.println("set a password  of user (enter b for back to main menu):");
                String password = sc.nextLine().trim();
                if (password.equalsIgnoreCase("b")) return;
                if (email.isBlank() && name.isBlank() && password.isBlank()) {
                    System.err.println("Field should not be empty...");
                } else {
                    User user = User.builder().name(name).email(email).password(password).role(Role.admin).build();
                    Response response = adminService.addUserByAdmin(user);
                    System.out.println(response.getMessage());
                    return;
                }
            }
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Enter a proper value...");
        }
    }

    private void removeAdmin(User user) {
        try {
            while (true) {
                displayUser(true);
                System.out.println("Enter a Email of user (enter b for go to back menu ):");
                String email = sc.nextLine();
                if (email.equalsIgnoreCase("b")) return;
                if (user.getEmail().equalsIgnoreCase(email)) {
                    System.out.println(" ");
                    System.err.println("user and delete user should not be same");
                    System.out.println(" ");
                    continue;
                }
                if (email.isEmpty()) {
                    System.err.println("filed should not be empty");
                } else {
                    Response response = adminService.removeAdmin(email);
                    System.out.println(response.getMessage());
                }

            }
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Enter a proper value...");
        }
    }

    //Add Book
    private void bookAdd() {
        while (true) {
            try {
                String bookName = FieldValidator.takeValidStringInput("Enter a Book name");
                if (bookName.isBlank()) return;
                String bookAuthor = FieldValidator.takeValidStringInput("Enter a Book Author");
                if (bookAuthor.isBlank()) return;
                String category=displayCategoryEnumValue();
                if(category.isBlank())return;
                System.out.println("Enter a Book edition (Number)(enter '0' for back to main menu):");
                int edition = sc.nextInt();
                if (bookName.equalsIgnoreCase("0")) return;
                System.out.println("Enter a Number of Book Copy (enter '0' for back to main menu):");
                int copy = sc.nextInt();
                if (bookName.equalsIgnoreCase("0")) return;
                Book book = Book.builder().
                        name(bookName).author(bookAuthor).
                        category(BookCategory.valueOf(category.toUpperCase().trim())).
                        totalNumberOfCopy(copy).edition(edition).
                        build();
                Response bookResponse = bookService.addBook(book);
                System.out.println(bookResponse.getMessage());
                System.out.println(" ");
                System.out.println("Enter 0 to back to  main menu :");
                System.out.println("Enter 1 to add other book :");
                int addOtherBook = sc.nextInt();
                if (addOtherBook == 0) return;
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.err.println("Enter a proper value :");
            }
        }
    }


    private String displayCategoryEnumValue() {
        while (true) {
            System.out.print("Categories :[");
            for (BookCategory e : BookCategory.values()) {
                System.out.print(e + ",");
            }
            System.out.println("]");
            String category = FieldValidator.takeValidStringInput("Enter a Book Category : ");
            if(category.isBlank()) return "";
            if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
                System.out.println(" ");
                System.err.println("category not found please check and renter proper category...");
                continue;
            }
            return category;
        }
    }

    private void removeBook() {
        while (true) {
            try {
                Map<Integer, Book> bookMap = displayBook();
                System.out.println("Enter a book id to delete (enter -1 to back to menu ):");
                int bookId = sc.nextInt();
                if (bookId == -1) return;
                if (bookId > -1) {
                    Book book = bookMap.get(bookId);
                    Response response = bookService.deleteBook(book);
                    if (response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())) {
                        System.out.println("Book delete successfully...");
                    } else {
                        System.out.println("Book delete failed try again");
                    }
                    sc.nextLine();
                } else {
                    System.err.println("enter a id properly...");
                }
            } catch (InputMismatchException e) {
                System.out.println("enter a proper input ");
                sc.nextLine();
            }
        }
    }

    private Map<Integer, Book> displayBook() {
        System.out.println(" ");
        System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
        System.out.println(String.format("|%-10s |%-30s | %-25s | %-28s | %-20s |", "BookId", "Name", "Category", "Author", "Copies Available"));
        System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
        Object bookObject = bookService.fetchBooks().getResponseObject();
        List<Book> books;
        Map<Integer, Book> bookMap = new HashMap<>();
        if (bookObject instanceof List<?>) {
            books = (List<Book>) bookObject;
            AtomicInteger index = new AtomicInteger();
            books.forEach(book -> bookMap.put(index.getAndIncrement() + 1, book));
            bookMap.forEach((key1, value) -> {
                String bookInfo = String.format("|%-10s |%-30s | %-25s | %-28s | %-20s |"
                        , key1, value.getName(), value.getCategory(), value.getAuthor(), value.getNumberOfCopyAvailable());
                System.out.println(bookInfo);
            });
        } else {
            System.out.println("No Book Found");
        }
        System.out.println(" ");
        return bookMap;
    }

//

    private void displayUserRecord() {
        try {
            Object response = userService.fetchUser().getResponseObject();
            if (Objects.nonNull(response)) {
                while (true) {
                    List<User> userList = null;
                    if (response instanceof List<?>) {
                        userList = (List<User>) response;
                        System.out.println("******************************************************user record***********************************************************************************************************");
                        System.out.println("+----------------+--------------------+------------+----------------+-----------------------------------------------------");
                        System.out.println(String.format("| %-15s |%-32s |%-32s | %-15s ", "Id", "user", "email", "Total Book Borrowed"));
                        System.out.println("+----------------+--------------------+------------+----------------+------------------------------------------------------");
                        AtomicInteger index = new AtomicInteger();
                        userList.stream().filter(adminUser -> adminUser.getRole().toString().equalsIgnoreCase("user")).forEach(user -> {
                            String bookInfo = String.format("| %-15s |%-32s  |%-32s  | %-15s ",
                                    index.getAndIncrement(), user.getName(), user.getEmail(), user.getTotalBookBorrowed());
                            System.out.println(bookInfo);
                        });
                    }
                    System.out.println("want to see user detail enter y (back tom menu b): ");
                    String isWantDetail = sc.nextLine().trim();
                    if (isWantDetail.equalsIgnoreCase("b")) return;
                    detailRecordOfUserBorrowedBook(isWantDetail, userList);
                }
            } else {
                System.out.println("not have any record");
            }
        } catch (
                Exception e) {
            System.out.println("Something went wrong while printing borrowed record try again...");
        }
    }

    private void detailRecordOfUserBorrowedBook(String isWantDetail, List<User> userList) {
        if (isWantDetail.equalsIgnoreCase("y")) {
            System.out.println("Enter a userId : ");
            String userId = sc.nextLine();
            if (Objects.nonNull(userList) && userList.size() > Integer.parseInt(userId)) {
                User user = userList.get(Integer.parseInt(userId) + 1);
                System.out.println(Integer.parseInt(userId) + 1);
                System.out.println(" ");
                userBookBorrowedDetail(user);
            } else {
                System.out.println("user not found");
            }
        }
    }

    private void userBookBorrowedDetail(User user) {
        Response borrowBook = borrowedBookService.fetchBorrowedBookByUserId(user.getId());
        Object responseObject = borrowBook.getResponseObject();
        System.out.println("******************************************************User detailed Record******************************************************************");
        System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+--------------+");
        System.out.println(String.format("| %-22s | %-32s | %-15s | %-16s | %-16s  |%-16s ", "user", "Book", "Date of Borrow", "Return Date", "Fine", "Status"));
        System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------+");
        List<BookBorrowed> bookBorrowedMap = null;
        if (responseObject instanceof List<?>) {
            bookBorrowedMap = (List<BookBorrowed>) responseObject;
            bookBorrowedMap.stream().filter(userData -> userData.getUserId().equalsIgnoreCase(user.getId())).forEach(bookPrint -> {
                String bookInfo = String.format("| %-22s | %-32s | %-15s | %-16s | %-16s  |%-16s ",
                        user.getName(), bookPrint.getBook().getName(), bookPrint.getBorrowDate(), bookPrint.getReturnDate(), bookPrint.getFine(), bookPrint.getStatus());
                System.out.println(bookInfo);

            });
        } else {
            System.out.println("no record found");
        }
        System.out.println(" ");
    }
}


