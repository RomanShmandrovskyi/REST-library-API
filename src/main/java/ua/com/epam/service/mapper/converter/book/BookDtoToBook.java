package ua.com.epam.service.mapper.converter.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.dto.book.BookDto;

public class BookDtoToBook implements Converter<BookDto, Book> {
    @Override
    public Book convert(MappingContext<BookDto, Book> mappingContext) {
        return null;
    }
}
