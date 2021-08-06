package fi.rikusarlin.reactivedb.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.ConstraintViolation;



public class ExceptionUtils {
	static Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);
	
    public static <T> List<ErrorInfo> listValidationErrors(Set<ConstraintViolation<T>> violations) {
    	List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
        for(ConstraintViolation<T> violation:violations) {
        	ErrorInfo newError = new ErrorInfo();
        	newError.setField(violation.getPropertyPath().toString());
        	newError.setMessage(violation.getMessage());
        	newError.setTimestamp(LocalDateTime.now());
        	errors.add(newError);
        }
        return errors;
    }

    public static <T> void logValidationErrors(Set<ConstraintViolation<T>> violations) {
    	if(violations != null) {
    		for(ConstraintViolation<T> violation:violations) {
    			logger.info(violation.getPropertyPath() + ": " + violation.getMessage());
    		}
    	}
    }
}
