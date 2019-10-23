package ua.com.epam.repository;

import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchFor {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Author> authors(String query, List<String> keywordsToSearch) {
        return getList(query, keywordsToSearch, Collections.singletonList("fullName"), Author.class);
    }

    public List<Genre> genres(String query, List<String> keywordsToSearch) {
        return getList(query, keywordsToSearch, Collections.singletonList("genreName"), Genre.class);
    }

    public List<Book> books(String query, List<String> keywordsToSearch) {
        return getList(query, keywordsToSearch, Collections.singletonList("bookName"), Book.class);
    }

    private <T> List<T> getList(String query, List<String> keywords, List<String> columns, Class<T> clazz) {
        String contains = buildSearchQuery(columns, Collections.singletonList(query));
        String anyMatch, full;

        if (!keywords.isEmpty()) {
            anyMatch = buildSearchQuery(columns, keywords);
            full = String.join(" OR ", contains, anyMatch);
        } else {
            full = String.join(" OR ", contains);
        }

        return performSearch(full, clazz);
    }

    private String buildSearchQuery(List<String> columnNames, List<String> valuesToSearch) {
        String or = " OR ";
        String like = " LIKE ";
        String valueContains = "'%%%s%%'";

        return columnNames.stream()
                .map(c -> valuesToSearch.stream()
                        .map(v -> "t." + c + like + String.format(valueContains, v.replace("'", "''")))
                        .collect(Collectors.joining(or)))
                .collect(Collectors.joining(or));
    }

    private <T> List<T> performSearch(String query, Class<T> clazz) {
        String prefix = "SELECT t FROM " + clazz.getName() + " t WHERE ";
        String queryToExecute = prefix + query;
        return entityManager.createQuery(queryToExecute, clazz).getResultList();
    }
}