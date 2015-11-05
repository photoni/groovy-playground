package ta

import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import util.ArrayUtil
import util.MathUtil

@Slf4j
class KAMATest {

    @Test
    public void changeTest(){
        double[] result=KAMA.change(TestDataSupport.KAMA_VALUES,10);

        for (int i = 0; i < result.length; i++) {
            result[i]=MathUtil.nDecimal(result[i],2);
        }
        ArrayHelper.log(result,log,true)
    }



}
