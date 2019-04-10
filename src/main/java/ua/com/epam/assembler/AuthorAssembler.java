package ua.com.epam.assembler;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.service.AuthorService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorAssembler {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private Gson gson;

    public String getAllAuthorsSortedBy(String sortBy) {
        List<AuthorGetDto> dtos = authorService.getAllAuthors();
        List<Object> dtosJson = new JSONArray(dtos).toList();
        return sortJsonsByKey(dtosJson, sortBy);
    }

    public String getAuthorsWithFilters(Map params, String sortBy, int count) {
        params.remove("count");
        params.remove("sortBy");

        //1. get all
        //2. filter by JsonPath
        //3. sortBy attr
        //4. limit by count

        List<AuthorGetDto> dtos = authorService.getAllAuthors();

        String sorted = sortJsonsByKey(new JSONArray(dtos).toList(), sortBy);

        if (params.size() == 0) {
            return sorted;
        }

        return filterByJsonPath(sorted, parseParamMapToJsonPath(params));
    }

    private Object getEntityValueByKey(Object entity, String key) {
        return JsonPath.read(gson.toJson(entity), "$." + key);
    }

    private String sortJsonsByKey(List<Object> jsons, String key) {
        Map<Object, Object> sorted = new TreeMap<>();
        for (Object single : jsons) {
            Object keyValue = getEntityValueByKey(single, key);
            sorted.put(keyValue, single);
        }
        return new JSONArray(sorted.values()).toString();
    }

    private String parseParamMapToJsonPath(Map<String, String> params) {
        String prefix = "$.[?", beforeKey = "@.", postfix = "]";
        StringBuilder path = new StringBuilder();
        path.append(prefix);
        List<String> andFilters = new ArrayList<>();
        for (Map.Entry pair : params.entrySet()) {
            String key = pair.getKey().toString();
            List<String> orFilters = new ArrayList<>();
            for (String value : pair.getValue().toString().split(",")) {
                orFilters.add(beforeKey + key + "==" + "'" + value + "'");
            }
            andFilters.add(orFilters.stream().collect(Collectors.joining("||", "(", ")")));
        }
        path.append(andFilters.stream().collect(Collectors.joining("&&", "(", ")"))).append(postfix);
        return path.toString();
    }

    private String filterByJsonPath(String sourceToFilter, String filter) {
        return JsonPath.read(sourceToFilter, filter).toString();
    }
}
