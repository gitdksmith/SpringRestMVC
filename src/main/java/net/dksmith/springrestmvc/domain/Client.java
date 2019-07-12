package net.dksmith.springrestmvc.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/*
 * This will be transformed as a table via the Entity annotation
 * NOTE: Make sure to enable annotation processing in your IDE
 */

@Data // NOTE: project lombok providing getters and setters for this class
@Entity
public class Client {

	@Id // indicates this is primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //TODO look up the GenerationType differences
	private Long id;
	
	private String ip;
	private String userAgent;
}
