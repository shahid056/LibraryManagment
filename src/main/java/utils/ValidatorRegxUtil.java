package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorRegxUtil {

    private ValidatorRegxUtil(){}

    public static  boolean isEmailValid(String email){
        String emailRex="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRex);
        Matcher matcher = pattern.matcher(email);
        return  matcher.matches();
    }

}
