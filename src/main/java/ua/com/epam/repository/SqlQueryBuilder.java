package ua.com.epam.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SqlQueryBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> List<T> getFilteredEntities(
            Map<String, String> params, String orderBy, String orderType, Class<T> clazz) {
        StringBuilder query = new StringBuilder();

        String queryPrefix = "SELECT t FROM %s t ";
        String sorting = " ORDER BY t.%s ";

        query.append(String.format(queryPrefix, clazz.getName()));

        if (params.size() > 0) {
            query.append(convertParamsMapToSqlQuery(params));
        }

        query.append(String.format(sorting, orderBy))
                .append(orderType);

        return (List<T>) entityManager.createQuery(query.toString()).getResultList();
    }

    private String convertParamsMapToSqlQuery(Map<String, String> params) {
        String prefix = "WHERE ";
        String beforeValue = "t.";

        List<String> and = new ArrayList<>();
        StringBuilder filter = new StringBuilder();

        filter.append(prefix);

        for (Map.Entry<String, String> pair : params.entrySet()) {
            String attr = pair.getKey();
            List<String> or = new ArrayList<>();
            for (String value : pair.getValue().split(",")) {
                or.add(beforeValue + attr + "=" + "'" + value + "'");
            }
            and.add(or.stream().collect(Collectors.joining(" OR ", "(", ")")));
        }

        filter.append(String.join(" AND ", and));

        return filter.toString();
    }
}
