package fi.rikusarlin.reactivedb.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

import fi.rikusarlin.reactivedb.model.Person;

public class InvalidPersonException extends Exception{
	
	private static final long serialVersionUID = -24961230350794465L;

	private Set<ConstraintViolation<Person>> violations;
	
	public InvalidPersonException() {
	}

	public InvalidPersonException(Set<ConstraintViolation<Person>> viols) {
		this.violations = viols;
	}

	public Set<ConstraintViolation<Person>> getViolations() {
		return violations;
	}

	public void setViolations(Set<ConstraintViolation<Person>> violations) {
		this.violations = violations;
	}


}
