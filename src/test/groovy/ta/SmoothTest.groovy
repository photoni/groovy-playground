package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.junit.Test
import service.SecurityService

/**
 * groovy-playground
 * Created by filippo on 30/10/14.
 */
@Slf4j
class SmoothTest {

    @Test
    def void smooth2Test(){
        def setup=setupRsiQaTest()
        double[] ag=Smooth.wSmoothedIterator(0,setup.'prices'.length,setup.'prices',14,2)
        ArrayHelper.log(ag,log,true)
    }

    /* Helpers */
    def setupRsiQaTest() {
        SecurityService ss = SecurityService.instance
        Map<String, String> mapping = new HashMap<String, String>()
        mapping.put("Date", "dateAsString")
        mapping.put("Close", "adjClose")
        Security s = ss.getSecurityFromCsv('cs-smooth.csv', mapping, "dd-MMM-yy", true)
        def myPrices = new double[s.getHistory().size()]
        s.getHistory().eachWithIndex { obj, i ->
            myPrices[i] = obj.adjClose;
            log.trace(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose}")
        }
        def result=[:]
        result['prices']=myPrices
        return result
    }


}
