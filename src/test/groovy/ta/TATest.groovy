package ta;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j
import model.Security

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test

import service.SecurityService

import com.tictactec.ta.lib.Core
import com.tictactec.ta.lib.MInteger

/**
 * Tests several TALib functions on a single security price array
 * @author filippo
 *
 */
@Slf4j
class TASingleSecurityTest {
	static final String TICKER = "GOOGL"
	SecurityService ss;	Security s;double[] prices;TechnicalAnalisys ta;
	
	@Before
	public void init(){
		ss= SecurityService.instance	
		s=ss.getSecurity(TICKER)
		int historyLength=s.getHistory().size()
		prices=new double[historyLength]
		s.getHistory().eachWithIndex { obj, i -> prices[i]=obj.adjClose}
		ta=TechnicalAnalisys.instance
		//s.getHistory().eachWithIndex { val,i -> log.debug(" price: ${i} - val : ${val.adjClose}")}
	}

	@Test
	public void smaTest() {	
		double[] out= ta.sma(prices)
		//out.eachWithIndex { val,i -> log.debug(" aver: ${i} - val : ${val}")}
	}
	
	@Test
	public void wmaTest() {		
		double[] out= ta.wma(prices)
		out.eachWithIndex { val,i -> log.debug(" aver: ${i} - val : ${val}")}
	}
	
	@Test
	public void emaTest() {
		double[] out= ta.ema(prices)
		out.eachWithIndex { val,i -> log.debug(" aver: ${i} - val : ${val}")}
	}
}
