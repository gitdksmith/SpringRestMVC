package net.dksmith.springrestmvc;

import java.net.URI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import net.dksmith.springrestmvc.controllers.WebviewController;

/**
 * Here we use WebMvcTest instead of SpringBootTest so that we only start the 
 * specified controller. Useful when there is no need to start the service or
 * repository classes.
 * 
 * Also check out @AutoConfigureMockMvc annotation
 * 
 * @author Derek Smith
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(WebviewController.class)
public class TestWebviewController {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	/**
	 * Use mockMvc to perform get request to index page.
	 * Checks that returned status code is OK, and the
	 * body is not blank
	 * @throws Exception
	 */
	public void testIndexPage() throws Exception {
		MvcResult result = mockMvc.perform(get("/")).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(200);
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
	}
	
	@Test
	/**
	 * Uses a servlet request builder to set uri and header information. Setting 
	 * the remote address is not built into MockMvcRequestBuilders, but the "with"
	 * extension allows us to implement the post processor interface where we 
	 * add changes we want executed after the MockHttpServletRequest is built.
	 * 
	 * Performs the request and checks that our test information was returned. 
	 * @throws Exception
	 */
	public void testReflectRequestInfo() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(new URI("/request-info"))
				.header("user-agent", "testUserAgent").with(remoteAddr("10.0.0.99"));
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertThat(result.getResponse().getContentAsString()).contains("10.0.0.99", "testUserAgent");
	}
	
	private static RequestPostProcessor remoteAddr(final String remoteAddr) {
		return new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setRemoteAddr(remoteAddr);
				return request;
			}
		};
	}
	
	
}
