package com.example.search.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author vivek thakur
 *
 */
@SpringBootApplication(scanBasePackages = { "com.example.search.config", "com.example.search.rest" })
public class SearchApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(SearchApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}
}
