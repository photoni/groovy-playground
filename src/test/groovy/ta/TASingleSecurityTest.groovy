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
    public void trM1QATest() {
        def setup=setupAdxQaTest()

        double[] trM1=Indicators.trM1(0, setup['prices'].length, setup['highs'], setup['lows'])
        trM1.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
            switch (i){
                case 0:
                    assert val==0.36999999999999744
                    break;
                case 1:
                    assert val==0.509999999999998
                    break;
                case 502:
                    assert val==0.9600000000000009
                    break;
                case 503:
                    assert val==0.7899999999999991
                    break;


            }


        }

    }

    @Test
    public void dmPlusQATest() {
        def setup=setupAdxQaTest()

        double[] dmPlus=Indicators.dm(0, setup['prices'].length, setup['highs'],false)
        dmPlus.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
            switch (i){
                case 0:
                    assert val==0.04999999999999716
                    break;
                case 1:
                    assert val==0.10000000000000142
                    break;
                case 502:
                    assert val==0.08000000000000185
                    break;
                case 503:
                    assert val==0.0
                    break;


            }

        }

    }

    @Test
    public void dmMinusQATest() {
        def setup=setupAdxQaTest()

        double[] dmMinus=Indicators.dm(0, setup['prices'].length, setup['lows'],true)
        dmMinus.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
                switch (i){
                    case 0:
                        assert val==0.0
                        break;
                    case 4:
                        assert val==0.44000000000000483
                        break;
                    case 502:
                        assert val==0.08999999999999986
                        break;
                    case 500:
                        assert val==1.2200000000000024
                        break;


                }

        }

    }


    @Test
    public void dmCombinedQATest() {
        def setup=setupAdxQaTest()

        double[][] dmCombined=Indicators.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
        double[] dmMinus=dmCombined[1];
        double[] dmPlus=dmCombined[0];

        log.debug("DM MINUS")
        dmMinus.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
                switch (i){
                    case 0:
                        assert val==0.0
                        break;
                    case 4:
                        assert val==0.44000000000000483
                        break;
                    case 5:
                        assert val==0.0
                        break;
                    case 7:
                        assert val==0.0799999999999983
                        break;
                    case 8:
                        assert val==1.4500000000000028
                        break;
                    case 501:
                        assert val==0.0
                        break;
                    case 502:
                        assert val==0.08999999999999986
                        break;


                }

        }

        log.debug("DM PLUS")
        dmPlus.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
                switch (i){
                    case 0:
                        assert val==0.04999999999999716
                        break;
                    case 1:
                        assert val==0.10000000000000142
                    case 2:
                        assert val==0.5499999999999972
                        break;
                    case 3:
                        assert val==0.28000000000000114
                        break;
                    case 8:
                        assert val==1.4500000000000028
                        break;
                    case 501:
                        assert val==0.0
                        break;
                    case 502:
                        assert val==0.08999999999999986
                        break;


                }

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
        Security s = ss.getSecurityFromCsv('cs-adx.csv', mapping, "dd-MMM-yy", true)
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
