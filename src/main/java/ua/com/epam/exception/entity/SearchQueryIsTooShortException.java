package ua.com.epam.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchQueryIsTooShortException extends RuntimeException {
    private String searchedQuery;
    private boolean isBlank;
    private boolean tooShort;
}
