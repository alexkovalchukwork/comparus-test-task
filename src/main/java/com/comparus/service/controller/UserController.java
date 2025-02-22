package com.comparus.service.controller;

import com.comparus.service.controller.api.UserApi;
import com.comparus.service.dto.User;
import com.comparus.service.service.UserAggregationService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  public static final String FILTER_PREFIX = "filter.";
  private final UserAggregationService userAggregationService;

  @Override
  public ResponseEntity<List<User>> getUsers(MultiValueMap<String, String> allParams) {
    Map<String, String> filters = extractFilters(allParams);
    List<User> users = userAggregationService.getUsers(filters);
    return ResponseEntity.ok(users);
  }

  private Map<String, String> extractFilters(MultiValueMap<String, String> allParams) {
    return allParams.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(FILTER_PREFIX))
        .collect(Collectors.toMap(
            entry -> entry.getKey().substring(7),
            entry -> entry.getValue().get(0)
        ));
  }
}