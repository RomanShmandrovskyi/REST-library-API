package ua.com.epam.service.mapper.converter.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.dto.book.BookDto;

public class BookDtoToBook implements Converter<BookDto, Book> {
    @Override
    public Book convert(MappingContext<BookDto, Book> mappingContext) {
        BookDto source = mappingContext.getSource();

        Book b = new Book();
        b.setBookId(source.getBookId());
        b.setBookName(source.getBookName());
        b.setBookLang(source.getBookLanguage());
        b.setDescription(source.getBookDescription());
        b.setPageCount(source.getAdditional().getPageCount());
        b.setBookHeight(source.getAdditional().getSize().getHeight());
        b.setBookLength(source.getAdditional().getSize().getLength());
        b.setBookWidth(source.getAdditional().getSize().getWidth());
        b.setPublicationYear(source.getPublicationYear());

        return b;
    }
}
