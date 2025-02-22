package com.comparus.service.service.querybuilder;

import com.comparus.service.config.AppProperties;
import java.util.Map;

public interface QueryBuilder {
  FilterCondition buildFilterCondition(Map<String, String> filters, AppProperties.Mapping mapping);
}
