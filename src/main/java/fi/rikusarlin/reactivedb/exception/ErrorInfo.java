package fi.rikusarlin.reactivedb.exception;

import java.time.LocalDateTime;

public class ErrorInfo {
	
	public ErrorInfo() {
		super();
	}

	public ErrorInfo(String field, String message, LocalDateTime timestamp) {
		super();
		this.timestamp = timestamp;
		this.field = field;
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private LocalDateTime timestamp;
	
	private String field;

	private String message;

}
