package net.dksmith.springrestmvc.services;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;

/**
 * Used to interact with the MaxMind GeiLite2-Country database.
 * We do not want multiple instances of this class loading and reading the database each time it is
 * created. Instead make this a singleton bean by adding @Component annotation. This allows Spring 
 * to add this class to the spring context, and can be Autowired in somewhere else. 
 * 
 * <p>Note: It could not be Autowired into the client @Entity class, but I do not know the reason. Instead
 * it is wired into the WebviewController class and passed to the client when needed.</p> 
 * 
 * <p>Note: @Value annotated variables are have not been set at construction time. Use @PostConstruct annotation
 * to use @Value variable after constructor is called for needed initialization steps.</p>
 * 
 * @author Derek Smith
 *
 */

@Component
public class GeoIpService{

	@Value("${maxmind.country.file}")
	private String dbLocation;
	
	private DatabaseReader dbReader;
	
	@PostConstruct 
	public void init() throws IOException {
		dbReader = new DatabaseReader.Builder(new File(dbLocation)).build();
	}
	
	public String lookupCountry(String ip) throws UnknownHostException, IOException, GeoIp2Exception {
		return dbReader.country(InetAddress.getByName(ip)).getCountry().getName();
	}

}
