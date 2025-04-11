package userInterface;

import model.User;

import java.util.List;

public abstract class AbstractUi {
    void userScreen(User user){}
    void adminScreen(User user){}
    void homeScreen(){}

    public static void displayOption(List<String> options){
        options.forEach(System.out::println);
    }
}
