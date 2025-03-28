package utils;

import enums.ResponseStatus;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Builder
public class Response {
    private  Object response;
    private ResponseStatus statusCode;
    private String message;

    public Response(Object response, ResponseStatus statusCode, String message){
        this.response = response;
        this.statusCode = statusCode;
        this.message=message;
    }

    public Object getResponse() {
        return response;
    }

    public ResponseStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
