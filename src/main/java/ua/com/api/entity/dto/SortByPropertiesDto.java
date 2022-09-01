package ua.com.api.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Schema(name = "SortByProperties")
public class SortByPropertiesDto {
    private String defaultValue;
    private List<String> aliases;
}
