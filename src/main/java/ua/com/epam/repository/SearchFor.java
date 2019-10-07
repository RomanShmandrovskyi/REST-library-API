package ua.com.epam.repository;

import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Author;
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
        List<String> columns = new ArrayList<>(Arrays.asList("firstName", "secondName"));
        String startsWith = buildQueryStartWith("fullName", query);
        String contains = buildQueryContains(Collections.singletonList("fullName"), Collections.singletonList(query));
        String anyMatch, full;

        if (!keywordsToSearch.isEmpty()) {
            anyMatch = buildQueryContains(columns, keywordsToSearch);
            full = String.join(" OR ", startsWith, contains, anyMatch);
        } else {
            full = String.join(" OR ", startsWith, contains);
        }

        return performSearch(full, Author.class);
    }

    public List<Genre> genres(String query, List<String> keywordsToSearch) {
        List<String> columns = Collections.singletonList("genreName");

        String startsWith = buildQueryStartWith("genreName", query);
        String contains = buildQueryContains(columns, Collections.singletonList(query));
        String anyMatch = buildQueryContains(columns, keywordsToSearch);

        String full = String.join(" OR ", startsWith, contains, anyMatch);

        return performSearch(full, Genre.class);
    }

    private String buildQueryStartWith(String columnName, String valueToSearch) {
        String like = " LIKE ";
        String valueStartWith = "\'%s%%\'";
        return "t." + columnName + like + String.format(valueStartWith, valueToSearch);
    }

    private String buildQueryContains(List<String> columnNames, List<String> valuesToSearch) {
        String or = " OR ";
        String like = " LIKE ";
        String valueContains = "\'%%%s%%\'";

        return columnNames.stream()
                .map(c -> valuesToSearch.stream()
                        .map(v -> "t." + c + like + String.format(valueContains, v))
                        .collect(Collectors.joining(or)))
                .collect(Collectors.joining(or));
    }


    private List performSearch(String query, Class clazz) {
        String prefix = "SELECT t FROM " + clazz.getName() + " t WHERE ";
        String queryToExecute = prefix + query;
        return entityManager.createQuery(queryToExecute).getResultList();
    }
}
