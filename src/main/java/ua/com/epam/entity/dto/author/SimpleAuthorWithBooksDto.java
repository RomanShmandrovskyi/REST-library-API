package ua.com.epam.entity.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.nested.NameDto;
import ua.com.epam.entity.dto.book.SimpleBookDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleAuthorWithBooksDto {
    private Long authorId;
    private NameDto authorName;
    private List<SimpleBookDto> books;
}
