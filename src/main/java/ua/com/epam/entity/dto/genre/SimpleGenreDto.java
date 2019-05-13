package ua.com.epam.entity.dto.genre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SimpleGenre")
public class SimpleGenreDto {
    @ApiModelProperty(required = true)
    private Long id;

    @ApiModelProperty(required = true, position = 1)
    private String name;
}
