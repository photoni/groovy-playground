package ta

import data.TestDataSupport
import groovy.util.logging.Slf4j
import org.junit.Test

@Slf4j
class MATest {

    @Test
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
    public void emaTest() {

        double[] out1=MA.ema(0,TestDataSupport.SMA_VALUES.size(),TestDataSupport.SMA_VALUES,10);
        log.debug("-------- EMA ---------");
        out1.eachWithIndex { val,i -> log.trace(" index: ${i} - val : ${val}")}

    }
}
