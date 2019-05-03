package ua.com.epam.entity.dto.genre;

import io.swagger.annotations.ApiModel;
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
    private Long id;
    private String name;
}
