package net.dksmith.springrestmvc.domain;

import java.util.Enumeration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import lombok.Data;

/*
 * This will be transformed as a table via the Entity annotation
 * NOTE: Make sure to enable annotation processing in your IDE
 */

@Data // NOTE: project lombok providing getters and setters for this class
@Entity
public class Client {

	@Id // indicates this is primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //TODO look up the GenerationType differences
	private Long id;
	
	private String ip;
	private String proxyIp;
	private String userAgent;
	
	@Transient
	public static final String[] PROXY_INDICATOR_HEADERS = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR"
	};
	
	public void setFromRequest(HttpServletRequest request) {
		setIp(request);
		setUserAgent(request);
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setIp(HttpServletRequest request) {
		for(String header : PROXY_INDICATOR_HEADERS) {
			String possibleIp = request.getHeader(header);
			if (possibleIp != null && possibleIp.length() != 0 && !"unknown".equalsIgnoreCase(possibleIp)) {
				ip = possibleIp;
				proxyIp = request.getRemoteAddr();
				return;
			}
		}
		ip = request.getRemoteAddr();
		proxyIp = null;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public void setUserAgent(HttpServletRequest request) {
		this.userAgent =  request.getHeader("user-agent");
	}
	
	public void printRequest(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();	
		while(headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String val = request.getHeader(key);
			System.out.println("Key: " + key + " value: " + val);
		}
		System.out.println("Remote IP: " + request.getRemoteAddr());
		System.out.println("Remote Host: " + request.getRemoteHost());
		System.out.println("Local IP: " + request.getLocalAddr());
		System.out.println("Local Host: " + request.getLocalName());
	}
}
