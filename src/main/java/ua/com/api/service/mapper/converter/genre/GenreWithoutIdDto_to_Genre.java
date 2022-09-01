package ua.com.api.service.mapper.converter.genre;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.genre.GenreWithoutIdDto;

public class GenreWithoutIdDto_to_Genre implements Converter<GenreWithoutIdDto, Genre> {
    @Override
    public Genre convert(MappingContext<GenreWithoutIdDto, Genre> mappingContext) {
        GenreWithoutIdDto source = mappingContext.getSource();

        var genre = new Genre();
        genre.setGenreName(source.getName());
        genre.setDescription(source.getDescription());

        return genre;
    }
}
