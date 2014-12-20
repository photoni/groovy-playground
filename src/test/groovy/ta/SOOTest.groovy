package ta

import data.TestDataSupport
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.junit.Test
import service.SecurityService
import util.StopWatch

/**
 * groovy-playground
 * Created by filippo on 30/10/14.
 */
@Slf4j
class SOOTest {
    double[] revHighs = TestDataSupport.SOO_INITIAL_HIGH_VALUES.reverse() as double[]
    double[] revLow = TestDataSupport.SOO_INITIAL_LOW_VALUES.reverse() as double[]
    double[] revClose = TestDataSupport.SOO_INITIAL_CLOSE_VALUES.reverse() as double[]

    @Test
    @CompileStatic
    def void sooTest(){

        double[] result
        def watch = new StopWatch('nanosecond')
        watch.withTimeRecording("total") {
            result=SOO.stochasticOscillator(0, revHighs,
                    revLow, revClose,14)

        }
        ArrayHelper.log(result,log,true)
        watch.printResult()



    }

}
