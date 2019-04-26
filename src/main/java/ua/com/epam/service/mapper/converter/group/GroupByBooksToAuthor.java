package ua.com.epam.service.mapper.converter.group;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.GroupByBooksCount;
import ua.com.epam.entity.dto.author.AuthorGroupByBooksDto;

public class GroupByBooksToAuthor implements Converter<GroupByBooksCount, AuthorGroupByBooksDto> {
    @Override
    public AuthorGroupByBooksDto convert(MappingContext<GroupByBooksCount, AuthorGroupByBooksDto> mappingContext) {
        GroupByBooksCount source = mappingContext.getSource();

        AuthorGroupByBooksDto author = new AuthorGroupByBooksDto();
        author.setAuthorId(source.getId());
        author.setAuthorName(source.getName());
        author.setBooksCount(source.getBooksCount());

        return author;
    }
}