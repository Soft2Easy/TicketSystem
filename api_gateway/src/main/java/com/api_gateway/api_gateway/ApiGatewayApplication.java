package com.api_gateway.api_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ApiGatewayApplication {

	 @Value("${test.property:NOT_FOUND}")
    private String testProperty;
    
    @Autowired
    private Environment environment;
    
    @PostConstruct
    public void init() {
        System.out.println("========== TEST PROPERTY: " + testProperty + " ==========");
        System.out.println("========== ACTIVE PROFILES: " + String.join(", ", environment.getActiveProfiles()) + " ==========");
    }
    
    @GetMapping("/debug/props")
    public String debugProps() {
        return "Test Property: " + testProperty;
    }

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
