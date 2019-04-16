package ua.com.epam.entity.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidTypeException extends RuntimeException {
    private String key;
    private String value;
    private Class clazz;
}
