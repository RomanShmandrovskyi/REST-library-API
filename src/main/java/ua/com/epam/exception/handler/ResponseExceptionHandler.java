package ua.com.epam.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.epam.entity.ExceptionResponse;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.author.AuthorAlreadyExistsException;
import ua.com.epam.entity.exception.author.AuthorNotFoundException;
import ua.com.epam.entity.exception.author.BooksInAuthorIsPresentException;
import ua.com.epam.entity.exception.genre.GenreAlreadyExistsException;
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
        String message = "Author with 'authorId' = '%d' doesn't exist!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        String.format(message, enfe.getAuthorId())),
                HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleNumberFormat(MethodArgumentTypeMismatchException matme) {
        String message = "'%s' value must be of '%s' type!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        String.format(message, matme.getName(), matme.getParameter().getParameterType().getSimpleName())),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = NoSuchJsonKeyException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchJsonProperty(NoSuchJsonKeyException nsjpe) {
        String message = "No such JSON property - '%s'!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        String.format(message, nsjpe.getPropName())),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = InvalidOrderTypeException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOrderType(InvalidOrderTypeException iote) {
        String message = "Order type must be 'asc' or 'desc' instead '%s'!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        String.format(message, iote.getInvalidOrder())),
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

    @ResponseBody
    @ExceptionHandler(value = GenreAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleGenreConflict() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "Genre with such 'genreId' already exists!"),
                HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(value = IdMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleIdMismatch() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Entity ID in URL must be the same as in body!"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = BooksInAuthorIsPresentException.class)
    public ResponseEntity<ExceptionResponse> handleBooksIsPresent(BooksInAuthorIsPresentException biaip) {
        String message = "Author with 'authorId' = '%d' has '%d' books! To delete - set 'forcibly' parameter to 'true'!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        String.format(message, biaip.getAuthorId(), biaip.getBooksCount())),
                HttpStatus.BAD_REQUEST);
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
    @ResponseBody
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable cause = ex.getMostSpecificCause();
        String message;

        if (cause instanceof JsonParseException) {
            message = "Request JSON is invalid!";
        } else if (cause instanceof InvalidDateTypeException) {
            InvalidDateTypeException e = (InvalidDateTypeException) cause;
            message = "Value '" + e.getValue() + "' in '" + e.getKey() + "' is invalid! Valid format is: yyyy-MM-dd!";
        } else if (cause instanceof InvalidTypeException) {
            InvalidTypeException e = (InvalidTypeException) cause;
            message = "'" + e.getKey() + "' is require to be of '" + e.getClazz().getSimpleName() + "' type!";
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

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Method '%s' mapped by '%s' not supported!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                        String.format(message, ex.getMethod(), request.getDescription(false).split("=")[1])),
                HttpStatus.METHOD_NOT_ALLOWED);
    }
}
