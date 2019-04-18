package ua.com.epam.entity.exception.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorNotFoundException extends RuntimeException {
    private long authorId;
}
