package net.dksmith.springrestmvc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import net.dksmith.springrestmvc.domain.Client;
import net.dksmith.springrestmvc.repositories.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService{

	private final ClientRepository clientRepository;
	
	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	@Override
	public Client findCustomerByID(Long id) {
		return clientRepository.findById(id).get();
	}

	@Override
	public List<Client> findAllClients() {
		return clientRepository.findAll();
	}

	@Override
	public Client saveClient(Client client) {
		return clientRepository.save(client);
	}

}
