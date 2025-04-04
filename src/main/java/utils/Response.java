package utils;

import enums.ResponseStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder(access = AccessLevel.PUBLIC)
public class Response {
    private  Object responseObject;
    private ResponseStatus statusCode;
    private String message;
}
