package com.ralphavalon.autodoc;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutodocApplication {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AutodocApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}

}
