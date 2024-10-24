package org.product_service.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.product_service.exception.payload.ExceptionMsg;
import org.product_service.exception.wrapper.CategoryNotFoundException;
import org.product_service.exception.wrapper.ProductNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

	@ExceptionHandler(value = {
			MethodArgumentNotValidException.class,
			HttpMessageNotReadableException.class,
	})
	public ResponseEntity<ExceptionMsg> handleValidationErrors(final Exception e) {
		log.error("Validation error occurred", e);

		final var badRequest = HttpStatus.BAD_REQUEST;
		String errorMessage = e instanceof MethodArgumentNotValidException
				? ((MethodArgumentNotValidException) e).getBindingResult().getFieldError() != null
				? ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage()
				: "Invalid input"
				: e.getMessage();

		return new ResponseEntity<>(
				ExceptionMsg.builder()
						.msg(errorMessage)
						.httpStatus(badRequest)
						.timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
						.build(), badRequest
		);
	}

	@ExceptionHandler(value = {
			CategoryNotFoundException.class,
			ProductNotFoundException.class,
	})
	public ResponseEntity<ExceptionMsg> handleResourceNotFoundException(final RuntimeException e) {
		log.error("Resource not found", e);

		final var notFound = HttpStatus.NOT_FOUND;

		return new ResponseEntity<>(
				ExceptionMsg.builder()
						.msg(e.getMessage())
						.httpStatus(notFound)
						.timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
						.build(), notFound
		);
	}
}






