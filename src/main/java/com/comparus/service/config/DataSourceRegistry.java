package com.comparus.service.config;

import com.comparus.service.service.querybuilder.QueryBuilder;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSourceRegistry {

  private static final Map<String, String> DRIVER_MAPPING = Map.ofEntries(
      Map.entry("postgres", "org.postgresql.Driver"),
      Map.entry("mysql", "com.mysql.cj.jdbc.Driver")
  );

  private final AppProperties appProperties;

  private final Map<String, QueryBuilder> queryBuilders;
  private Map<String, JdbcTemplate> jdbcTemplateMap;
  private Map<String, QueryBuilder> dataSourceQueryBuilders;


  @PostConstruct
  public void init() {
    jdbcTemplateMap = new HashMap<>();
    dataSourceQueryBuilders = new HashMap<>();

    for (AppProperties.Config config : appProperties.getDataSources()) {
      DataSource dataSource = DataSourceBuilder.create()
          .driverClassName(DRIVER_MAPPING.get(config.getStrategy().toLowerCase()))
          .url(config.getUrl())
          .username(config.getUser())
          .password(config.getPassword())
          .build();

      jdbcTemplateMap.put(config.getName(), new JdbcTemplate(dataSource));
      dataSourceQueryBuilders.put(config.getName(),
          queryBuilders.get(config.getStrategy().toLowerCase()));

    }
  }

  public JdbcTemplate getJdbcTemplate(String name) {
    return jdbcTemplateMap.get(name);
  }

  public QueryBuilder getQueryBuilder(String dataSourceName) {
    return dataSourceQueryBuilders.get(dataSourceName);
  }
}
