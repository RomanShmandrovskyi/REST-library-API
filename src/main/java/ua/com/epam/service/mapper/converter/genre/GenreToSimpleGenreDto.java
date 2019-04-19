package ua.com.epam.service.mapper.converter.genre;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;

public class GenreToSimpleGenreDto implements Converter<Genre, SimpleGenreDto> {
    @Override
    public SimpleGenreDto convert(MappingContext<Genre, SimpleGenreDto> mappingContext) {
        Genre source = mappingContext.getSource();

        SimpleGenreDto agd = new SimpleGenreDto();
        agd.setId(source.getGenreId());
        agd.setName(source.getGenreName());
        return agd;
    }
}
