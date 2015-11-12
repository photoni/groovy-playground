package ta

import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.apache.commons.lang.ArrayUtils
import org.junit.Ignore
import org.junit.Test
import service.SecurityService
import util.MathUtil

@Slf4j
class MATest {

    @Test
    /*
      QA=OK
     */
    public void smaTest() {

        double[] out1=MA.sma(0,TestDataSupport.SMA_VALUES.size(),TestDataSupport.SMA_VALUES,5); //OK fine
        log.debug("-------- SMA ---------");
        out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
        assert out1[3]==0
        assert out1[4]==125.872
        assert out1[5]==126.096
        assert out1[29]==127.27799999999999
    }

    @Test
    /*
      QA=OK
     */
    public void ema10Test() {

        double[] out1=MA.ema(0,TestDataSupport.SMA_VALUES.size(),TestDataSupport.SMA_VALUES,10);
        log.debug("-------- EMA 10---------");
        out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
        assert out1[8]==0
        assert out1[9]==125.881
        assert out1[10]==125.64081818181818
        assert out1[18]==126.23071955719182
        assert out1[19]==126.20054362279278

    }
    @Test
    /*
      QA=OK
     */
    public void ema12Test() {

        double[] out1=MA.ema(0,TestDataSupport.MACD_VALUES.size(),TestDataSupport.MACD_VALUES,12);
        log.debug("-------- EMA 12---------");
        assert out1[11]==28.675
        assert out1[12]==28.317307692307693
        assert out1[22]==27.55662323187996
        assert out1[23]==27.79046559697763
        assert out1[33]==29.58674225206244
        assert out1[52]==32.633968851578935
        assert out1[55]==33.138097244770854
        out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
    }

    @Test
    /*
      QA=OK
     */
    public void ema26Test() {
        double[] out1=MA.ema(0,TestDataSupport.MACD_VALUES.size(),TestDataSupport.MACD_VALUES,26);
        log.debug("-------- EMA 26---------");
        assert out1[25]==28.01807692307692
        assert out1[26]==28.084145299145295
        assert out1[50]==31.46877160099621
        assert out1[51]==31.598268441769708
        assert out1[52]==31.688336977804553
        assert out1[53]==31.809660054758446
        assert out1[54]==31.952431159717882
        out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
    }

    @Test
    /*
      QA_MACD_LINE=OK
      QA_MACD_SIGNAL=OK
      QA_MACD_HISTOGRAM=OK
     */
    public void macdTest() {
        double[][] out=MA.macd(0,TestDataSupport.MACD_VALUES.size(),TestDataSupport.MACD_VALUES,12,26);
        assert out[0][25]==0.20380053583966884
        assert out[0][26]==0.24674034266928757
        assert out[0][27]==0.41003825490876267
        assert out[0][28]==0.4946877085048378
        assert out[0][29]==0.5421220603720194
        assert out[0][30]==0.6513165481654788
        assert out[0][31]==0.659658926871348
        assert out[0][32]==0.5968873337506793
        assert out[0][33]==0.5752591669894471
        assert out[0][52]==0.9456318737743814
        assert out[0][53]==0.9696966217911296
        assert out[0][54]==1.0116040765985659
        out[0].eachWithIndex { val,i -> log.trace(" macd line lower: ${i} - val : ${val}")}
        out[1].eachWithIndex { val,i -> log.trace(" macd signal: ${i} - val : ${val}")}
        out[2].eachWithIndex { val,i -> log.trace(" macd histogram: ${i} - val : ${val}")}
    }


    @Test
    public void kamaTest() {
        double[] result=MA.kama(TestDataSupport.KAMA_VALUES,10,2,30);

        for (int i = 0; i < result.length; i++) {
            result[i]=MathUtil.nDecimal(result[i],2);
        }
        ArrayHelper.log(result,log,true)

    }


}
