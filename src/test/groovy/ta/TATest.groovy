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
		double[] out= ta.sma(prices,CUT) //OK fine
		double[] out1=Indicators.sma(0,prices.length,prices, 30); //OK fine
		out.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
		log.debug("-------- MY SMA ---------");
		out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
	}

	@Test
	public void bollingerBandsTest() {
		double[][] out=Indicators.bollingerBands(0,prices.length,prices, 30); //OK fine
		out[0].eachWithIndex { val,i -> log.trace(" boll lower: ${i} - val : ${val}")}
		out[1].eachWithIndex { val,i -> log.trace(" boll middle: ${i} - val : ${val}")}
		out[2].eachWithIndex { val,i -> log.trace(" boll higher: ${i} - val : ${val}")}
	}

	@Test
	public void wmaTest() {
		double[] out= ta.wma(prices,CUT)
		out.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
	}

	@Test
	public void emaTest() {
		double[] out= ta.ema(prices,CUT) // not OK looks odd
		double[] out1=Indicators.ema(0,prices.length,prices, 20);//OK looks fine
		out.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
		log.debug("-------- MY EMA ---------");
		out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
	}

	@Test
	public void tmaTest() {
		double[] out= ta.tma(prices,CUT)
		out.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
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
