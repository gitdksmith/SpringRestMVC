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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringRestMvcApplication.class)
public class TestClientController {
	
	@LocalServerPort
	private int  port;
	
	@Value("${server.address}")
	private String address;
	
	@Autowired
	TestRestTemplate restTemplate;
	HttpHeaders headers = new HttpHeaders(); //useful if we want to set header values to pass into entity
	
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
	
	@Test
	public void testCreateClientReturnCode() throws URISyntaxException {
		String body = "{\"ip\":\"testIP\", \"userAgent\":\"testUserAgent\"}";
		
		// OK working with http stuff gets pretty messy. You can do it several ways:
		// Create an HttpHeader object, then an HttpEntity object using that header,
		// then a RequestEntity object using that HttpEntity.
		// Or try and squish it into one as below.
		ResponseEntity<String> response = doPost(new URI(createURL(address, port,"/api/v1/clients/", false)), body); 
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
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
	
	private ResponseEntity<String> doPost(URI uri, String body) {
		RequestEntity<String> putRequest = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON).body(body, String.class);
		return restTemplate.exchange(putRequest, String.class);
	}
	
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
