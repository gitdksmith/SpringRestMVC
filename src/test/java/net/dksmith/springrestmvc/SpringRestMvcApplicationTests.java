package net.dksmith.springrestmvc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.dksmith.springrestmvc.controllers.ClientController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRestMvcApplicationTests {

	private ClientController controller;
	
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
