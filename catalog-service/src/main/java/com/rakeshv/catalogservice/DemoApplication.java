package com.rakeshv.catalogservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableCircuitBreaker
public class DemoApplication implements CommandLineRunner{

	@Autowired
	VaultTemplate vaultTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String...strings) throws Exception{
		VaultResponse response = vaultTemplate.opsForKeyValue("secret", KeyValueBackend.KV_2).get("catalog-service");
		log.info("username is {}", response.getData().get("spring.datasource.username"));
		log.info("passsword is {}", response.getData().get("spring.datasource.password"));
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
