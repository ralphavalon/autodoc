package com.ralphavalon.autodoc.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ralphavalon.autodoc.args.Args;
import com.ralphavalon.autodoc.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(2)
public class SQLDoc implements CommandLineRunner {
	
	@Autowired
	private Args parsedArgs;
	
	@Autowired
	private Validator validator;

	@Override
	public void run(String... args) throws Exception {
		try {
			if(validator.isValidArgs(parsedArgs)) {
				Process proc = execute(getCommands(parsedArgs));
				
				try(InputStreamReader infoOutput = new InputStreamReader(proc.getInputStream());
					InputStreamReader errorOutput = new InputStreamReader(proc.getErrorStream());
					BufferedReader stdInput = new BufferedReader(infoOutput);
					BufferedReader stdError = new BufferedReader(errorOutput)) {
					
					stdInput.lines().forEach(log::info);
					stdInput.lines().forEach(log::error);
				}
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
		}
	}

	protected Process execute(String[] commands) throws IOException {
		return Runtime.getRuntime().exec(commands);
	}
	
	private String[] getCommands(Args parsedArgs) {
		return new String[] {"java", "-jar", parsedArgs.getSchemaSpyJar(), "-configFile", parsedArgs.getPropertiesFile()};
	}

}
