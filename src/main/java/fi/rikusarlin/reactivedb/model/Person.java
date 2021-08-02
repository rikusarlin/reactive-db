package fi.rikusarlin.reactivedb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import fi.rikusarlin.reactivedb.validation.ValidEmailAddress;
import fi.rikusarlin.reactivedb.validation.ValidPersonNumber;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A natural person residing in Finland
 */

@Getter
@Setter
public class Person extends EntityBase{
    @ValidPersonNumber
	@NotNull(message = "Person number can not be empty")
	@Size(min = 11, max = 11)
    @Column(value = "personNumber")	
	private String personNumber;

    @Column(value = "firstName")	
    @Size(max=20)
    private String firstName;

    @NotNull(message = "Last name can not be empty")
    @Size(max=40)
    @Column(value = "lastName")	
	private String lastName;

    @Valid
    @Column(value = "birthDate")	
    @NotNull(message = "Birthdate can not be empty")
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate birthDate;

    @Valid
    @Column(value = "gender")	
	private Gender gender;

    @ValidEmailAddress
    @Column(value = "email")	
	private String email;
    
    @Builder
    public Person(UUID id, LocalDateTime created, String creator, LocalDateTime modified, String modifier,
    		String personNumber, String firstName, String lastName, LocalDate birthDate, Gender gender, String email) {
    	super(id, created, creator, modified, modifier);
    	this.personNumber = personNumber;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.birthDate = birthDate;
    	this.gender = gender;
    	this.email = email;
    }
}

