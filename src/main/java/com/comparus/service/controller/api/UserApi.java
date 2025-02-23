package com.comparus.service.controller.api;

import com.comparus.service.dto.User;
import com.comparus.service.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
  @Operation(
      summary = "Retrieve filtered users",
      description = "Get users with optional filters using 'filter.' prefix",
      parameters = {
          @Parameter(
              name = "filter.id",
              description = "Id filter",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "string")
          ),
          @Parameter(
              name = "filter.username",
              description = "Username filter",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "string")
          ),
          @Parameter(
              name = "filter.name",
              description = "Name filter",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "string")
          ),
          @Parameter(
              name = "filter.surname",
              description = "Surname filter",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "string")
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved users",
              content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid request parameters",
              content = @Content(
                  schema = @Schema(implementation = ErrorResponse.class),
                  examples = @ExampleObject(
                      value = "{\"error\":\"Invalid filter parameter\", \"message\":\"Your provided filters are not valid\"}"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Data source access error",
              content = @Content(
                  schema = @Schema(implementation = ErrorResponse.class),
                  examples = @ExampleObject(
                      value = "{\"error\":\"Data source error: data-base-1\", \"message\":\"Connection failed\"}"
                  )
              )
          )
      }
  )
  @GetMapping("/users")
  ResponseEntity<List<User>> getUsers(
      @Parameter(hidden = true)
      @RequestParam MultiValueMap<String, String> allParams
  );
}
