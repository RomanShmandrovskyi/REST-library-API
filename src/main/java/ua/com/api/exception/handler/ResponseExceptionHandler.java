package ua.com.api.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.api.exception.entity.author.AuthorAlreadyExistsException;
import ua.com.api.exception.entity.author.AuthorNotFoundException;
import ua.com.api.exception.entity.author.BooksInAuthorArePresentException;
import ua.com.api.exception.entity.InvalidSortByParameterValueException;
import ua.com.api.exception.entity.book.BookAlreadyExistsException;
import ua.com.api.exception.entity.book.BookNotFoundException;
import ua.com.api.exception.entity.genre.BooksInGenreArePresentException;
import ua.com.api.exception.entity.genre.GenreAlreadyExistsException;
import ua.com.api.exception.entity.genre.GenreNotFoundException;
import ua.com.api.exception.entity.search.SearchKeywordsIsTooShortException;
import ua.com.api.exception.entity.search.SearchQueryIsBlankException;
import ua.com.api.exception.entity.search.SearchQueryIsTooShortException;
import ua.com.api.exception.entity.type.InvalidDateTypeException;
import ua.com.api.exception.entity.type.InvalidTypeException;
import ua.com.api.exception.entity.type.InvalidYearValueException;
import ua.com.api.exception.model.ExceptionResponse;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private String generateDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS dd-MM-yyyy");
        return formatter.format(new Date());
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstrainViolationError(ConstraintViolationException cve) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        cve.getLocalizedMessage().split(": ")[1]),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = InvalidSortByParameterValueException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchSortByParam(InvalidSortByParameterValueException nssbpe) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        nssbpe.getErrorMessage()),
                HttpStatus.BAD_REQUEST);
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
    @ExceptionHandler(value = BookNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBookNotFound(BookNotFoundException bnfe) {
        String message = "Book with 'bookId' = '%d' doesn't exist!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        String.format(message, bnfe.getBookId())),
                HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(value = GenreNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleGenreNotFound(GenreNotFoundException gnfe) {
        String message = "Genre with 'genreId' = '%d' doesn't exist!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        String.format(message, gnfe.getGenreId())),
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
    @ExceptionHandler(value = AuthorAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorConflict() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "Author with such name already exists!"),
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
    @ExceptionHandler(value = GenreNameAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleGenreNameConflict() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "Genre with such 'genreName' already exists!"),
                HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(value = BookAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleBookConflict() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "Book with such 'bookId' already exists!"),
                HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(value = BooksInAuthorArePresentException.class)
    public ResponseEntity<ExceptionResponse> handleBooksIsPresent(BooksInAuthorArePresentException biaip) {
        long booksCount = biaip.getBooksCount();
        String b = booksCount == 1 ? " book! " : " books! ";
        String message = "Author with 'authorId' = '%d' has '%d'" + b + "To delete - set 'forcibly' parameter to 'true'!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        String.format(message, biaip.getAuthorId(), booksCount)),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = BooksInGenreArePresentException.class)
    public ResponseEntity<ExceptionResponse> handleBooksInGenreArePresent(BooksInGenreArePresentException bigap) {
        long booksCount = bigap.getBooksCount();
        String b = booksCount == 1 ? " book! " : " books! ";
        String message = "Genre with 'genreId' = '%d' has '%d'" + b + "To delete - set 'forcibly' parameter to 'true'!";
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        String.format(message, bigap.getGenreId(), booksCount)),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = SearchQueryIsTooShortException.class)
    public ResponseEntity<ExceptionResponse> handleSearchedPhraseIsTooShort(SearchQueryIsTooShortException sqits) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Searched phrase '" + sqits.getSearchedQuery() + "' is too short! Try at least "
                                + sqits.getMinSearchedQueryLength() + " symbols (excluding spaces)!"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = SearchKeywordsIsTooShortException.class)
    public ResponseEntity<ExceptionResponse> handleKeywordsIsTooShort() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Searched query keywords is too short! Try at least 3 symbols per keyword!"),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = SearchQueryIsBlankException.class)
    public ResponseEntity<ExceptionResponse> handleSearchedPhraseIsTooShort() {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Searched phrase can not be blank!"),
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
        } else if (cause instanceof InvalidDateTypeException e) {
            message = "Value '" + e.getValue() + "' in '" + e.getKey() + "' is invalid! Valid format is: yyyy-MM-dd!";
        } else if (cause instanceof InvalidTypeException e) {
            message = "'" + e.getKey() + "' is require to be of '" + e.getClazz().getSimpleName() + "' type!";
        } else if (cause instanceof InvalidYearValueException) {
            message = "Year value can't be grater then " + Year.now().getValue();
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

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        generateDate(),
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(),
                        ex.getMessage()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}