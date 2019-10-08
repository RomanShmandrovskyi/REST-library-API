package ua.com.epam.repository;

import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchFor {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Author> authors(String query, List<String> keywordsToSearch) {
        List<String> columns = new ArrayList<>(Collections.singletonList("fullName"));
        return getList(query, keywordsToSearch, columns, Author.class);
    }

    public List<Genre> genres(String query, List<String> keywordsToSearch) {
        List<String> columns = Collections.singletonList("genreName");
        return getList(query, keywordsToSearch, columns, Genre.class);
    }

    public List<Book> books(String query, List<String> keywordsToSearch) {
        List<String> columns = Collections.singletonList("bookName");
        return getList(query, keywordsToSearch, columns, Book.class);
    }

    private <T> List<T> getList(String query, List<String> keywords, List<String> columns, Class clazz) {
        String contains = buildQueryContains(columns, Collections.singletonList(query));
        String anyMatch, full;

        if (!keywords.isEmpty()) {
            anyMatch = buildQueryContains(columns, keywords);
            full = String.join(" OR ", contains, anyMatch);
        } else {
            full = String.join(" OR ", contains);
        }

        return performSearch(full, clazz);
    }

    private String buildQueryContains(List<String> columnNames, List<String> valuesToSearch) {
        String or = " OR ";
        String like = " LIKE ";
        String valueContains = "'%%%s%%'";

        return columnNames.stream()
                .map(c -> valuesToSearch.stream()
                        .map(v -> "t." + c + like + String.format(valueContains, v.replace("'", "''")))
                        .collect(Collectors.joining(or)))
                .collect(Collectors.joining(or));
    }

    private <T> List<T> performSearch(String query, Class clazz) {
        String prefix = "SELECT t FROM " + clazz.getName() + " t WHERE ";
        String queryToExecute = prefix + query;
        return entityManager.createQuery(queryToExecute, clazz).getResultList();
    }
}