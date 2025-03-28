package utils;

import java.util.UUID;

public class RandomID {
    public String srno(){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        return uuidString.substring(0, 8);
    }
}
