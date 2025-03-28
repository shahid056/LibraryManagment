package customexception;

public class DataInsertException extends Exception {
    public DataInsertException(String message){
        super(message);
    }
}