package ua.com.api.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Author;
import ua.com.api.entity.dto.author.AuthorDto;
import ua.com.api.exception.entity.author.AuthorAlreadyExistsException;
import ua.com.api.exception.entity.author.AuthorNotFoundException;
import ua.com.api.exception.entity.author.BooksInAuthorArePresentException;
import ua.com.api.exception.entity.book.BookNotFoundException;
import ua.com.api.exception.entity.genre.GenreNotFoundException;
import ua.com.api.exception.entity.search.SearchQueryIsBlankException;
import ua.com.api.exception.entity.search.SearchQueryIsTooShortException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService extends BaseService {

    private List<AuthorDto> mapToDto(List<Author> authors) {
        return authors.stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public AuthorDto findAuthor(long authorId) {
        Author toGet = authorRepository.getOneByAuthorId(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public AuthorDto findAuthorOfBook(long bookId) {
        if (!bookRepository.existsByBookId(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Author toGet = authorRepository.getAuthorOfBook(bookId);

        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    // define sort by params

    public List<AuthorDto> findAllAuthors(String sortBy, String order, int page, int size, boolean pageable) {
        String sortParam = convertAndValidateSortBy(sortBy, Author.class);
        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParam);

        List<Author> authors;

        if (!pageable) {
            authors = authorRepository.findAll(sorter);
        } else {
            authors = authorRepository.getAllAuthors(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(authors);
    }

    public List<AuthorDto> searchForExistedAuthors(String searchQuery) {
        List<Author> result = new ArrayList<>();

        searchQuery = searchQuery.trim();

        if (searchQuery.isEmpty()) throw new SearchQueryIsBlankException();
        else if (searchQuery.length() <= 2) throw new SearchQueryIsTooShortException(searchQuery, 3);

        List<String> splitQuery = Arrays.asList(searchQuery.split(" "));
        List<Author> searched = searchFor.authors(searchQuery, splitQuery);

        if (searched.isEmpty()) return new ArrayList<>();

        for (String word : splitQuery) {
            searched.stream()
                    .filter(a -> a.getFirstName().startsWith(word))
                    .forEach(result::add);

            searched.stream()
                    .filter(a -> a.getSecondName().startsWith(word))
                    .forEach(result::add);

            searched.removeAll(result);
        }

        result.addAll(searched);

        return mapToDto(result.stream()
                .limit(5)
                .collect(Collectors.toList()));
    }

    public List<AuthorDto> findAllAuthorsInGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        String sortParam = convertAndValidateSortBy(sortBy, Author.class);

        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParam);

        List<Author> authors;

        if (!pageable) {
            authors = authorRepository.findAll(sorter);
        } else {
            authors = authorRepository.getAllAuthorsInGenre(genreId, PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(authors);
    }

    public AuthorDto addNewAuthor(AuthorDto author) {
        if (authorRepository.existsByAuthorId(author.getAuthorId())) {
            throw new AuthorAlreadyExistsException();
        }

        Author toPost = toModelMapper.mapAuthorDtoToAuthor(author);
        Author response = authorRepository.save(toPost);

        return toDtoMapper.mapAuthorToAuthorDto(response);
    }

    public AuthorDto updateExistedAuthor(AuthorDto authorDto) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorDto.getAuthorId());

        if (opt.isEmpty()) {
            throw new AuthorNotFoundException(authorDto.getAuthorId());
        }

        Author proxy = opt.get();

        proxy.setFirstName(authorDto.getAuthorName().getFirst());
        proxy.setSecondName(authorDto.getAuthorName().getSecond());
        proxy.setDescription(authorDto.getAuthorDescription());
        proxy.setNationality(authorDto.getNationality());
        proxy.setBirthDate(authorDto.getBirth().getDate());
        proxy.setBirthCity(authorDto.getBirth().getCity());
        proxy.setBirthCountry(authorDto.getBirth().getCountry());

        Author updated = authorRepository.save(proxy);

        return toDtoMapper.mapAuthorToAuthorDto(updated);
    }

    public void deleteExistedAuthor(long authorId, boolean forcibly) {
        Author toDelete = authorRepository.getOneByAuthorId(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        long booksCount = bookRepository.getAllBooksOfAuthorCount(authorId);

        if (booksCount > 0 && !forcibly) {
            throw new BooksInAuthorArePresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
    }
}