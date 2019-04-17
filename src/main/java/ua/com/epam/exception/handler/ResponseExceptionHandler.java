package ua.com.epam.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.epam.entity.ExceptionResponse;
import ua.com.epam.entity.exception.*;
import ua.com.epam.entity.exception.type.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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
                        "'" + matme.getName() + "' value must be a number!"),
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
                        "Invalid property name - '" + nsjpe.getPropName() + "'!"),
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
                        "Order type must be 'asc' or 'desc' instead '" + iote.getInvalidOrder() + "'"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = InvalidLimitFormatException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidNumberFormat() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "'limit' value must be a number!"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = AuthorAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorConflict() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "Author with such 'authorId' already exists!"),
                HttpStatus.CONFLICT);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());

        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable cause = ex.getMostSpecificCause();
        String message = "";

        if (cause instanceof JsonParseException) {
            message = "Request JSON is invalid!";
        } else if (cause instanceof InvalidDateTypeException) {
            InvalidDateTypeException e = (InvalidDateTypeException) cause;
            message = "Value '" + e.getValue() + "' in '" + e.getKey() + "' is invalid! Valid format is: yyyy-MM-dd!";
        } else if (cause instanceof InvalidTypeException) {
            InvalidTypeException e = (InvalidTypeException) cause;
            message = "Value '" + e.getValue() + "' in '" + e.getKey() + "' is require to be of '" + e.getClazz().getSimpleName() + "' type!";
        } else {
            message = Objects.requireNonNull(ex.getMessage()).split(": ")[0];
        }

        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = IdMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleIdMismatch() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Entity id in path must be the same as in body!"),
                HttpStatus.CONFLICT);
    }
}
