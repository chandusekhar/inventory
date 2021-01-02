package com.digital.telco.inventory.exception.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.digital.telco.utility.common.Utils;
import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.utility.exception.ErrorResponseDto;

@ControllerAdvice
public class InventoryManagementExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponseDto> getBaseException(BaseException ex) {
		ErrorResponseDto errorResponse = new ErrorResponseDto();
		errorResponse.setError(ex.getMessage());
		errorResponse.setTimeStampMillis(Utils.getCurrentTimeInMiliSec());
		errorResponse.setStatus(ex.getCode());
		return new ResponseEntity<>(errorResponse, ex.getHttpStatus());

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> value = new HashMap<>();
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		value.put("status", status.value());
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		value.put("errors", errors);
		errorResponseDto.setErrorMap(value);
		errorResponseDto.setTimeStampMillis(Utils.getCurrentTimeInMiliSec());
		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> getException(Exception ex) {
		ErrorResponseDto errorResponse = new ErrorResponseDto();
		errorResponse.setError(ex.getMessage());
		errorResponse.setTimeStampMillis(Utils.getCurrentTimeInMiliSec());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}