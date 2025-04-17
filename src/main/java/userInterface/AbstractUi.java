package userInterface;

import model.User;

import java.util.List;
import java.util.Scanner;


public abstract class AbstractUi {

   Scanner sc =  new Scanner(System.in);
    public abstract void   userScreen(User user);
    public abstract void   adminScreen(User user);
    void homeScreen(){}

    public static void displayOption(List<String> options){
        options.forEach(System.out::println);
    }

}
