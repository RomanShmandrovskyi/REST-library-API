package ua.com.epam.service.mapper.converter.book;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.book.nested.AdditionalDto;
import ua.com.epam.entity.dto.book.nested.SizeDto;

public class BookToBookDto implements Converter<Book, BookDto> {

    @Override
    public BookDto convert(MappingContext<Book, BookDto> mappingContext) {
        Book source = mappingContext.getSource();

        BookDto dto = new BookDto();
        dto.setBookId(source.getBookId());
        dto.setBookName(source.getBookName());
        dto.setBookLanguage(source.getBookLang());
        dto.setPublicationYear(source.getPublicationYear());
        dto.setBookDescription(source.getDescription());

        AdditionalDto adds = new AdditionalDto();
        SizeDto size = new SizeDto();
        adds.setPageCount(source.getPageCount());
        size.setHeight(source.getBookHeight());
        size.setLength(source.getBookLength());
        size.setWidth(source.getBookWidth());
        adds.setSize(size);

        dto.setAdditional(adds);

        return dto;
    }
}
