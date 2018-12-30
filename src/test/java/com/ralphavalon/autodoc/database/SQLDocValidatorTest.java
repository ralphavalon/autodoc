package com.ralphavalon.autodoc.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ralphavalon.autodoc.args.Args;

@RunWith(SpringRunner.class)
public class SQLDocValidatorTest {
	
	@MockBean
	private Args parsedArgs;
	
	@SpyBean
	private SQLDocValidator validator;
	
	private static final String JAR_FILE_NAME = "schemaspy-6.0.0.jar";
	private static final String PROPERTIES_FILE = "application-test.properties";
	
	@Test
	public void whenRunningWithRightParams_thenShouldBeValid() throws Exception {
		doReturn(JAR_FILE_NAME).when(parsedArgs).getSchemaSpyJar();
		doReturn(PROPERTIES_FILE).when(parsedArgs).getPropertiesFile();
		
		assertThat(validator.isValidArgs(parsedArgs)).isTrue();
	}
	
	@Test
	public void whenRunningWithoutSchemaSpyParam_thenShouldNotBeValid() throws Exception {
		doReturn(null).when(parsedArgs).getSchemaSpyJar();
		doReturn(PROPERTIES_FILE).when(parsedArgs).getPropertiesFile();
		
		assertThat(validator.isValidArgs(parsedArgs)).isFalse();
	}
	
	@Test
	public void whenRunningWithoutParams_thenShouldNotBeValid() throws Exception {
		doReturn(null).when(parsedArgs).getSchemaSpyJar();
		doReturn(null).when(parsedArgs).getPropertiesFile();
		
		assertThat(validator.isValidArgs(parsedArgs)).isFalse();
	}
	
	@Test
	public void whenRunningWithoutPropertiesFileParam_thenShouldThrowError() throws Exception {
		doReturn(JAR_FILE_NAME).when(parsedArgs).getSchemaSpyJar();
		doReturn(null).when(parsedArgs).getPropertiesFile();
		
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> { validator.isValidArgs(parsedArgs); })
	        .withMessage("Missing properties file parameter")
	        .withNoCause();
	}

}
