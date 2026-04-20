package com.sahil.virality_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class ViralityEngineApplication {

	public static void main(String[] args) {
		// Forces the app to use UTC, bypassing the "Asia/Calcutta" (deprecated) vs "Asia/Kolkata" error
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(ViralityEngineApplication.class, args);
	}
}