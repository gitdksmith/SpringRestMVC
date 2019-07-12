package net.dksmith.springrestmvc.services;

import java.util.List;
import net.dksmith.springrestmvc.domain.Client;

/*
 * Used to interact with the database
 */

public interface ClientService {

	Client findCustomerByID(Long id);
	List<Client> findAllClients();
	Client saveClient(Client client);
}
