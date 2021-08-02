package fi.rikusarlin.reactivedb.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A base class for entities
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class EntityBase{
	@Id
	@Column(value = "id")	
	protected UUID id;

    @Column(value = "created")	
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
	protected LocalDateTime created;

    @Column(value = "creator")	
	protected String creator;

    @Column(value = "modified")	
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
	protected LocalDateTime modified;

    @Column(value = "modifier")	
	protected String modifier;
}

