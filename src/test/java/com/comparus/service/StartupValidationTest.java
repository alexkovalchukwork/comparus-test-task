package com.comparus.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comparus.service.config.DataSourceRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StartupValidationTest extends BaseTestConfig {

  @Autowired
  private DataSourceRegistry dataSourceRegistry;

  @Test
  void testDataSourceInitialization() {
    assertAll(
        () -> assertNotNull(dataSourceRegistry.getJdbcTemplate("data-base-1"),
            "Postgres JdbcTemplate should be initialized"),
        () -> assertNotNull(dataSourceRegistry.getJdbcTemplate("data-base-2"),
            "MySQL JdbcTemplate should be initialized"),
        () -> assertNotNull(dataSourceRegistry.getQueryBuilder("data-base-1"),
            "Postgres QueryBuilder should be initialized"),
        () -> assertNotNull(dataSourceRegistry.getQueryBuilder("data-base-2"),
            "MySQL QueryBuilder should be initialized")
    );
  }
}