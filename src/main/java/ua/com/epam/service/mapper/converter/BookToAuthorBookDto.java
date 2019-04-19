package ua.com.epam.service.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.dto.book.AuthorBookDto;

public class BookToAuthorBookDto implements Converter<Book, AuthorBookDto> {
    @Override
    public AuthorBookDto convert(MappingContext<Book, AuthorBookDto> mappingContext) {
        Book b = mappingContext.getSource();

        AuthorBookDto abd = new AuthorBookDto();
        abd.setBookId(b.getBookId());
        abd.setBookName(b.getBookName());
        abd.setBookDescr(b.getBookDescription());

        return abd;
    }
}
