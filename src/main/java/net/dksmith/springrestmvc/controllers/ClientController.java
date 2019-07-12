package net.dksmith.springrestmvc.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.dksmith.springrestmvc.domain.Client;
import net.dksmith.springrestmvc.services.ClientService;

/*
 * Will receive http requests and execute actions on the 
 * db through the ClientService
 */

@RestController
@RequestMapping(ClientController.BASE_URL) // NOTE: This is applied to each method annotated "GetMapping"
public class ClientController {

	public static final String BASE_URL = "/api/v1/clients"; // TODO: move this to properties file
	private final ClientService clientService;
	
	public ClientController(ClientService clientService) { // NOTE: Spring injects constructor variables because we added Spring annotation
		this.clientService = clientService;
	}
	
	@GetMapping
	public List<Client> getAllClients(){
		return clientService.findAllClients();
	}
	
	@GetMapping("/{id}")
	public Client getClient(@PathVariable Long id) {
		return clientService.findCustomerByID(id);
	}
	
	@GetMapping("/request-info")
	public Client reflectRequestInfo(HttpServletRequest request, Client client) {
		client.setFromRequest(request);
		clientService.saveClient(client);
		return client;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	// NOTE: because we annotate RequestBody, jackson library parses POST json into client object
	public Client saveCustomer(@RequestBody Client client) {
		return clientService.saveClient(client);
	}
}
