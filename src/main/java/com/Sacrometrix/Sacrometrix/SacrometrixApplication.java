package com.Sacrometrix.Sacrometrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SacrometrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(SacrometrixApplication.class, args);
	}

}
