package com.ogive.oheo.serviceclients;

import java.io.File;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestClientTest implements CommandLineRunner {

	public static void main(String[] args) {
		 SpringApplication.run(RestClientTest.class, args);
	}
	
	RestTemplate restTemplate() throws Exception {
		
		final String trustStorePassword = "123456";
		
		
		File file = new File("C:\\Users\\Lenovo\\Downloads\\cert\\certnew\\ogive\\keystore.p12");
		
		
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(file, trustStorePassword.toCharArray())
				.build();

		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
		
		HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(factory);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		final String password = "123456";

		File file = new File("C:\\Users\\Lenovo\\Downloads\\cert\\certnew\\ogive\\keystore.p12");
		
		
		SSLContext sslContext = SSLContextBuilder.create()
				.loadTrustMaterial(file, password.toCharArray())
				.build();

		CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(client);

		RestTemplate restTemplate = restTemplate() ;
		
		ResponseEntity<String> forEntity = restTemplate.getForEntity(
				"https://oheo-backend.ogivetechnology.com:8081/oheo-service/customers?orderBy=id&page=0&size=10&sortDirection=ASC",
				String.class);
		
		
		
		
		
		System.out.println(forEntity.getBody());
	}

}
