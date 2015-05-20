package ta

import data.TestDataSupport
import groovy.util.logging.Slf4j
import org.junit.Test

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
    public void emaTest() {

        double[] out1=MA.ema(0,TestDataSupport.SMA_VALUES.size(),TestDataSupport.SMA_VALUES,10);
        log.debug("-------- EMA ---------");
        out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}
        assert out1[8]==0
        assert out1[9]==125.881
        assert out1[10]==125.64081818181818
        assert out1[18]==126.23071955719182
        assert out1[19]==126.20054362279278

    }

    @Test
    public void macdTest() {
        double[][] out=MA.macd(0,TestDataSupport.MACD_VALUES.size(),TestDataSupport.MACD_VALUES);
        out[0].eachWithIndex { val,i -> log.trace(" macd line lower: ${i} - val : ${val}")}
        out[1].eachWithIndex { val,i -> log.trace(" macd signal: ${i} - val : ${val}")}
        out[2].eachWithIndex { val,i -> log.trace(" macd histogram: ${i} - val : ${val}")}
    }
}
