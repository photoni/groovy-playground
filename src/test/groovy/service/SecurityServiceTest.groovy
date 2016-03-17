package service

import helpers.ArrayHelper
import org.junit.Before;

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
		Security s=ss.loadSecurity("AAPL")
		//s.getHistory().eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry day: ${obj.adjClose}")}
        ArrayHelper.log(s.getHistory(),log,true,'TRACE')
		/* define here assertions */
	}

	/**
	 * Test for loading a security from the source and storing in the repository
	 */
	@Test
	public void loadSecurityAndStoreTest() {
		SecurityService ss= SecurityService.instance

		//def sec = "GOOGL"
		//def sec = "AAPL"
		def sec = "AAPL"
		Security s=ss.loadSecurityAndStore(sec);
		/* define here assertions */
	}
	
	/**
	 * Test for getting a security from the repository
	 */
	@Test
	public void getSecurity() {
		SecurityService ss= SecurityService.instance
		Security s=ss.getSecurity("AAPL");
		s.getHistory().eachWithIndex { obj, i -> log.trace(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose}")}
	}

    /**
     * Test for getting a security from the repository
     */
    @Test
    public void getSecurityFromCsv() {
        SecurityService ss= SecurityService.instance
        Map<String, String> mapping = new HashMap<String, String>()
        mapping.put("Date", "dateAsString")
        mapping.put("Close", "adjClose")
        mapping.put("High", "high");
        mapping.put("Low", "low");
        Security s=ss.getSecurityFromCsv('cs-adx.csv',mapping,"dd-MMM-yy",true)
        s.getHistory().eachWithIndex { obj, i -> log.trace(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose}")}
    }
}
