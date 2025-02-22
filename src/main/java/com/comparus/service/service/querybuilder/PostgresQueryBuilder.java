package com.comparus.service.service.querybuilder;

import com.comparus.service.config.AppProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PostgresQueryBuilder implements QueryBuilder {
  @Override
  public FilterCondition buildFilterCondition(Map<String, String> filters, AppProperties.Mapping mapping) {
    List<String> conditions = new ArrayList<>();
    List<Object> parameters = new ArrayList<>();

    filters.forEach((field, value) -> {
      String column = switch (field) {
        case "id" -> mapping.getId();
        case "username" -> mapping.getUsername();
        case "name" -> mapping.getName();
        case "surname" -> mapping.getSurname();
        default -> null;
      };

      if (column != null) {
        conditions.add(column + " LIKE ?");
        parameters.add("%" + value + "%");
      }
    });

    return conditions.isEmpty() ?
        new FilterCondition("", new Object[0]) :
        new FilterCondition(" WHERE " + String.join(" AND ", conditions), parameters.toArray());
  }
}