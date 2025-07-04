package com.pieceofcake.alert_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AlertServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertServiceApplication.class, args);
	}

}
