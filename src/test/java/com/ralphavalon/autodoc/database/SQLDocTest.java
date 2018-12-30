package com.ralphavalon.autodoc.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ralphavalon.autodoc.args.Args;

@RunWith(SpringRunner.class)
public class SQLDocTest {
	
	@MockBean
	private Args parsedArgs;
	
	@SpyBean
	private SQLDoc sqlDoc;
	
	@Mock
	private Process process;

	@Captor
	private ArgumentCaptor<String[]> paramsCaptor;
	
	private static final String JAR_FILE_NAME = "schemaspy-6.0.0.jar";
	private static final String PROPERTIES_FILE = "application-test.properties";

	@Test
	public void test() throws Exception {
		InputStream inputStreamMock = new ByteArrayInputStream("test data".getBytes());

		doReturn(process).when(sqlDoc).execute(any(String[].class));
		doReturn(inputStreamMock).when(process).getInputStream();
		doReturn(inputStreamMock).when(process).getErrorStream();
		
		doReturn(JAR_FILE_NAME).when(parsedArgs).getSchemaSpyJar();
		doReturn(PROPERTIES_FILE).when(parsedArgs).getPropertiesFile();
		
		sqlDoc.run(new String[] { "-ss", JAR_FILE_NAME, "-p", PROPERTIES_FILE });
		
		verify(sqlDoc).execute(paramsCaptor.capture());
		
		String[] params = paramsCaptor.getValue();
		assertThat(params).isEqualTo(new String[] {"java", "-jar", JAR_FILE_NAME, "-configFile", PROPERTIES_FILE} );

	}

}
