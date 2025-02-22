package com.comparus.service.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  @NotEmpty(message = "At least one data source must be configured")
  private List<@Valid Config> dataSources;

  @Data
  public static class Config {

    @NotBlank(message = "Data source name must not be blank")
    private String name;

    @Pattern(regexp = "^(postgres|mysql|oracle|mssql|mariadb)?$",
        message = "Invalid database strategy")
    private String strategy;

    @NotBlank(message = "JDBC URL must not be blank")
    @Pattern(regexp = "^jdbc:\\w+://.+$",
        message = "Invalid JDBC URL format")
    private String url;

    @NotBlank(message = "Table name must not be blank")
    private String table;

    @NotBlank(message = "Database user must not be blank")
    private String user;

    @NotBlank(message = "Database password must not be blank")
    private String password;

    @Valid
    @NotNull(message = "Field mapping must be specified")
    private Mapping mapping;
  }

  @Data
  public static class Mapping {

    @NotBlank(message = "ID column mapping must not be blank")
    private String id;

    @NotBlank(message = "Username column mapping must not be blank")
    private String username;

    @NotBlank(message = "Name column mapping must not be blank")
    private String name;

    @NotBlank(message = "Surname column mapping must not be blank")
    private String surname;
  }
}