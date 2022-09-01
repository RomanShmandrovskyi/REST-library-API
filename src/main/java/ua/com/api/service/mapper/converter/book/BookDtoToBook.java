package ua.com.api.service.mapper.converter.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.api.entity.Book;
import ua.com.api.entity.dto.book.BookDto;

public class BookDtoToBook implements Converter<BookDto, Book> {
    @Override
    public Book convert(MappingContext<BookDto, Book> mappingContext) {
        BookDto source = mappingContext.getSource();

        Book b = new Book();
        b.setBookName(source.getBookName());
        b.setBookLanguage(source.getBookLanguage());
        b.setBookDescription(source.getBookDescription());
        b.setPagesCount(source.getAdditional().getPageCount());
        b.setBookHeight(source.getAdditional().getSize().getHeight());
        b.setBookLength(source.getAdditional().getSize().getLength());
        b.setBookWidth(source.getAdditional().getSize().getWidth());
        b.setPublicationYear(source.getPublicationYear());

        return b;
    }
}
