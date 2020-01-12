package com.bcaldas.sheetimporter.exception;

public class SheetValidationException extends RuntimeException {

	public SheetValidationException(String message) {
        super(message);
    }
    public SheetValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
