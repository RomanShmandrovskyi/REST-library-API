package ua.com.epam.entity.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorGroupByBooksDto {
    private Long authorId;
    private String authorName;
    private Long booksCount;
}
