package com.ralphavalon.autodoc.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ralphavalon.autodoc.args.Args;
import com.ralphavalon.autodoc.validator.Validator;

@RunWith(SpringRunner.class)
public class SQLDocTest {
	
	@MockBean
	private Args parsedArgs;
	
	@MockBean
	private Validator validator;
	
	@SpyBean
	private SQLDoc sqlDoc;
	
	@Mock
	private Process process;

	@Captor
	private ArgumentCaptor<String[]> paramsCaptor;
	
	private static final String JAR_FILE_NAME = "schemaspy-6.0.0.jar";
	private static final String PROPERTIES_FILE = "application-test.properties";
	
	@Before
	public void setup() throws IOException {
		InputStream inputStreamMock = new ByteArrayInputStream("test data".getBytes());

		doReturn(process).when(sqlDoc).execute(any(String[].class));
		doReturn(inputStreamMock).when(process).getInputStream();
		doReturn(inputStreamMock).when(process).getErrorStream();
		
		doReturn(JAR_FILE_NAME).when(parsedArgs).getSchemaSpyJar();
		doReturn(PROPERTIES_FILE).when(parsedArgs).getPropertiesFile();
	}

	@Test
	public void whenRunningWithRightParams_thenShouldExecuteSuccessfully() throws Exception {
		doReturn(true).when(validator).isValidArgs(parsedArgs);
		
		sqlDoc.run(new String[] { "-ss", JAR_FILE_NAME, "-p", PROPERTIES_FILE });
		
		verify(sqlDoc).execute(paramsCaptor.capture());
		
		String[] params = paramsCaptor.getValue();
		assertThat(params).isEqualTo(new String[] {"java", "-jar", JAR_FILE_NAME, "-configFile", PROPERTIES_FILE} );
	}
	
	@Test
	public void whenRunningWithoutSchemaSpyParam_thenShouldNotExecute() throws Exception {
		doReturn(false).when(validator).isValidArgs(parsedArgs);
		
		sqlDoc.run(new String[] { "-ss", JAR_FILE_NAME, "-p", PROPERTIES_FILE });
		
		verify(sqlDoc, times(0)).execute(any(String[].class));
	}
	
	@Test
	public void whenRunningWithoutPropertiesFileParam_thenShouldNotExecute() throws Exception {
		doThrow(new IllegalArgumentException("error message")).when(validator).isValidArgs(parsedArgs);
		
		sqlDoc.run(new String[] { "-ss", JAR_FILE_NAME, "-p", PROPERTIES_FILE });
		
		verify(sqlDoc, times(0)).execute(any(String[].class));
	}

}
