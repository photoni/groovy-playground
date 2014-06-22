package playground;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j
import model.Security

import org.junit.Test

import service.SecurityService

@Slf4j
class SecurityServiceTest {
	

	@Test
	public void loadSecurityTest() {
		SecurityService ss= new SecurityService();
		Security s=ss.loadSecurity("GOOGL");
		s.getHistory().each { obj -> def Date date=Date.parse("yyyy-MM-dd",obj.dateAsString);obj.setDate(date.getTime())};
		s.getHistory().eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry: ${obj.date}")};
	}
}
