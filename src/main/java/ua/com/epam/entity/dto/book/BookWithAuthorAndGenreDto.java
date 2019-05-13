package ua.com.epam.entity.dto.book;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;
import ua.com.epam.entity.dto.book.nested.AdditionalDto;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "BookFull")
public class BookWithAuthorAndGenreDto {
    @ApiModelProperty(required = true)
    private Long bookId;

    @ApiModelProperty(required = true, position = 1)
    private String bookName;

    @ApiModelProperty(required = true, position = 2)
    private String bookLanguage;

    @ApiModelProperty(position = 3)
    private AdditionalDto additional;

    @ApiModelProperty(position = 4)
    private Integer publicationYear;

    @ApiModelProperty(position = 5)
    private SimpleAuthorDto author;

    @ApiModelProperty(position = 6)
    private SimpleGenreDto genre;
}
