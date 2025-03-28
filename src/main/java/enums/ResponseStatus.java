package enums;

public enum ResponseStatus {
    SUCCESS(200),
    Error(400);

    final int statusCode;

    ResponseStatus(int statusCode){
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
