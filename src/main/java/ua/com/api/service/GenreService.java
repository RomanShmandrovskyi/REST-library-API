package ua.com.api.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.SortByPropertiesDto;
import ua.com.api.entity.dto.genre.GenreDto;
import ua.com.api.entity.dto.genre.GenreWithoutIdDto;
import ua.com.api.exception.entity.author.AuthorNotFoundException;
import ua.com.api.exception.entity.book.BookNotFoundException;
import ua.com.api.exception.entity.genre.BooksInGenreArePresentException;
import ua.com.api.exception.entity.genre.GenreAlreadyExistsException;
import ua.com.api.exception.entity.genre.GenreNotFoundException;
import ua.com.api.exception.entity.search.SearchQueryIsBlankException;
import ua.com.api.exception.entity.search.SearchQueryIsTooShortException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService extends BaseService {

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
        String sortParam = convertAndValidateSortBy(sortBy, Genre.class);
        Sort.Direction direction = resolveDirection(order);
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
        String sortParam = convertAndValidateSortBy(sortBy, Genre.class);

        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParam);

        return mapToDto(genreRepository.getAllGenresOfAuthorOrdered(authorId, sorter));
    }

    public List<SortByPropertiesDto> getSortByParameterValues() {
        return getSortByParameterValues(Genre.class);
    }

    public GenreDto addNewGenre(GenreWithoutIdDto genre) {
        if (genreRepository.existsByGenreName(genre.getName())) {
            throw new GenreAlreadyExistsException();
        }

        Genre toPost = toModelMapper.mapGenreWithoutIdToGenre(genre);
        Genre response = genreRepository.save(toPost);

        return toDtoMapper.mapGenreToGenreDto(response);
    }

    public GenreDto updateExistedGenre(long genreId, GenreWithoutIdDto genre) {
        Genre proxy = genreRepository.getOneByGenreId(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        if (genreRepository.existsByGenreNameAndGenreIdNotLike(genre.getName(), genreId)) {
            throw new GenreAlreadyExistsException();
        }

        proxy.setGenreName(genre.getName());
        proxy.setDescription(genre.getDescription());

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