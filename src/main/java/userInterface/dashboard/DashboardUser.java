package userInterface.dashboard;

import entity.BookBorrowed;
import entity.User;
import enums.ResponseStatus;
import server.BorrowedBookServices;
import server.UserServices;
import servicesImpl.BookServicesImpl;
import servicesImpl.BorrowedBookServicesImpl;
import servicesImpl.UserServicesImpl;
import userInterface.AbstractUi;
import utils.Response;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DashboardUser extends AbstractUi {
    static Scanner sc = new Scanner(System.in);
    private final UserServices userServices = UserServicesImpl.getInstance();

   public void UserScreen(User user) {

       boolean isExit=true;
       while (isExit) {
            displayOption(List.of("*************Library***********************",
                    "Enter 1 for profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Borrow Book",
                    "Enter 4 for Return Book",
                    "Enter 5 for sign out"));
            int choice;
            try {
                System.out.println("Enter Your Options:");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("please enter proper input");
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 1-> profileView(user);
                case 2-> BookServicesImpl.getBookInstance().displayBook(user);
                case 3-> bookBoorowAndReturn(3,user);
                case 4-> bookBoorowAndReturn(4,user);
                case 5-> isExit=false;
                default-> System.err.println("invalid operation");
            }
        }
    }

    private void profileView(User user){
        System.out.println("======================================================Profile====================================================================");
       System.out.println(user);
       userServices.updateUser(user);
    }

    private  void bookBoorowAndReturn(int caseInput, User user) {
       try {
           BookServicesImpl.getBookInstance().displayBook(user);
           System.out.println("Enter a BookId to Borrow :");
           String bookId = sc.next().toLowerCase();
           BorrowedBookServices bookServices =BorrowedBookServicesImpl.getInstance();
           if (caseInput == 3) {
               BookBorrowed bookBorrowed = new BookBorrowed(bookId, user.getId(), LocalDate.now());
               Response response=bookServices.borrowBook(bookBorrowed);
               if(response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())){
                   System.out.println(response.getMessage());
               }else {
                   System.out.println(response.getMessage());
               }
           } else if (caseInput == 4) {
               Response response=bookServices.returnBook(bookId,user);
               if(response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())){
                   System.out.println(response.getMessage());
               }else {
                   System.out.println(response.getMessage());
               }
           }
       }catch (InputMismatchException | IllegalArgumentException e){
           System.err.println("Please enter proper inputs ");
       }
    }
}
