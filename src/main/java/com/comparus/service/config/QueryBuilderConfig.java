package com.comparus.service.config;

import com.comparus.service.service.querybuilder.MySqlQueryBuilder;
import com.comparus.service.service.querybuilder.PostgresQueryBuilder;
import com.comparus.service.service.querybuilder.QueryBuilder;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryBuilderConfig {

  @Bean
  public Map<String, QueryBuilder> queryBuilders(
      PostgresQueryBuilder postgresQueryBuilder,
      MySqlQueryBuilder mySqlQueryBuilder
  ) {
    return Map.of(
        "postgres", postgresQueryBuilder,
        "mysql", mySqlQueryBuilder
    );
  }
}