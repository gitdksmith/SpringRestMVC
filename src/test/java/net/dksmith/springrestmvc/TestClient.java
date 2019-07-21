package net.dksmith.springrestmvc;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import net.dksmith.springrestmvc.domain.Client;
import net.dksmith.springrestmvc.services.GeoIpService;

/**
 * Tests Client class using mocked servlet. 
 * <p>Running with MockitoJUnitRunner allows us to mock objects, like our HttpServletRequest,
 * by using the @Mock annotation. This means we can test the Client class without having
 * to start a server or create real http requests.</p>
 * <p>We use StrictStubs to help find problems, like setting unused stubs in the mock, or 
 * stubbed argument mismatches.</p>
 * @author Derek Smith
 *
 */
@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class TestClient {
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	GeoIpService geoIpService;
	
	Client client;
	
	String userAgent = "mockUserAgent";
	String remoteAddr = "mockRemoteAddr";
	String proxyAddr = "mockProxyAddr";
	
	@Before
	public void setNewClient() {
		client = new Client();
	}
	
	/*
	 * Not going to add method comments for each since they follow the same pattern:
	 * 1. Use Mockito to set the value we want returned when calling specific methods
	 * on the mocked request.
	 * 2. Passed mocked request to client methods
	 * 3. Assert client object variables were set from the mocked request
	 */
	@Test
	public void testSetFromRequest() throws UnknownHostException, IOException, GeoIp2Exception {
		// Why do we use lenient() here?
		// In the client class we call request.getHeader on this same request, but using a list
		// of possible header strings as arguments. Since those arguments do not match "user-agent"
		// it is seen as a possible "stubbed argument mismatch".
		Mockito.lenient().when(request.getHeader("user-agent")).thenReturn(userAgent);
		Mockito.when(request.getRemoteAddr()).thenReturn(remoteAddr);
		Mockito.when(geoIpService.lookupCountry(Mockito.anyString())).thenReturn("United States");
		
		client.setFromRequest(request, geoIpService);
		
		assertThat(client.getUserAgent()).isEqualTo(userAgent);
		assertThat(client.getIp()).isEqualTo(remoteAddr);
		assertThat(client.getCountryOrigin()).isEqualTo("United States");

	}
	
	@Test
	public void testSetUserAgentFromRequest() {
		Mockito.when(request.getHeader("user-agent")).thenReturn(userAgent);
		client.setUserAgent(request);
		assertThat(client.getUserAgent()).isEqualTo(userAgent);
	}
	
	@Test
	public void testSetIpFromRequest_WithProxyPresent() {
		Mockito.when(request.getRemoteAddr()).thenReturn(proxyAddr);
		for(String proxyIndicator: Client.PROXY_INDICATOR_HEADERS) {
//			Mockito.when(request.getHeader(proxyIndicator)).thenReturn(remoteAddr);
			// here we are told to follow the format below because of strict stubbing.
			// I dont really know why it makes a difference
			Mockito.doReturn(remoteAddr).when(request).getHeader(proxyIndicator);
			client.setIp(request);
			assertThat(client.getIp()).isEqualTo(remoteAddr);
			assertThat(client.getProxyIp()).isEqualTo(proxyAddr);
//			Mockito.when(request.getHeader(proxyIndicator)).thenReturn("");
			Mockito.doReturn("").when(request).getHeader(proxyIndicator);
		}
	}
	
	@Test 
	public void testSetIpFromRequest_NoProxyPresent() {
		Mockito.when(request.getRemoteAddr()).thenReturn(remoteAddr);
		client.setIp(request);
		assertThat(client.getIp()).isEqualTo(remoteAddr);
	}

}