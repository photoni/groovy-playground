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
	public void macdTest() {
		double[][] out=Indicators.macd(0,prices.length,prices); //OK fine
		out[0].eachWithIndex { val,i -> log.trace(" macd line lower: ${i} - val : ${val}")}
		out[1].eachWithIndex { val,i -> log.trace(" macd signal: ${i} - val : ${val}")}
		out[2].eachWithIndex { val,i -> log.trace(" macd histogram: ${i} - val : ${val}")}
	}
	
	@Test
	public void adxTest() {
		double[] out=Indicators.adx(0, prices.length, prices, highs, lows, 14)
		out.eachWithIndex { val,i -> log.trace(" adx: ${i} - val : ${val}")}
		
	}

    @Test
    public void adxQATest() {
        def setup=setupAdxQaTest()
        double[] out=Indicators.adx(0, setup['prices'].length, setup['prices'], setup['highs'], setup['lows'], 14)
        out.eachWithIndex { val,i -> log.trace(" adx: ${i} - val : ${val}")}

    }
    @Test
    public void trQATest() {
        def setup=setupAdxQaTest()
        double[] trM2=Indicators.trM2(0, setup['prices'].length, setup['highs'], setup['prices'])
        trM2.eachWithIndex {
            val,i -> log.debug(" tr: ${i} - val : ${val}")
        }

        double[] trM3=Indicators.trM3(0, setup['prices'].length, setup['lows'], setup['prices'])
        trM3.eachWithIndex {
            val,i -> log.debug(" tr: ${i} - val : ${val}")
        }

        double[] trM1=Indicators.trM1(0, setup['prices'].length, setup['highs'], setup['lows'])
        trM1.eachWithIndex {
            val,i -> log.debug(" tr: ${i} - val : ${val}")
        }

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

    /* Helpers */
    def setupAdxQaTest() {
        SecurityService ss = SecurityService.instance
        Map<String, String> mapping = new HashMap<String, String>()
        mapping.put("Date", "dateAsString")
        mapping.put("Close", "adjClose")
        mapping.put("High", "high");
        mapping.put("Low", "low");
        Security s = ss.getSecurityFromCsv('cs-adx.csv', mapping, "dd-MMM-yy", false)
        def myPrices = new double[s.getHistory().size()]
        def myHighs = new double[s.getHistory().size()]
        def myLows = new double[s.getHistory().size()]
        s.getHistory().eachWithIndex { obj, i ->
            myPrices[i] = obj.adjClose; myHighs[i] = obj.high; myLows[i] = obj.low;
            log.trace(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose} - high: ${obj.high} - low: ${obj.low}")
        }
        def result=[:]
        result['prices']=myPrices
        result['highs']=myHighs
        result['lows']=myLows
        return result
    }

}
