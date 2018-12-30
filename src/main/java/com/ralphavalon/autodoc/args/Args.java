package com.ralphavalon.autodoc.args;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import lombok.Getter;
import lombok.ToString;

@Component
@Getter
@Order(1)
@ToString
public class Args implements CommandLineRunner {
	
	@Parameter(names = "--help", help = true)
	private boolean help;
	
	@Parameter(names={"--schemaspy", "-ss"})
    private String schemaSpyJar;
	
	@Parameter(names={"--properties", "-p"})
    private String propertiesFile;

	@Override
	public void run(String... args) throws Exception {
		JCommander jCommander = JCommander.newBuilder()
		  .addObject(this)
		  .build();
		
		jCommander.parse(args);
		
		if (this.help) {
            jCommander.usage();
        }
	}

}
