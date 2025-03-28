package userInterface;

import entity.User;

import java.util.List;

public abstract class AbstractUi {
    void UserScreen(User user){}
    void AdminScreen(User user){}
    void HomeScreen(){}

    public static void displayOption( List<String> options){
        options.forEach(System.out::println);
    }
}
