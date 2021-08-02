package fi.rikusarlin.reactivedb.testdata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import fi.rikusarlin.reactivedb.model.Gender;
import fi.rikusarlin.reactivedb.model.Person;


public class PersonData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static Person getPerson1() {
		return Person.builder()
				.id(UUID.randomUUID())
				.personNumber("010170-901K")
				.firstName("Rauli")
				.lastName("Wnape")
				.birthDate(LocalDate.parse("01.01.1970", formatter))
				.gender(Gender.MALE)
				.email("rauliwnape2@hotmail.com")
				.build();
	}
	
	public static Person getPerson2() {
		return Person.builder()
    		.id(UUID.randomUUID())
    		.personNumber("010170-902L")
    		.firstName("Marke")
    		.lastName("Peerakpe")
    		.birthDate(LocalDate.parse("01.01.1970", formatter))
    		.gender(Gender.FEMALE)
    		.email("marke.peerakpe@yahoo.com")
    		.build();
	}

	public static Person getPerson3() {
		return Person.builder()
    		.id(UUID.randomUUID())
    		.personNumber("010170-903M")
    		.firstName("Walter")
    		.lastName("Nutbekk")
    		.birthDate(LocalDate.parse("01.01.1970", formatter))
    		.gender(Gender.MALE)
    		.email("walter.nutbekk@welho.com")
    		.build();
	}
	
	public static Person getPerson4() {
		return Person.builder()
    		.id(UUID.randomUUID())
    		.personNumber("010170-904N")
    		.firstName("Suvi-Tuulia")
    		.lastName("Retsetenpe")
    		.birthDate(LocalDate.parse("01.01.1970", formatter))
    		.gender(Gender.FEMALE)
    		.email("suvi-tuuli.retsenape@gmail.com")
    		.build();
	}
}
