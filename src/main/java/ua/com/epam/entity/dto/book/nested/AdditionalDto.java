package ua.com.epam.entity.dto.book.nested;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalDto {
    private Long pageCount = 0L;
    private SizeDto size = new SizeDto();
}
