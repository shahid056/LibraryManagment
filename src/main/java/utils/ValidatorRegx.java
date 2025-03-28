package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorRegx {

    private ValidatorRegx(){}

    public static  boolean isEmailValid(String email){
        String emailRex="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRex);
        Matcher matcher = pattern.matcher(email);
        return  matcher.matches();
    }
    public static boolean isNameValid(String name){
        String nameRegx="^[A-Z][a-zA-Z-' ]*$ \n";
        Pattern pattern = Pattern.compile(nameRegx);
        Matcher matcher = pattern.matcher(name);
        return  matcher.matches();
    }
}
