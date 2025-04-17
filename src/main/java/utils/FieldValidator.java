package utils;

import java.util.Scanner;

public class FieldValidator {
       static Scanner sc = new Scanner(System.in);

    public static String takeValidStringInput(String msg){
        String input;
        while (true){
            System.out.println(msg+" (enter '0' for back main menu) : ");
            input = sc.nextLine().trim();
            if(input.equalsIgnoreCase("0")) return "";
            if(input.isBlank()){
                System.out.println("Field should not be empty...");
                continue;
            }
            return input;
        }
    }


}
