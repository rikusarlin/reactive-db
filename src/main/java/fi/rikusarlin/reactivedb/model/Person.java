package fi.rikusarlin.reactivedb.model;

import java.time.LocalDate;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A natural person residing in Finland
 */

@Builder
@Getter
@Setter
public class Person extends EntityBase{
    @Column(value = "personNumber")	
	private String personNumber;

    @Column(value = "firstName")	
	private String firstName;

    @Column(value = "lastName")	
	private String lastName;

    @Column(value = "birthDate")	
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate birthDate;

    @Column(value = "gender")	
	private Gender gender;

    @Column(value = "email")	
	private String email;
}

