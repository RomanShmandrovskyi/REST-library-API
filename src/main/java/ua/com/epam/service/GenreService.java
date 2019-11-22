package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.exception.entity.author.AuthorNotFoundException;
import ua.com.epam.exception.entity.book.BookNotFoundException;
import ua.com.epam.exception.entity.genre.BooksInGenreArePresentException;
import ua.com.epam.exception.entity.genre.GenreAlreadyExistsException;
import ua.com.epam.exception.entity.genre.GenreNameAlreadyExistsException;
import ua.com.epam.exception.entity.genre.GenreNotFoundException;
import ua.com.epam.exception.entity.search.SearchQueryIsBlankException;
import ua.com.epam.exception.entity.search.SearchQueryIsTooShortException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.Arrays;
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
    private SearchFor searchFor;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    private Sort.Direction resolveDirection(String order) {
        return Sort.Direction.fromString(order);
    }

    private List<GenreDto> mapToDto(List<Genre> genres) {
        return genres.stream()
                .map(toDtoMapper::mapGenreToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto findGenre(long genreId) {
        Genre toGet = genreRepository.getOneByGenreId(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        return toDtoMapper.mapGenreToGenreDto(toGet);
    }

    public GenreDto findGenreOfBook(long bookId) {
        bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Genre toGet = genreRepository.getGenreOfBook(bookId);

        return toDtoMapper.mapGenreToGenreDto(toGet);
    }

    public List<GenreDto> findAllGenres(String sortBy, String order, int page, int size, boolean pageable) {
        Sort.Direction direction = resolveDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort sorter = Sort.by(direction, sortParam);

        List<Genre> genres;

        if (!pageable) {
            genres = genreRepository.findAll(sorter);
        } else {
            genres = genreRepository.getAllGenres(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(genres);
    }

    public List<GenreDto> searchForExistedGenres(String searchQuery) {
        searchQuery = searchQuery.trim();

        if (searchQuery.isEmpty()) {
            throw new SearchQueryIsBlankException();
        } else if (searchQuery.length() <= 2) {
            throw new SearchQueryIsTooShortException(searchQuery, 3);
        }

        List<String> keywords = Arrays.stream(searchQuery.split(" "))
                .filter(e -> e.length() > 2)
                .collect(Collectors.toList());

        List<Genre> searched = searchFor.genres(searchQuery, keywords);

        return mapToDto(searched);
    }

    public List<GenreDto> findAllGenresOfAuthor(long authorId, String sortBy, String order) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        Sort.Direction direction = resolveDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort sorter = Sort.by(direction, sortParam);

        return mapToDto(genreRepository.getAllGenresOfAuthorOrdered(authorId, sorter));
    }

    public GenreDto addNewGenre(GenreDto genre) {
        if (genreRepository.existsByGenreId(genre.getGenreId())) {
            throw new GenreAlreadyExistsException();
        }

        if (genreRepository.existsByGenreName(genre.getGenreName())) {
            throw new GenreNameAlreadyExistsException();
        }

        Genre toPost = toModelMapper.mapGenreDtoToGenre(genre);
        Genre response = genreRepository.save(toPost);

        return toDtoMapper.mapGenreToGenreDto(response);
    }

    public GenreDto updateExistedGenre(GenreDto genre) {
        Optional<Genre> opt = genreRepository.getOneByGenreId(genre.getGenreId());

        if (!opt.isPresent()) {
            throw new GenreNotFoundException(genre.getGenreId());
        }

        Genre proxy = opt.get();

        proxy.setGenreId(genre.getGenreId());
        proxy.setGenreName(genre.getGenreName());
        proxy.setDescription(genre.getGenreDescription());

        Genre updated = genreRepository.save(proxy);
        return toDtoMapper.mapGenreToGenreDto(updated);
    }

    public void deleteExistedGenre(long genreId, boolean forcibly) {
        Genre toDelete = genreRepository.getOneByGenreId(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        long booksCount = bookRepository.getAllBooksInGenreCount(genreId);

        if (booksCount > 0 && !forcibly) {
            throw new BooksInGenreArePresentException(genreId, booksCount);
        }

        genreRepository.delete(toDelete);
    }
}