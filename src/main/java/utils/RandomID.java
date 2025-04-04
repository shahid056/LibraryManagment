package utils;

import java.util.Random;
import java.util.UUID;

public class RandomID {
    public String srno(){
        UUID uuid = UUID.randomUUID();
//        new Random().nextInt(500);
        String uuidString = uuid.toString();
        return uuidString.substring(0, 8);
    }
}
