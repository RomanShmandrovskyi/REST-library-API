package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.exception.entity.IdMismatchException;
import ua.com.epam.exception.entity.author.AuthorNotFoundException;
import ua.com.epam.exception.entity.book.BookNotFoundException;
import ua.com.epam.exception.entity.genre.BooksInGenreArePresentException;
import ua.com.epam.exception.entity.genre.GenreAlreadyExistsException;
import ua.com.epam.exception.entity.genre.GenreNameAlreadyExistsException;
import ua.com.epam.exception.entity.genre.GenreNotFoundException;
import ua.com.epam.repository.AuthorRepository;
import ua.com.epam.repository.BookRepository;
import ua.com.epam.repository.GenreRepository;
import ua.com.epam.repository.JsonKeysConformity;
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
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    private Sort.Direction getSortDirection(String order) {
        Sort.Direction orderType = null;

        if (order.equals("desc"))
            orderType = Sort.Direction.DESC;
        else if (order.equals("asc"))
            orderType = Sort.Direction.ASC;

        return orderType;
    }

    public GenreDto findGenreByGenreId(long genreId) {
        Genre toGet = genreRepository.getOneByGenreId(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        return toDtoMapper.mapGenreToGenreDto(toGet);
    }

    public GenreDto findGenreOfBook(long bookId) {
        if (!bookRepository.existsByBookId(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Genre toGet = genreRepository.getGenreOfBook(bookId);

        return toDtoMapper.mapGenreToGenreDto(toGet);
    }

    public List<GenreDto> findAllGenres(String sortBy, String order, int page, int size, boolean pageable) {
        Sort.Direction orderType = getSortDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        List<Genre> genres = pageable ?
                genreRepository.getAllGenresOrderedPaginated(Sort.by(orderType, sortParam), PageRequest.of(page - 1, size)) :
                genreRepository.getAllGenresOrdered(Sort.by(orderType, sortParam));

        return genres.stream()
                .map(toDtoMapper::mapGenreToGenreDto)
                .collect(Collectors.toList());
    }

    public List<GenreDto> findAllGenresOfAuthor(long authorId, String sortBy, String order) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        Sort.Direction orderType = getSortDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return genreRepository
                .getAllGenresOfAuthorOrdered(authorId, Sort.by(orderType, sortParam))
                .stream()
                .map(toDtoMapper::mapGenreToGenreDto)
                .collect(Collectors.toList());
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
        proxy.setDescription(genre.getGenreDescription());

        Genre updated = genreRepository.save(proxy);
        return toDtoMapper.mapGenreToGenreDto(updated);
    }

    public GenreDto deleteExistedGenre(long genreId, boolean forcibly) {
        Genre toDelete = genreRepository.getOneByGenreId(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        long booksCount = bookRepository.getAllBooksInGenre(genreId).size();

        if (booksCount > 0 && !forcibly) {
            throw new BooksInGenreArePresentException(genreId, booksCount);
        }

        genreRepository.delete(toDelete);

        return toDtoMapper.mapGenreToGenreDto(toDelete);
    }
}