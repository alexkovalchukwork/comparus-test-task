package com.comparus.service.exception;

import lombok.Getter;

@Getter
public class DataSourceAccessException extends RuntimeException {
  private final String dataSourceName;

  public DataSourceAccessException(String dataSourceName, String message, Throwable cause) {
    super(message, cause);
    this.dataSourceName = dataSourceName;
  }
}
