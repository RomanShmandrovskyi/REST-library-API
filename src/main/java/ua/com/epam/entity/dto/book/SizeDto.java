package ua.com.epam.entity.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SizeDto {
    private Double height = 0.0;
    private Double width = 0.0;
    private Double length = 0.0;
}
