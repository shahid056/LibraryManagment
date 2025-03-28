package customexception;

public class DataUpdateException extends RuntimeException{
    public DataUpdateException(String message){
        super(message);
    }
}
