package com.ralphavalon.autodoc.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SQLDoc implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		Process proc = execute(getCommands(args));

		try(InputStreamReader infoOutput = new InputStreamReader(proc.getInputStream());
			InputStreamReader errorOutput = new InputStreamReader(proc.getErrorStream());
			BufferedReader stdInput = new BufferedReader(infoOutput);
			BufferedReader stdError = new BufferedReader(errorOutput)) {
			
			stdInput.lines().forEach(log::info);
			stdInput.lines().forEach(log::error);
			
		}
	}

	protected Process execute(String[] commands) throws IOException {
		return Runtime.getRuntime().exec(commands);
	}
	
	private String[] getCommands(String... args) {
		return new String[] {"java", "-jar", args[0], "-configFile", "application-test.properties"};
	}

}
