package ua.com.epam.service.mapper.converter.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.dto.book.SimpleBookDto;

public class BookToSimpleBookDto implements Converter<Book, SimpleBookDto> {
    @Override
    public SimpleBookDto convert(MappingContext<Book, SimpleBookDto> mappingContext) {
        Book b = mappingContext.getSource();

        SimpleBookDto abd = new SimpleBookDto();
        abd.setId(b.getBookId());
        abd.setName(b.getBookName());
        abd.setDescription(b.getBookDescription());

        return abd;
    }
}
