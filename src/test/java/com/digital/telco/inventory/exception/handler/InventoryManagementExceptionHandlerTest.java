package com.digital.telco.inventory.exception.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.digital.telco.utility.exception.BaseException;
import com.digital.telco.utility.exception.ErrorCodes;
import com.digital.telco.utility.exception.ErrorResponseDto;

@RunWith(MockitoJUnitRunner.class)
public class InventoryManagementExceptionHandlerTest {

	private InventoryManagementExceptionHandler exceptionHandler = new InventoryManagementExceptionHandler();

	@Test
	public void testBaseExceptionHandler() throws Exception {

		BaseException baseException = new BaseException("Error condition", ErrorCodes.NOT_FOUND, HttpStatus.NOT_FOUND);
		ResponseEntity<ErrorResponseDto> responseEntity = exceptionHandler.getBaseException(baseException);
		assertNull(responseEntity.getBody().getErrorMap());
	}

	@Test
	public void testHandleMethodArgumentNotValid() throws Exception {
		BindingResult result = Mockito.mock(BindingResult.class);
		MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, result);
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		WebRequest request = null;
		ResponseEntity<Object> responseEntity = exceptionHandler.handleMethodArgumentNotValid(ex, headers, status,
				request);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testExceptionHandler() throws Exception {

		Exception ex = new Exception("Error");
		ResponseEntity<ErrorResponseDto> responseEntity = exceptionHandler.getException(ex);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
}
