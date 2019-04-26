package ua.com.epam.service.mapper.converter.group;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.GroupByBooksCount;
import ua.com.epam.entity.dto.book.GenreGroupByBooksDto;

public class GroupByBooksToGenre implements Converter<GroupByBooksCount, GenreGroupByBooksDto> {
    @Override
    public GenreGroupByBooksDto convert(MappingContext<GroupByBooksCount, GenreGroupByBooksDto> mappingContext) {
        GroupByBooksCount source = mappingContext.getSource();

        GenreGroupByBooksDto author = new GenreGroupByBooksDto();
        author.setGenreId(source.getId());
        author.setGenreName(source.getName());
        author.setBooksCount(source.getBooksCount());

        return author;
    }
}