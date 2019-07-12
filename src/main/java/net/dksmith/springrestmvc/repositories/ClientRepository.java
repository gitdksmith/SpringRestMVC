package net.dksmith.springrestmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import net.dksmith.springrestmvc.domain.Client;

/*
 * Will be used by the ClientService to interact with database
 * The controller then interacts with the service, not the repository
 * 
 * SpringDataJPA is going to provide implementation of JpaRepository.
 * Look at JpaRepository for provided methods
 * Works with database through Hibernate 
 * 
 * We pass in the Domain class Client which is our Entity, as well as 
 * the id/primary key type.
 */

public interface ClientRepository extends JpaRepository<Client, Long>{

}
