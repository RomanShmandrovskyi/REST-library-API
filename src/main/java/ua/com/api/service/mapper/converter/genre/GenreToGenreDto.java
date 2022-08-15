package ua.com.api.service.mapper.converter.genre;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.genre.GenreDto;

public class GenreToGenreDto implements Converter<Genre, GenreDto> {
    @Override
    public GenreDto convert(MappingContext<Genre, GenreDto> mappingContext) {
        Genre g = mappingContext.getSource();

        GenreDto gt = new GenreDto();
        gt.setGenreId(g.getGenreId());
        gt.setGenreName(g.getGenreName());
        gt.setGenreDescription(g.getDescription());

        return gt;
    }
}
