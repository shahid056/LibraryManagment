package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorRegxUtil {

    private ValidatorRegxUtil(){}

    public static  boolean isEmailValid(String email){
        String emailRex="^[A-Za-z0-9]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRex);
        Matcher matcher = pattern.matcher(email);
        return  matcher.matches();
    }

}
