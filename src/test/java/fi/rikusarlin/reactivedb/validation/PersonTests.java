package fi.rikusarlin.reactivedb.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.rikusarlin.reactivedb.exception.ExceptionUtils;
import fi.rikusarlin.reactivedb.model.Gender;
import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.testdata.PersonData;

public class PersonTests 
{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	Set<ConstraintViolation<Person>> violations;
	ExceptionUtils exeptionUtils = new ExceptionUtils();
	
	private List<String> getMessages(Set<ConstraintViolation<Person>> violations){
		return violations
    		.stream()
    		.map(v -> v.getPropertyPath() + ": "+ v.getMessage())
    		.collect(Collectors.toList());
	}
	
    @Test
    public void testValidPersonNumbers()
    {
    	Person p1 = PersonData.getPerson1();
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010170+904N");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010100A900F");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010170-901K");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidControlChar()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010100A900G");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(getMessages(violations).contains("personNumber: invalid person number '010100A900G'"));
    }
    
    @Test
    public void testTooShortPersonNumber()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010100A900");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: size must be between 11 and 11"));
        Assertions.assertTrue(messages.contains("personNumber: invalid person number '010100A900'"));
    }

    @Test
    public void testInvalidDateInPersonNumber()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setPersonNumber("31YY70-904N");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: invalid person number '31YY70-904N'"));
    }

    @Test
    public void testInvalidCenturyMarker()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010170C904N");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: invalid person number '010170C904N'"));
    }

    @Test
    public void testInvalidIndividualNumber()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010170-NNNN");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: invalid person number '010170-NNNN'"));
    }

    @Test
    public void testMissingPersonNumber()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setPersonNumber(null);
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        // Is both invalid and missing (several validators)        
        Assertions.assertTrue(violations.size() == 2);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: Person number can not be empty"));
        Assertions.assertTrue(messages.contains("personNumber: invalid person number ''"));
    }

    @Test
    public void testMissingBirthDate()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setBirthDate(null);
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("birthDate: Birthdate can not be empty"));
    }
    
    @Test
    public void testMissingFirstName_isOk()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setFirstName(null);
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    }

    
    @Test
    public void testMissingLastName()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setLastName(null);
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("lastName: Last name can not be empty"));
    }

    @Test
    public void testTooLongFirstName()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setFirstName("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("firstName: size must be between 0 and 20"));
    }

    @Test
    public void testTooLongLastName()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setLastName("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("lastName: size must be between 0 and 40"));
    }

    @Test
    public void testNoGender()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setGender(null);
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testNoEmail()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setEmail(null);
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail()
    {
    	Person p1 = PersonData.getPerson1();
    	p1.setEmail("username@yahoo..com");
    	violations = validator.validate(p1);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("email: invalid emailAddress 'username@yahoo..com'"));
    }

    @Test
    public void testInvalidGender()
    {
    	Person p1 = PersonData.getPerson1();
    	
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
        	p1.setGender(Gender.fromValue("ThereIsNoSuchSex"));
        });
        Assertions.assertTrue(exception.getMessage().contains("Unexpected value 'ThereIsNoSuchSex'"));
    	
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
        ExceptionUtils.logValidationErrors(violations);
    }

}
