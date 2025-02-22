package com.comparus.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comparus.service.config.AppProperties;
import com.comparus.service.dto.User;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserApiTest extends BaseTestConfig {

  @Autowired
  private AppProperties appProperties;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void testGetAllUsers() {
    ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);
    assertResponseValid(response, 2, "Should return two users from both databases");
  }

  @Test
  void testFilterByUsername() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.username=john_doe", User[].class);
    assertResponseValid(response, 1, "Should return one user matching username filter");
    assertEquals("john_doe", response.getBody()[0].getUsername());
  }

  @Test
  void testInvalidFilterIgnored() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.invalid=abc", User[].class);
    assertResponseValid(response, 2, "Invalid filter should be ignored and return all users");
  }

  @Test
  void testFilterNoResults() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.username=nonexistent", User[].class);
    assertResponseValid(response, 0, "No results for nonexistent username");
  }

  @Test
  void testFilterBySurname() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.surname=Doe", User[].class);
    assertResponseValid(response, 2, "Filtering by surname 'Doe' should return both users");
  }

  @Test
  void testFilterByName() {
    ResponseEntity<User[]> responseJohn = restTemplate.getForEntity(
        "/users?filter.name=John", User[].class);
    assertResponseValid(responseJohn, 1, "Filtering by name 'John' should return one user");
    ResponseEntity<User[]> responseJane = restTemplate.getForEntity(
        "/users?filter.name=jane_doe", User[].class);
    assertResponseValid(responseJane, 1, "Filtering by name 'jane_doe' should return one user");
  }

  @Test
  void testMultipleFiltersMatching() {
    ResponseEntity<User[]> responsePostgres = restTemplate.getForEntity(
        "/users?filter.username=john_doe&filter.surname=Doe", User[].class);
    assertResponseValid(responsePostgres, 1,
        "Combined filters should return one matching Postgres user");

    ResponseEntity<User[]> responseMysql = restTemplate.getForEntity(
        "/users?filter.username=ldap_login&filter.surname=Doe", User[].class);
    assertResponseValid(responseMysql, 1, "Combined filters should return one matching MySQL user");
  }

  @Test
  void testMultipleFiltersNoMatch() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.username=john_doe&filter.name=jane_doe", User[].class);
    assertResponseValid(response, 0,
        "Combined filters with conflicting values should return no users");
  }

  @Test
  void testEmptyFilterValue() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.username=", User[].class);
    assertResponseValid(response, 2, "Empty filter value should return all users");
  }

  @Test
  void testMultipleValuesForSameParameter() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.username=john_doe&filter.username=ignored_value", User[].class);
    assertResponseValid(response, 1, "Only the first value should be used for filter.username");
    assertEquals("john_doe", response.getBody()[0].getUsername());
  }

  @Test
  void testValidAndInvalidFilter() {
    ResponseEntity<User[]> response = restTemplate.getForEntity(
        "/users?filter.username=john_doe&filter.extra=shouldBeIgnored", User[].class);
    assertResponseValid(response, 1, "Valid filter should be applied and invalid filter ignored");
    assertEquals("john_doe", response.getBody()[0].getUsername());
  }

  @Test
  void testDataSourceAccessException() {
    String correctTable = appProperties.getDataSources().get(1).getTable();
    appProperties.getDataSources().get(1).setTable("nonexistent_table");

    ResponseEntity<Map> response = restTemplate.getForEntity("/users", Map.class);
    assertAll(
        () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
            "HTTP status should be INTERNAL_SERVER_ERROR"),
        () -> {
          Map<?, ?> body = response.getBody();
          assertNotNull(body, "Response body should not be null");
          assertEquals("Data source error: data-base-2", body.get("error"),
              "Error message should indicate datasource failure");
          assertNotNull(body.get("message"), "Error message details should be provided");
        }
    );
    appProperties.getDataSources().get(1).setTable(correctTable);
  }


  private void assertResponseValid(ResponseEntity<User[]> response, int expectedCount,
      String message) {
    assertAll(
        () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK"),
        () -> assertNotNull(response.getBody(), "Response body should not be null"),
        () -> assertEquals(expectedCount, response.getBody().length, message)
    );
  }
}
