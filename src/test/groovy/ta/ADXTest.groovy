package ta

import groovy.util.logging.Slf4j
import model.Security
import org.junit.Test
import service.SecurityService

/**
 * groovy-playground
 * Created by filippo on 21/09/14.
 */
@Slf4j
class ADXTest {

    /*@Test
    public void adxTest() {
        double[] out=ADX.adx(0, prices.length, prices, highs, lows, 14)
        out.eachWithIndex { val,i -> log.trace(" adx: ${i} - val : ${val}")}

    }

    @Test
    public void adxQATest() {
        def setup=setupAdxQaTest()
        double[] out=ADX.adx(0, setup['prices'].length, setup['prices'], setup['highs'], setup['lows'], 14)
        out.eachWithIndex { val,i -> log.trace(" adx: ${i} - val : ${val}")}

    }*/

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

        double[] dmPlus=ADX.dm(0, setup['prices'].length, setup['highs'],false)
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
    public void dmPlusSmoothedQATest() {
        def setup=setupAdxQaTest()
        double[][] dmCombined=ADX.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
        double[] dmPlus=dmCombined[0];
        double[] dmPlusSmoothed14=Smooth.wSmoothed1Iterator(0,dmPlus.length-1,dmPlus,14)
        log.debug("DM PLUS SMOOTHED")
        dmPlusSmoothed14.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
        }

        double[] dmMinus=dmCombined[1];
        double[] dmMinusSmoothed14=Smooth.wSmoothed1Iterator(0,dmMinus.length-1,dmMinus,14)
        log.debug("DM MINUS SMOOTHED")
        dmMinusSmoothed14.eachWithIndex {
            val,i -> log.debug(" index: ${i} - val : ${val}")
        }



    }

    @Test
    public void dmMinusQATest() {
        def setup=setupAdxQaTest()

        double[] dmMinus=ADX.dm(0, setup['prices'].length, setup['lows'],true)
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

        double[][] dmCombined=ADX.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
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
                        break;
                    case 2:
                        assert val==0.5499999999999972
                        break;
                    case 3:
                        assert val==0.28000000000000114
                        break;
                    case 8:
                        assert val==0.0
                        break;
                    case 501:
                        assert val==0.16999999999999815
                        break;
                    case 502:
                        assert val==0.0
                        break;


                }

        }

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
