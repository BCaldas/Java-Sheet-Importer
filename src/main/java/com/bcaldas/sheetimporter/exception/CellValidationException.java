package com.bcaldas.sheetimporter.exception;

public class CellValidationException extends RuntimeException {

	public CellValidationException(String message) {
        super(message);
    }
    public CellValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
