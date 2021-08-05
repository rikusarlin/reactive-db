package fi.rikusarlin.reactivedb.model;

import java.time.LocalDate;

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

/**
 * A natural person residing in Finland
 */
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
    
    public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

