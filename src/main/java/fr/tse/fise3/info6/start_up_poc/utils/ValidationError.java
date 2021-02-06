package fr.tse.fise3.info6.start_up_poc.utils;

import lombok.Data;

@Data
public class ValidationError {

	private String field;
	
	private String message;
	
	public ValidationError(String field, String message) {
		this.field = field;
		this.message = message;
	}
}
