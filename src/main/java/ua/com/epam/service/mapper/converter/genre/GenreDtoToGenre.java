package ua.com.epam.service.mapper.converter.genre;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.genre.GenreDto;

public class GenreDtoToGenre implements Converter<Genre, GenreDto> {
    @Override
    public GenreDto convert(MappingContext<Genre, GenreDto> mappingContext) {
        Genre source = mappingContext.getSource();

        GenreDto genre = new GenreDto();
        genre.setGenreId(source.getGenreId());
        genre.setGenreName(source.getGenreName());
        genre.setGenreDescription(source.getDescription());

        return genre;
    }
}
