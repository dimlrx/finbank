package com.finbank.shared;

import java.time.Instant;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(BadRequestException.class)
	ResponseEntity<ApiError> badRequest(BadRequestException exception) {
		return response(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<ApiError> notFound(NotFoundException exception) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
	ResponseEntity<ApiError> conflict(Exception exception) {
		String message = exception instanceof ConflictException
				? exception.getMessage() : "The request conflicts with existing data";
		return response(HttpStatus.CONFLICT, message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ApiError> invalidRequest(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.findFirst().map(error -> error.getField() + " " + error.getDefaultMessage())
				.orElse("Request validation failed");
		return response(HttpStatus.BAD_REQUEST, message);
	}

	private ResponseEntity<ApiError> response(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new ApiError(status.value(), message, Instant.now()));
	}

	public record ApiError(int status, String message, Instant timestamp) {}
}
