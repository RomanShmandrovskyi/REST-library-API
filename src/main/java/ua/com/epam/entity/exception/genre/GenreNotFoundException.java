package ua.com.epam.entity.exception.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenreNotFoundException extends RuntimeException {
    private long genreId;
}
