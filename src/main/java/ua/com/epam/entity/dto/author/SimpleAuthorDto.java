package ua.com.epam.entity.dto.author;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.nested.NameDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("SimpleAuthor")
public class SimpleAuthorDto {
    @ApiModelProperty(required = true)
    private Long id;

    @ApiModelProperty(required = true, position = 1)
    private NameDto name;
}
