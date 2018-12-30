package com.ralphavalon.autodoc.database;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.stereotype.Component;

import com.ralphavalon.autodoc.args.Args;
import com.ralphavalon.autodoc.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SQLDocValidator implements Validator {
	
	public boolean isValidArgs(Args parsedArgs) {
		if(isBlank(parsedArgs.getSchemaSpyJar())) {
			log.info("Empty schema spy jar property. Skipping database process...");
			return false;
		}
		if(isNotBlank(parsedArgs.getSchemaSpyJar()) &&
				isNotBlank(parsedArgs.getPropertiesFile())) {
			log.info("Initiating database process...");
			return true;
		}
		throw new IllegalArgumentException("Missing properties file parameter");
	}
	
}
