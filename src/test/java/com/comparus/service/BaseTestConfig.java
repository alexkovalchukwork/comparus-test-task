package com.comparus.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
public abstract class BaseTestConfig {

  @Container
  protected static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
      "postgres:13-alpine")
      .withDatabaseName("testdb1")
      .withUsername("testuser")
      .withPassword("testpass")
      .withInitScript("sql/postgres.sql");

  @Container
  protected static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
      .withDatabaseName("testdb2")
      .withUsername("testuser")
      .withPassword("testpass")
      .withInitScript("sql/mysql.sql")
      .withUrlParam("useSSL", "false")
      .withUrlParam("allowPublicKeyRetrieval", "true");


  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("app.data-sources[0].url", postgresContainer::getJdbcUrl);
    registry.add("app.data-sources[0].user", postgresContainer::getUsername);
    registry.add("app.data-sources[0].password", postgresContainer::getPassword);
    registry.add("app.data-sources[0].strategy", () -> "postgres");
    registry.add("app.data-sources[0].name", () -> "data-base-1");
    registry.add("app.data-sources[0].table", () -> "users");
    registry.add("app.data-sources[0].mapping.id", () -> "user_id");
    registry.add("app.data-sources[0].mapping.username", () -> "login");
    registry.add("app.data-sources[0].mapping.name", () -> "first_name");
    registry.add("app.data-sources[0].mapping.surname", () -> "last_name");

    registry.add("app.data-sources[1].url", mysqlContainer::getJdbcUrl);
    registry.add("app.data-sources[1].user", mysqlContainer::getUsername);
    registry.add("app.data-sources[1].password", mysqlContainer::getPassword);
    registry.add("app.data-sources[1].strategy", () -> "mysql");
    registry.add("app.data-sources[1].name", () -> "data-base-2");
    registry.add("app.data-sources[1].table", () -> "user_table");
    registry.add("app.data-sources[1].mapping.id", () -> "ldap_login");
    registry.add("app.data-sources[1].mapping.username", () -> "ldap_login");
    registry.add("app.data-sources[1].mapping.name", () -> "name");
    registry.add("app.data-sources[1].mapping.surname", () -> "surname");
  }
}
