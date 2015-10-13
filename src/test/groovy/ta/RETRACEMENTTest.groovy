package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import model.SecurityQuote
import org.junit.Ignore
import org.junit.Test
import service.SecurityService
import util.ArrayUtil

/**
 * groovy-playground
 * Created by filippo on 31/08/15.
 */
@Slf4j
class RETRACEMENTTest {

    @Test
    def void retracementTest(){
        double[] values=[100,105,106,105,105,109,111,115,120,130,131,132,137,140,142,135,130,120,115,114,110,109,108,
                         107,105,101,105,106,105,105,109,111,115,120,130,131,132,137,140,135,130,129,128,127,127,125,
                         124,123,127,128,130,131,132]
        RETRACEMENT retr= new RETRACEMENT();
        double[] result=retr.fibonacci(values,10)
        ArrayHelper.log(result,log,true)

    }

    @Test
    @Ignore
    def void retracementTestAAPL(){
        SecurityService ss= SecurityService.instance
        Security s=ss.getSecurity("AAPL");
        List<SecurityQuote> listVal= s.history
        double[] values=new double[listVal.size()]
        listVal.eachWithIndex { SecurityQuote entry, int i ->
            values[i]=entry.getAdjClose()
        }
        RETRACEMENT retr= new RETRACEMENT();
        double[] result=retr.fibonacci(ArrayUtil.reverse(values),10)
        ArrayHelper.log(result,log,true)

    }


}
