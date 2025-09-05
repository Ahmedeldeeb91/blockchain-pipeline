package com.parotia.ingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class IngestorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngestorApplication.class, args);
	}

}
