package ua.com.epam.entity.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenreNotFoundException extends RuntimeException {
    private long genreId;
}
