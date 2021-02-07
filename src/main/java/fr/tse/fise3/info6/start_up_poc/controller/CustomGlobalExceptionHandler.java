package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.utils.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																	HttpHeaders headers, 
																	HttpStatus status, 
																	WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        
        List<ValidationError> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {        	
        	errors.add(new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        Collections.sort(errors, Comparator.comparing(ValidationError::getField));

        body.put("errors", errors);
        body.put("path",request.getContextPath());

        return new ResponseEntity<>(body, headers, status);
	}

    @ExceptionHandler(value = AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {

	    Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", new Date());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getContextPath());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public final ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", new Date());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());
        body.put("path", request.getContextPath());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
