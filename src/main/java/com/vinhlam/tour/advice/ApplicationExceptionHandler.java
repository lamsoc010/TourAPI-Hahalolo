package com.vinhlam.tour.advice;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vinhlam.tour.exception.PriceTourNotFoundException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}
	
//	Exception input argument
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Map<String, String> handleInvalidArgument(BindException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}
	
	
//	Exception input string convert to date
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Map<String, String> handleInvalidArgument(HttpMessageNotReadableException ex) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("messageError", ex.getMessage());
		return errorMap;
	}
	
//	Exception price tour not found
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(PriceTourNotFoundException.class)
	public Map<String, String> handleBusinessException(PriceTourNotFoundException ex) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("message", ex.getMessage());
		return errorMap;
	}
	
	
}
