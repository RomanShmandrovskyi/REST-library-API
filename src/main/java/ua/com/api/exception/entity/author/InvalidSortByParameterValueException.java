package ua.com.api.exception.entity.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class InvalidSortByParameterValueException extends RuntimeException {
    private String sortBy;
    private String errorMessage;
}
