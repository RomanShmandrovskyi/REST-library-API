package ua.com.epam.entity.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenreGroupByBooksDto {
    private Long genreId;
    private String genreName;
    private Long booksCount;
}
