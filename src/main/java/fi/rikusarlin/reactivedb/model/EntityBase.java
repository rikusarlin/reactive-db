package fi.rikusarlin.reactivedb.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * A base class for entities
 */

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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

}

