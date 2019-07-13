package net.dksmith.springrestmvc;


import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import net.dksmith.springrestmvc.domain.Client;

@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
public class TestClient {
	
	@Mock
	HttpServletRequest request;
	Client client;
	
	String userAgent = "mockUserAgent";
	String remoteAddr = "mockRemoteAddr";
	String proxyAddr = "mockProxyAddr";
	
	@Before
	public void setNewClient() {
		client = new Client();
	}
	
	@Test
	public void testSetFromRequest() {
		Mockito.when(request.getHeader("user-agent")).thenReturn(userAgent);
		Mockito.when(request.getRemoteAddr()).thenReturn(remoteAddr);
		client.setFromRequest(request);
		assertThat(client.getUserAgent()).isEqualTo(userAgent);
		assertThat(client.getIp()).isEqualTo(remoteAddr);

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
		for(String s: Client.PROXY_INDICATOR_HEADERS) {
			Mockito.when(request.getHeader(s)).thenReturn(remoteAddr);
			client.setIp(request);
			assertThat(client.getIp()).isEqualTo(remoteAddr);
			assertThat(client.getProxyIp()).isEqualTo(proxyAddr);
		}
	}
	
	@Test 
	public void testSetIpFromRequest_NoProxyPresent() {
		Mockito.when(request.getRemoteAddr()).thenReturn(remoteAddr);
		client.setIp(request);
		assertThat(client.getIp()).isEqualTo(remoteAddr);
	}

}