package userInterface.dashboard;

import entity.Book;
import entity.BookBorrowed;
import entity.User;
import enums.BookCategory;
import enums.ResponseStatus;
import enums.Role;
import services.AdminService;
import services.BookService;
import services.BorrowedBookService;
import services.UserService;
import userInterface.AbstractUi;
import userInterface.common.UpdateUser;
import utils.Response;
import utils.ValidatorRegxUtil;

import javax.management.openmbean.InvalidOpenTypeException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
                case 2 -> displayBook(false);
                case 3 -> bookAdd();
                case 4 -> removeBook();
                case 5 -> displayUserRecord();
                case 6 -> displayUser();
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
                displayBook(false);
                System.out.println("Enter a  book id to update (for back to main menu enter b: ");
                String bookId = sc.nextLine().trim();
                if (bookId.equalsIgnoreCase("b")) return;
                if (bookId.isEmpty()) {
                    System.err.println("Field should not be empty");
                }
                updateBookFields(bookId);
                }catch (Exception e) {
                System.err.println("Enter a proper input");
                sc.nextLine();
            }
            }
        }


    private void updateBookFields(String bookId) {
        try {
            List<Book> books = bookService.fetchBooks();
            Book bookObject = books.get(Integer.parseInt(bookId));
            Response bookById = bookService.getBookById(bookObject.getBookId());
            Object book = bookById.getResponseObject();
            if (book instanceof Book bookData) {
                System.out.println(" ");
                AbstractUi.displayOption(List.of("What data you want to update :",
                        "Enter 1 for update book name : ",
                        "Enter 2 for update book author : ",
                        "Enter 3 for update Number of copy : ",
                        "Enter 4 for Back to Main Menu : "));
                int op = sc.nextInt();
                switch (op) {
                    case 1 -> updateName(bookData);
                    case 2 -> updateAuthor(bookData);
                    case 3 -> updateCopy(bookData);
                    case 4 -> {
                        return;
                    }
                    default -> System.err.println("Invalid operations...");
                }

            }
        } catch (Exception e) {
            System.err.println("Enter a proper input :");
        }
    }

        private void updateCopy (Book book){
            sc.nextLine();
            try {
                displayOption(List.of("To add more copy enter 1: ", "To remove  copy enter 2: ", "Enter 3 back  to menu :"));
                int input = sc.nextInt();
                switch (input) {
                    case 1: {
                        System.out.println("Enter the number of book you want  to add : ");
                        int bookCount = sc.nextInt();
                        if (bookCount == 0) {
                            System.out.println("Enter a copy should be greater then 0 :");
                            break;
                        }
                        book.setNumberOfCopy(book.getNumberOfCopy() + bookCount);
                        System.out.println(bookService.updateBook(book).getMessage());
                        sc.nextLine();
                        displayBook(false);
                        return;
                    }
                    case 2: {
                        System.out.println("Enter the number of book you want  to remove : ");
                        int bookCount = sc.nextInt();
                        if (bookCount == 0) {
                            System.out.println("Enter a copy should be greater then 0 :");
                            break;
                        } else if (book.getNumberOfCopy() > bookCount) {
                            book.setNumberOfCopy(book.getNumberOfCopy() - bookCount);
                            System.out.println(bookService.updateBook(book).getMessage());
                            sc.nextLine();
                            displayBook(false);
                            return;
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.err.println("Enter a proper value ..");
            }
        }

        private void updateAuthor (Book book){
            sc.nextLine();
            try {
                System.out.println("Enter  Author name of Book (Enter back to b to menu :): ");
                System.out.println();
                String bookAuthorName = sc.nextLine();
                if (bookAuthorName.equalsIgnoreCase("b")) {
                    return;
                }
                book.setAuthor(bookAuthorName);
                displayBook(false);
            } catch (InputMismatchException e) {
                System.out.println("Enter a proper value ");
            }
            System.out.println(bookService.updateBook(book).getMessage());
        }


        private void updateName (Book book){
            sc.nextLine();
            try {
                System.out.println("Enter new name of Book (Enter b  to back to menu ) : ");
                String bookName = sc.nextLine();
                if (bookName.equalsIgnoreCase("back")) {
                    return;
                }
                book.setName(bookName);
                displayBook(false);
            } catch (InvalidOpenTypeException e) {
                System.err.println("Enter a proper input");
            }
            System.out.println(bookService.updateBook(book).getMessage());
        }

        private void profileView (User user){
            System.out.println("======================================================Profile====================================================================");
            System.out.println(user);
            updateUser.updateUser(user);
        }

        private void displayUser () {
            try {
                Object response = adminService.fetchUser().getResponseObject();
                if (response instanceof List<?> user) {
                    user.forEach(System.out::println);
                }
            } catch (Exception e) {
                System.out.println("something went wrong..");
            }
        }

        private void addAdmin () {
            try {
                while (true) {
                    System.out.println("Enter a Email of user (enter b for back to main menu):");
                    String email = sc.nextLine().trim();
                    if(email.equalsIgnoreCase("b")) return;
                    if (!ValidatorRegxUtil.isEmailValid(email)) {
                        System.err.println("Enter a valid email...");
                        continue;
                    }
                    System.out.println("Enter a name of user (enter b for back to main menu):");
                    String name = sc.nextLine().trim();
                    if(name.equalsIgnoreCase("b")) return;
                    System.out.println("set a password  of user (enter b for back to main menu):");
                    String password = sc.nextLine().trim();
                    if(password.equalsIgnoreCase("b")) return;
                    if (email.isEmpty() && name.isEmpty() && password.isEmpty()) {
                        System.err.println("Field should not be empty...");
                    } else {
                        User user = new User.UserBuilder().setName(name).setEmail(email).setPassword(password).setRole(Role.admin).build();
                        Response response = adminService.addUserByAdmin(user);
                        System.out.println(response.getMessage());
                        return;
                    }
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Enter a proper value...");
            }
        }

        private void removeAdmin (User user){
            try {
                while (true) {
                    System.out.println("Enter a Email of user (enter b for go to back menu ):");
                    String email = sc.nextLine();
                    if (email.equalsIgnoreCase("b")) return;
                    if (user.getEmail().equalsIgnoreCase(email)) {
                        System.out.println(" ");
                        System.err.println("user and delete user should not be same");
                        System.out.println(" ");
                        return;
                    }
                    if (email.isEmpty()) {
                        System.err.println("filed should not be empty");
                    } else {
                        Response response = adminService.removeAdmin(email);
                        System.out.println(response.getMessage());
                        break;
                    }

                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Enter a proper value...");
            }
        }

        //Add Book
        private void bookAdd () {
            try {
                System.out.println("Enter a Book name : ");
                String bookName = sc.nextLine();
                System.out.println("Enter a Book Author : ");
                String bookAuthor = sc.nextLine();
                System.out.println("Enter a Book Category : ");
                displayCategoryEnumValue();
                String category = sc.next();
                if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
                    sc.nextLine();
                    System.out.println(" ");
                    System.err.println("Enter a proper category...");
                }
                System.out.println("Enter a Book Copy : ");
                int copy = sc.nextInt();
                Book book = new Book.BookBuilder().
                        setName(bookName).setAuthor(bookAuthor).
                        setCategory(BookCategory.valueOf(category.toUpperCase().trim())).
                        setNumberOfCopy(copy).
                        build();
                Response bookResponse = bookService.addBook(book);
                System.out.println(bookResponse.getMessage());
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.err.println("Enter a proper value :");
            }
        }

        private static void displayCategoryEnumValue () {
            System.out.print("Categories :[");
            for (BookCategory e : BookCategory.values()) {
                System.out.print(e + ",");
            }
            System.out.println("]");
        }

        //delete book
        private void removeBook () {
            try {
                while (true) {
                    displayBook(true);
                    System.out.println("Enter a book id to delete (enter b to back to menu ):");
                    String bookId = sc.nextLine().trim();
                    if (bookId.equalsIgnoreCase("b")) return;
                    System.out.println("do you want to delete all copy y/n:");
                    String allCopy = sc.next().trim();
                    if (!bookId.isEmpty() || !allCopy.isEmpty()) {
                        if (allCopy.equalsIgnoreCase("y")) {
                            Response response = bookService.deleteBook(bookId, 0, true);
                            System.out.println(response.getMessage());
                            sc.nextLine();
                        } else {
                            System.out.println("Enter a how many copy want to delete :");
                            int noCopy = sc.nextInt();
                            Response response = bookService.deleteBook(bookId, noCopy, false);
                            System.out.println(response.getMessage());
                            sc.nextLine();
                        }
                    } else {
                        System.out.println("filed should not be empty : ");
                        sc.nextLine();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("enter a proper input ");
            }
        }

        private void displayBook ( boolean isForRemove){
            System.out.println(" ");
            System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
            System.out.println(String.format("|%-10s |%-30s | %-25s | %-28s | %-20s |", "BookId", "Name", "Category", "Author", "Copies Available"));
            System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
            List<Book> books = bookService.fetchBooks();
            AtomicInteger index = new AtomicInteger();
            if (Objects.nonNull(books) && !books.isEmpty()) {
                for (Book book : books) {
                    String bookInfo = String.format("|%-10s |%-30s | %-25s | %-28s | %-20s |",
                            index.getAndIncrement(), book.getName(), book.getCategory(), book.getAuthor(), book.getNumberOfCopy());
                    System.out.println(bookInfo);
                }
            } else {
                System.out.println("No Book Found");
            }
            System.out.println(" ");
        }

//

        private void displayUserRecord () {
            try {
                Object response = userService.fetchUser().getResponseObject();
                if(Objects.nonNull(response)) {
                    System.out.println("+----------------+--------------------+------------+----------------+----------");
                    System.out.println(String.format("| %-15s |%-32s | %-15s ", "Id", "user", "Total Book Borrowed"));
                    System.out.println("+----------------+--------------------+------------+----------------+-----------");
                    List<User> userList = null;
                    while (true) {
                        if (response instanceof List<?>) {
                            userList = (List<User>) response;
                            AtomicInteger index = new AtomicInteger();
                            userList.stream().filter(adminUser -> adminUser.getRole().toString().equalsIgnoreCase("user")).forEach(user -> {
                                String bookInfo = String.format("| %-15s |%-32s | %-15s ",
                                        index.getAndIncrement(), user.getName(), user.getBorrowedBooks().size());
                                System.out.println(bookInfo);
                            });
                        }
                        System.out.println("want to see user detail enter y (back tom menu b): ");
                        String isWantDetail = sc.nextLine().trim();
                        if (isWantDetail.equalsIgnoreCase("b")) return;
                        detailRecordOfUserBorredBook(isWantDetail, userList);
                    }
                }else {
                    System.out.println("not have any record");
                }
            } catch (
                    Exception e) {
                System.out.println("Something went wrong while printing borrowed record try again...");
            }
        }

        private void detailRecordOfUserBorredBook (String isWantDetail, List < User > userList){
            if (isWantDetail.equalsIgnoreCase("y")) {
                System.out.println("Enter a userId : ");
                String userId = sc.nextLine();
                if (Objects.nonNull(userList) && userList.size() > Integer.parseInt(userId)) {
                    User user = userList.get(Integer.parseInt(userId));
                    UserBookBorrowedDetail(user.getId());
                } else {
                    System.out.println("user not found");
                }
            }
        }

        private void UserBookBorrowedDetail (String userId){
            Response borrowBook = borrowedBookService.fetchBorrowedBook();
            Object responseObject = borrowBook.getResponseObject();
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            System.out.println(String.format("| %-32s | %-15s | %-15s | %-16s | %-16s  |%-16s ", "user", "Book", "Date of Borrow", "Return Date", "Fine", "Status"));
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            Map<String, BookBorrowed> bookBorrowedMap = null;
            if (responseObject instanceof Map<?, ?>) {
                bookBorrowedMap = (Map<String, BookBorrowed>) responseObject;
                bookBorrowedMap.entrySet().stream().filter(user -> user.getValue().getUserId().equalsIgnoreCase(userId)).forEach(bookPrint -> {
                    String bookInfo = String.format("| %-32s | %-15s | %-15s | %-16s | %-16s  |%-16s ",
                            bookPrint.getValue().getUser().getName(), bookPrint.getValue().getBook().getName(), bookPrint.getValue().getBorrowDate(), bookPrint.getValue().getReturnDate(), bookPrint.getValue().getFine(), bookPrint.getValue().getStatus());
                    System.out.println(bookInfo);
                });
            }
        }


        //   private void checkSerialNumberOfBook(String bookId, List<Book> bookList) {
//        System.out.println("+----------------+--------------------+------------+----------------+--------------------------------------------");
//        System.out.println(String.format("|%-10s |%-30s | %-25s | %-28s |", "BookId", "Name", "Author", "Serial Number"));
//        System.out.println("+----------------+--------------------+------------+----------------+---------------------------------------------");
//        Book book = bookList.get(Integer.parseInt(bookId));
//        if (Objects.nonNull(book)) {
//            int pageSize = 5;
//            int currentIndex = 0;
//            List<String> allSerialNumber = book.getAllSerialNumber();
//            String bookInfo = String.format("|%-10s |%-30s | %-28s | %-20s |",
//                    bookId, book.getName(), book.getAuthor(), book.getSerialNumber());
//            System.out.println(bookInfo);
//            while (currentIndex < allSerialNumber.size()) {
//                allSerialNumber.stream().
//                        skip(currentIndex).
//                        limit(pageSize).
//                        forEach(bookDisplay -> {
//                            String bookInfoSerial = String.format("|%-10s |%-30s | %-28s | %-20s |",
//                                    "", "", "", bookDisplay);
//                            System.out.println(bookInfoSerial);
//                        });
//
//                currentIndex += pageSize;
//                if (currentIndex < allSerialNumber.size()) {
//                    System.out.println("Do you want to see more record Y/N :");
//                    String input = sc.nextLine();
//                    if (input.equalsIgnoreCase("n")) {
//                        break;
//                    }
//                }
//            }
//            System.out.println("No more record to display...");
//        }
//    }
//          if (!isForRemove) {
//        displayOption(Li"Enter 1 check the serial number  : ","Enter 2 for update the book: ");
//
//
//        String input = sc.nextLine();
//        if (input.equalsIgnoreCase("y")) {
//            System.out.println("Enter a bookId : ");
//            String bookId = sc.nextLine();
//            checkSerialNumberOfBook(bookId, books);
//        }
//    }

    }


