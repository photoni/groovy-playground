package service;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j
import model.Security

import org.junit.Test

@Slf4j
class SecurityServiceTest {


	/**
	 * Test for loading a security from the source
	 */
	@Test
	public void loadSecurityTest() {
		SecurityService ss= SecurityService.instance
		Security s=ss.loadSecurity("GOOGL")
		s.getHistory().eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry day: ${obj.date}")}
		/* define here assertions */
	}

	/**
	 * Test for loading a security from the source and storing in the repository
	 */
	@Test
	public void loadSecurityAndStoreTest() {
		SecurityService ss= SecurityService.instance
		Security s=ss.loadSecurityAndStore("GOOGL");
		/* define here assertions */
	}
	
	/**
	 * Test for getting a security from the repository
	 */
	@Test
	public void getSecurity() {
		SecurityService ss= SecurityService.instance
		Security s=ss.getSecurity("GOOGL");
		s.getHistory().eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry day: ${obj.date}")}
	}
}
