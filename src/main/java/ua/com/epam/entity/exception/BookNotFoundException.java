package ua.com.epam.entity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookNotFoundException extends RuntimeException {
    private long bookId;
}
