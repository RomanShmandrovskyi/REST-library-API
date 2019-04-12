package ua.com.epam.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.epam.entity.ExceptionResponse;
import ua.com.epam.entity.exception.AuthorNotFoundException;
import ua.com.epam.entity.exception.InvalidOrderTypeException;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private String generateDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS dd-MM-yyyy");
        return formatter.format(new Date());
    }

    @ResponseBody
    @ExceptionHandler(value = AuthorNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorNotFound(AuthorNotFoundException enfe) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "Author with 'authorId' = " + enfe.getAuthorId() + " not found!"),
                HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleNumberFormat(MethodArgumentTypeMismatchException matme) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                        "'" + matme.getName() + "' value must be a number of long type!"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = NoSuchJsonKeyException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchJsonProperty(NoSuchJsonKeyException nsjpe) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Entity hasn't property like '" + nsjpe.getPropName() + "'!"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = InvalidOrderTypeException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOrderType(InvalidOrderTypeException iote) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Order type must be 'asc' or 'desc' instead " + iote.getInvalidOrder()),
                HttpStatus.BAD_REQUEST);
    }
}
