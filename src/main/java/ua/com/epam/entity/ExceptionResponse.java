package ua.com.epam.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String timeStamp;
    private int statusCode;
    private String error;
    private String errorMessage;
}
