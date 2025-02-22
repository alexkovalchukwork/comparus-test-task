package com.comparus.service.service;


import com.comparus.service.config.AppProperties;
import com.comparus.service.config.DataSourceRegistry;
import com.comparus.service.dto.User;
import com.comparus.service.exception.DataSourceAccessException;
import com.comparus.service.service.querybuilder.FilterCondition;
import com.comparus.service.service.querybuilder.QueryBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAggregationService {

  private static final Set<String> ALLOWED_FILTERS = Set.of("id", "username", "name", "surname");
  private final AppProperties appProperties;
  private final DataSourceRegistry dataSourceRegistry;

  public List<User> getUsers(Map<String, String> filters) {
    Map<String, String> validatedFilters = filters.entrySet().stream()
        .filter(entry -> ALLOWED_FILTERS.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return appProperties.getDataSources().parallelStream()
        .flatMap(config -> fetchUsersFromDataSource(config, validatedFilters).stream())
        .toList();
  }

  private List<User> fetchUsersFromDataSource(AppProperties.Config config,
      Map<String, String> filters) {
    try {
      JdbcTemplate jdbcTemplate = dataSourceRegistry.getJdbcTemplate(config.getName());
      QueryBuilder queryBuilder = dataSourceRegistry.getQueryBuilder(config.getName());

      String baseQuery = String.format(
          "SELECT %s AS id, %s AS username, %s AS name, %s AS surname FROM %s",
          config.getMapping().getId(), config.getMapping().getUsername(),
          config.getMapping().getName(), config.getMapping().getSurname(), config.getTable());

      FilterCondition filterCondition = queryBuilder.buildFilterCondition(filters,
          config.getMapping());
      String fullQuery = baseQuery + filterCondition.getWhereClause();
      return jdbcTemplate.query(
          fullQuery,
          (rs, rowNum) -> new User(
              rs.getString("id"),
              rs.getString("username"),
              rs.getString("name"),
              rs.getString("surname")
          ),
          filterCondition.getParameters()
      );
    } catch (Exception e) {
      log.error("Error fetching users from {}: {}", config.getName(), e.getMessage(), e);
      throw new DataSourceAccessException(
          config.getName(),
          "Failed to fetch users from data source: " + config.getName(),
          e
      );
    }
  }
}