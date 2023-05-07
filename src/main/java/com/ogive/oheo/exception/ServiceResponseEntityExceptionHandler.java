package com.ogive.oheo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ogive.oheo.dto.ErrorResponseDTO;

@ControllerAdvice
public class ServiceResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { EmptyResultDataAccessException.class })
	protected ResponseEntity<Object> handleEmptyResultDataAccessException(RuntimeException ex, WebRequest request) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		errorResponseDTO.setDescription(ex.getMessage());
		return handleExceptionInternal(ex, errorResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = { ValidationException.class })
	protected ResponseEntity<Object> handleValidationException(RuntimeException ex, WebRequest request) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		errorResponseDTO.setDescription(ex.getMessage());
		return handleExceptionInternal(ex, errorResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		/*
		 * List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x
		 * -> x.getDefaultMessage()) .collect(Collectors.toList());
		 */
		
		Map<String, String> map = new HashMap<>();
		ex.getBindingResult().getFieldErrors().stream().forEach(error -> {
			map.put(error.getField(), error.getDefaultMessage());
		});
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		errorResponseDTO.setRejectedFields(map);
		return handleExceptionInternal(ex, errorResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		/*
		 * List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x
		 * -> x.getDefaultMessage()) .collect(Collectors.toList());
		 */
		
		Map<String, String> map = new HashMap<>();
		ex.getBindingResult().getFieldErrors().stream().forEach(error -> {
			map.put(error.getField(), error.getDefaultMessage());
		});
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		errorResponseDTO.setRejectedFields(map);
		return handleExceptionInternal(ex, errorResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
}
