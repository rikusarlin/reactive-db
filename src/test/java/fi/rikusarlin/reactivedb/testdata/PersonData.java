package fi.rikusarlin.reactivedb.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import fi.rikusarlin.reactivedb.model.Gender;
import fi.rikusarlin.reactivedb.model.Person;


public class PersonData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static String userName = "User";

	public static Person getPerson1() {
		Person p = new Person();
		p.setId(UUID.randomUUID());
		p.setPersonNumber("010170-901K");
		p.setFirstName("Rauli");
		p.setLastName("Wnape");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.MALE);
		p.setEmail("rauliwnape2@hotmail.com");
		p.setCreated(LocalDateTime.now());
		p.setCreator(userName);
		p.setModified(LocalDateTime.now());
		p.setModifier(userName);
		return p;
	}
	
	public static Person getPerson2() {
		Person p = new Person();
		p.setId(UUID.randomUUID());
		p.setPersonNumber("010170-902L");
		p.setFirstName("Marke");
		p.setLastName("Peerakpe");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.FEMALE);
		p.setEmail("marke.peerakpe@yahoo.com");
		p.setCreated(LocalDateTime.now());
		p.setCreator(userName);
		p.setModified(LocalDateTime.now());
		p.setModifier(userName);
    	return p;
	}

	public static Person getPerson3() {
		Person p = new Person();
		p.setId(UUID.randomUUID());
		p.setPersonNumber("010170-903M");
		p.setFirstName("Walter");
		p.setLastName("Nutbekk");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.MALE);
		p.setEmail("walter.nutbekk@welho.com");
		p.setCreated(LocalDateTime.now());
		p.setCreator(userName);
		p.setModified(LocalDateTime.now());
		p.setModifier(userName);
		return p;
	}
	
	public static Person getPerson4() {
		Person p = new Person();
		p.setId(UUID.randomUUID());
		p.setPersonNumber("010170-904N");
		p.setFirstName("Suvi-Tuulia");
		p.setLastName("Retsetenpe");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.FEMALE);
		p.setEmail("suvi-tuuli.retsenape@gmail.com");
		p.setCreated(LocalDateTime.now());
		p.setCreator(userName);
		p.setModified(LocalDateTime.now());
		p.setModifier(userName);
		return p;
	}
}
