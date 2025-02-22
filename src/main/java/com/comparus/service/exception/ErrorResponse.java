package com.comparus.service.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response payload")
public class ErrorResponse {
  @Schema(description = "Error description", example = "Internal server error")
  private String error;

  @Schema(description = "Detailed message", example = "An unexpected error occurred")
  private String message;
}
