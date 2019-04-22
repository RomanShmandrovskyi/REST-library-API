package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;
import ua.com.epam.entity.dto.book.SimpleBookDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.entity.dto.genre.SimpleGenreWithAuthorsDto;
import ua.com.epam.entity.dto.genre.SimpleGenreWithBooksDto;
import ua.com.epam.entity.exception.IdMismatchException;
import ua.com.epam.entity.exception.genre.GenreAlreadyExistsException;
import ua.com.epam.entity.exception.genre.GenreNotFoundException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;
import ua.com.epam.service.mapper.converter.genre.BooksInGenreIsPresentException;

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

    public SimpleGenreWithAuthorsDto getGenreWithItAuthors(long genreId, int authorsCount) {
        Optional<Genre> opt = genreRepository.getOneByGenreId(genreId);

        if (!opt.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }

        Genre genre = opt.get();

        List<SimpleAuthorDto> authorsInGenre = authorRepository.getAllAuthorsOfGenreByGenreId(genreId)
                .stream()
                .limit(authorsCount)
                .map(toDtoMapper::mapAuthorToSimpleAuthorDto)
                .collect(Collectors.toList());

        return new SimpleGenreWithAuthorsDto(
                genre.getGenreId(),
                genre.getGenreName(),
                authorsInGenre);
    }

    public SimpleGenreWithBooksDto getGenreWithItBooks(long genreId, int booksCount) {
        Optional<Genre> opt = genreRepository.getOneByGenreId(genreId);

        if (!opt.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }

        Genre genre = opt.get();

        List<SimpleBookDto> books = bookRepository.getGenreBooksByGenreId(genreId)
                .stream()
                .limit(booksCount)
                .map(toDtoMapper::mapBookToSimpleBookDto)
                .collect(Collectors.toList());

        return new SimpleGenreWithBooksDto(
                genre.getGenreId(),
                genre.getGenreName(),
                books);
    }

    public List<GenreDto> getAllGenres(String sortBy, String order, int page, int size) {
        Sort.Direction orderType;

        if (order.equals("desc"))
            orderType = Sort.Direction.DESC;
        else
            orderType = Sort.Direction.ASC;

        String parameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return genreRepository.findAllOrderBy(Sort.by(orderType, parameter))
                .stream()
                .skip((page - 1) * size)
                .limit(size)
                .map(toDtoMapper::mapGenreToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto addNewGenre(GenreDto genre) {
        if (genreRepository.existsByGenreId(genre.getGenreId())) {
            throw new GenreAlreadyExistsException();
        }

        Genre toPost = toModelMapper.mapGenreDtoToGenre(genre);
        Genre response = genreRepository.save(toPost);

        return toDtoMapper.mapGenreToGenreDto(response);
    }

    public GenreDto updateExistedGenre(long genreId, GenreDto genre) {
        Optional<Genre> opt = genreRepository.getOneByGenreId(genreId);

        if (!opt.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }
        if (genreId != genre.getGenreId()) {
            throw new IdMismatchException();
        }

        Genre proxy = opt.get();

        proxy.setGenreId(genre.getGenreId());
        proxy.setGenreName(genre.getGenreName());
        proxy.setGenreDescription(genre.getDescription());

        Genre updated = genreRepository.save(proxy);
        return toDtoMapper.mapGenreToGenreDto(updated);
    }

    public GenreDto deleteExistedGenre(long genreId, boolean forcibly) {
        Optional<Genre> opt = genreRepository.getOneByGenreId(genreId);

        if (!opt.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }

        Genre toDelete = opt.get();
        long booksCount = bookRepository.getGenreBooksByGenreId(genreId).size();

        if (booksCount > 0 && !forcibly) {
            throw new BooksInGenreIsPresentException(genreId, booksCount);
        }

        genreRepository.delete(toDelete);
        return toDtoMapper.mapGenreToGenreDto(toDelete);
    }
}
