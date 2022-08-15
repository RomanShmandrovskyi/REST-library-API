package ua.com.api.exception.entity.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorNotFoundException extends RuntimeException {
    private long authorId;
}
