package ta

import data.TestDataSupport
import groovy.util.logging.Slf4j
import model.Security

import org.junit.Before
import org.junit.Test

import service.SecurityService



/**
 * Tests several TALib functions on a single security price array
 * @author filippo
 *
 */
@Slf4j
class TASingleSecurityTest {
	static final String TICKER = "GOOGL"
	static final boolean CUT = true
	SecurityService ss;	Security s;double[] prices;double[] highs;double[] lows;TechnicalAnalisys ta;

	@Before
	public void init(){
		ss= SecurityService.instance
		s=ss.getSecurity(TICKER)
		int historyLength=s.getHistory().size()
		prices=new double[historyLength]
		highs=new double[historyLength]
		lows=new double[historyLength]
		s.getHistory().eachWithIndex { obj, i -> prices[i]=obj.adjClose;highs[i]=obj.high;lows[i]=obj.low}
		prices.each {obj -> log.trace(" val : ${obj}")}
		ta=TechnicalAnalisys.instance
		//s.getHistory().eachWithIndex { val,i -> log.debug(" price: ${i} - val : ${val.adjClose}")}
	}

	@Test
	public void smaTest() {

		double[] out1=Indicators.sma(0,TestDataSupport.SMA_VALUES.size(),TestDataSupport.SMA_VALUES,5); //OK fine
		log.debug("-------- SMA ---------");
		out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
		assert out1[3]==0
		assert out1[4]==125.872
		assert out1[5]==126.096
		assert out1[29]==127.27799999999999
	}

	@Test
	public void bollingerBandsTest() {
		double[][] out=Indicators.bollingerBands(0,prices.length,prices, 30); //OK fine
		out[0].eachWithIndex { val,i -> log.trace(" boll lower: ${i} - val : ${val}")}
		out[1].eachWithIndex { val,i -> log.trace(" boll middle: ${i} - val : ${val}")}
		out[2].eachWithIndex { val,i -> log.trace(" boll higher: ${i} - val : ${val}")}
	}
	
	@Test
	public void macdTest() {
		double[][] out=Indicators.macd(0,prices.length,prices); //OK fine
		out[0].eachWithIndex { val,i -> log.trace(" macd line lower: ${i} - val : ${val}")}
		out[1].eachWithIndex { val,i -> log.trace(" macd signal: ${i} - val : ${val}")}
		out[2].eachWithIndex { val,i -> log.trace(" macd histogram: ${i} - val : ${val}")}
	}
	



	@Test
	public void rocTest() {
		double[] out= Indicators.roc(0,prices.length,prices, 30);
		out.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
	}
	@Test
	public void atrTest() {
		double[] out= Indicators.atr(0,prices.length,highs,lows, 14);
		out.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
	}



}
