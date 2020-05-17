package cn.edu.tju.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class HttpClientConfig {
	
	@Bean("httpClient")
	public CloseableHttpClient httpClient() {
		HttpClientBuilder clientBuilder = HttpClients.custom() ;
		return clientBuilder.build() ; 
	}
		
}
