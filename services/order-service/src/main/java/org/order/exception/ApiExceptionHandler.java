package org.order.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.order.exception.wrapper.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.order.exception.payload.ExceptionMsg;
import org.order.exception.wrapper.CartNotFoundException;
import org.order.exception.wrapper.OrderNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {
	
	@ExceptionHandler(value = {
		MethodArgumentNotValidException.class,
		HttpMessageNotReadableException.class,
			BusinessException.class
	})
	public <T extends BindException> ResponseEntity<ExceptionMsg> handleValidationException(final T e) {

		log.info("Handling validation exception: {}", e.getMessage());

		HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		String errorMessage = e.getBindingResult().getFieldError() != null
				? e.getBindingResult().getFieldError().getDefaultMessage()
				: "Validation error";

		return new ResponseEntity<>(
				ExceptionMsg.builder()
						.msg("Validation Error: " + errorMessage)
						.httpStatus(badRequest)
						.timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
						.build(),
				badRequest
		);
	}
	
	@ExceptionHandler(value = {
		CartNotFoundException.class,
		OrderNotFoundException.class,
		IllegalStateException.class,
	})
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleApiRequestException(final T e) {

		log.info("Handling API request exception: {}", e.getMessage());

		HttpStatus notFound = HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(
				ExceptionMsg.builder()
						.msg("Error: " + e.getMessage())
						.httpStatus(notFound)
						.timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
						.build(),
				notFound
		);
	}
	
	
	
}










