package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;
import ua.com.epam.entity.dto.book.SimpleBookDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.entity.exception.genre.GenreNotFoundException;
import ua.com.epam.repository.AuthorRepository;
import ua.com.epam.repository.BookRepository;
import ua.com.epam.repository.GenreRepository;
import ua.com.epam.repository.SqlQueryBuilder;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SqlQueryBuilder queryBuilder;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    public GenreDto findGenreByGenreId(long genreId) {
        Optional<Genre> exist = genreRepository.getOneByGenreId(genreId);

        if (!exist.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }

        Genre toGet = exist.get();
        return toDtoMapper.mapGenreToGenreDto(toGet);
    }

    public List<SimpleAuthorDto> getAllAuthorsInGenre(long genreId) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        return authorRepository.getAllAuthorsOfGenreByGenreId(genreId)
                .stream()
                .map(toDtoMapper::mapAuthorToSimpleAuthorDto)
                .collect(Collectors.toList());
    }

    public List<SimpleBookDto> getAllBooksOfGenre(long genreId) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        return bookRepository.getGenreBooksByGenreId(genreId)
                .stream()
                .map(toDtoMapper::mapBookToSimpleBookDto)
                .collect(Collectors.toList());
    }
}
