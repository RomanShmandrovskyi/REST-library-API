package ua.com.epam.service.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.genre.AuthorGenreDto;

public class GenreToAuthorGenreDto implements Converter<Genre, AuthorGenreDto> {
    @Override
    public AuthorGenreDto convert(MappingContext<Genre, AuthorGenreDto> mappingContext) {
        Genre source = mappingContext.getSource();

        AuthorGenreDto agd = new AuthorGenreDto();
        agd.setGenreId(source.getGenreId());
        agd.setGenreName(source.getGenreName());
        return agd;
    }
}
