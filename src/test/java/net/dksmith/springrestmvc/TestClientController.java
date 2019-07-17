package net.dksmith.springrestmvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * SpringBootTest annotation allows us to run a test web environment over a random port.
 * It also registers a TestRestTemplate which allows to perform HTTP requests (using 
 * springframwork.http RequestEntity, ResponseEntity, Http[Entity|Status|Headers] to 
 * set options).
 * 
 * Basic authentication can be used if security is enabled.
 *  
 * @author Derek Smith
 *
 */
 
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringRestMvcApplication.class)
public class TestClientController {
	
	@LocalServerPort //Since we are using RANDOM_PORT web env, this annotation provides the port to us.
	private int port;
	
	@Value("${server.address}") //A way to get properties from application.properties file
	private String address;
	
	@Autowired
	TestRestTemplate restTemplate;
	HttpHeaders headers = new HttpHeaders(); //useful if we want to set header values to pass into entity
	
	/**
	 * Creates a {@link RequestEntity} and executes it with {@link TestRestTemplate} exchange, done in {@link #doGet}.
	 * Uses {@link ObjectMapper} from  jackson.databind to create a {@link JsonNode} from the {@link ResponseEntity}.
	 * <p>Asserts there is greater than one json entry.</p>
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testGetAllClients() throws URISyntaxException, IOException {
		String body = "{\"ip\":\"testIP\", \"userAgent\":\"testUserAgent\"}";		
		doPost(new URI(createURL(address, port,"/api/v1/clients/", false)), body); 
		doPost(new URI(createURL(address, port,"/api/v1/clients/", false)), body);
		ResponseEntity<String> getResponse = doGet(new URI(createURL(address, port,"/api/v1/clients/", false)));
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode getResponseJsonNode = mapper.readTree(getResponse.getBody()); 
		
		assertThat(getResponseJsonNode.size()).isGreaterThan(1);
	}
	
	/**
	 * Asserts http status code returned by post is CREATED.
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateClientReturnCode() throws URISyntaxException {
		String body = "{\"ip\":\"testIP\", \"userAgent\":\"testUserAgent\"}";
		ResponseEntity<String> response = doPost(new URI(createURL(address, port,"/api/v1/clients/", false)), body); 
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	/**
	 * Uses Request and Response Entity to perform post and get requests using TestRestTemple exchange.
	 * Parses returned string to json using ObjectMapper and JsonNode
	 * <p>Asserts the returned ip and user-agent are equal to what was posted.</p>
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testCreateAndAGetClient() throws URISyntaxException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String body = "{\"ip\":\"testIP\", \"userAgent\":\"testUserAgent\"}";
		
		// Add row to database
		ResponseEntity<String> putResponse = doPost(new URI(createURL(address, port,"/api/v1/clients/", false)), body); 
		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		// Get created ID
		JsonNode putResponseJsonNode = mapper.readTree(putResponse.getBody());
		String putResponseID = putResponseJsonNode.get("id").asText();
		
		// Do get and assert that it new row is returned
		ResponseEntity<String> getResponse = doGet(new URI(createURL(address, port,"/api/v1/clients/"+putResponseID, false))); 
		JsonNode getResponseJsonNode = mapper.readTree(getResponse.getBody());
		String getResponseIp = getResponseJsonNode.get("ip").textValue();
		String getResponseUA = getResponseJsonNode.get("userAgent").textValue();
		assertThat(getResponseIp).isEqualTo("testIP");
		assertThat(getResponseUA).isEqualTo("testUserAgent");
	}
	
	/**
	 * Does {@link TestRestTemplate} post exchange with given {@link URI} and body.
	 * @param uri
	 * @param body
	 * @return
	 */
	private ResponseEntity<String> doPost(URI uri, String body) {
		RequestEntity<String> putRequest = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON).body(body, String.class);
		return restTemplate.exchange(putRequest, String.class);
	}

	/**
	 * Does {@link TestRestTemplate} get exchange with given {@link URI}.
	 * @param uri
	 * @return ResponseEntity
	 */
	private ResponseEntity<String> doGet(URI uri) {
		RequestEntity<Void> getRequest = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(getRequest, String.class);
	}
	
	private String createURL(String address, int port, String uri, boolean https) {
		if(https) 
			return null;
		return "http://"+address+":"+port+uri;
	}
}
