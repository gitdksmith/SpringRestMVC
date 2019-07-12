package net.dksmith.springrestmvc.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import net.dksmith.springrestmvc.domain.Client;
import net.dksmith.springrestmvc.repositories.ClientRepository;

/*
 * Initializes the in mem H2 db and populates data
 * 
 * NOTE: I added "NOTE" tags to add short explanations or helpful hints 
 * 
 * NOTE: Since security was added, console will show you generated password, 
 * login with username "user"
 * 
 * NOTE: Since Spring Actuator was added you can get health, env, metrics, etc
 * but need to enable the endpoints (have not done that yet, just added to pom)
 * 
 * TODO: how beans and constructor injection vs autowire works in Spring
 */

@Component
public class BootStrapData implements CommandLineRunner{

	private final ClientRepository clientRepository;
	
	public BootStrapData(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Loading Client Data");
		
		Client c1 = new Client();
		c1.setIp("10.0.0.1");
		c1.setUserAgent("some-browser");
		clientRepository.save(c1);
		
		Client c2 =new Client();
		c2.setIp("10.0.0.2");
		c2.setUserAgent("another-browser");
		clientRepository.save(c2);
		
		System.out.println("Client Data Loaded...");
		
	}

	
}
