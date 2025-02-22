package com.comparus.service.service.querybuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterCondition {

  private String whereClause;
  private Object[] parameters;
}