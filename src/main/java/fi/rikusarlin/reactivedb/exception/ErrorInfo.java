package fi.rikusarlin.reactivedb.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorInfo {
	
	private LocalDateTime timestamp;
	
	private String field;

	private String message;

}
