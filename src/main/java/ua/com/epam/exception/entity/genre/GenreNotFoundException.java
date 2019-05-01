package ua.com.epam.exception.entity.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenreNotFoundException extends RuntimeException {
    private long genreId;
}
