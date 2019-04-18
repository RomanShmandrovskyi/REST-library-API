package ua.com.epam.entity.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long bookId = 0L;

    private String bookName;

    private String bookLanguage;

    private AdditionalDto additional;

    @Min(value = 1950, message = "Publication year must be higher then 1950")
    @Max(value = 2019, message = "Publication year must be lower then 2019")
    private Integer publicationYear = 0;
}
