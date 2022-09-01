package ua.com.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ua.com.api.entity.dto.SortByPropertiesDto;
import ua.com.api.exception.entity.InvalidSortByParameterValueException;
import ua.com.api.repository.AuthorRepository;
import ua.com.api.repository.BookRepository;
import ua.com.api.repository.GenreRepository;
import ua.com.api.repository.SearchFor;
import ua.com.api.service.mapper.DtoToModelMapper;
import ua.com.api.service.mapper.ModelToDtoMapper;
import ua.com.api.service.util.annotation.ForSort;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BaseService {

    @Autowired
    protected GenreRepository genreRepository;

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected SearchFor searchFor;

    @Autowired
    protected ModelToDtoMapper toDtoMapper;

    @Autowired
    protected DtoToModelMapper toModelMapper;

    protected Sort.Direction resolveDirection(String order) {
        return Sort.Direction.fromString(order);
    }

    protected String convertAndValidateSortBy(String sortBy, Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ForSort.class))
                .filter(f -> Arrays.asList(f.getAnnotation(ForSort.class).aliases()).contains(sortBy) ||
                        f.getAnnotation(ForSort.class).defaultValue().equals(sortBy))
                .map(Field::getName)
                .findFirst()
                .orElseThrow(() -> new InvalidSortByParameterValueException(sortBy, "Value '" + sortBy + "' is not allowed for sorting! Try other value!"));
    }

    protected List<SortByPropertiesDto> getSortByParameterValues(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ForSort.class))
                .map(f -> f.getAnnotation(ForSort.class))
                .map(a -> new SortByPropertiesDto(a.defaultValue(), Arrays.asList(a.aliases())))
                .toList();
    }
}
