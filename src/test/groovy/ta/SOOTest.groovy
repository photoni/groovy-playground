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
                    revLow, revClose,14,3)

        }
        ArrayHelper.log(result,log,true)
        watch.printResult()
        assert result[0]==0
        assert result[1]==0
        assert result[2]==75.79672695951754
        assert result[3]==74.24633936261826
        assert result[4]==78.9836347975881
        assert result[5]==70.80103359173118
        assert result[6]==73.72490022775223
        assert result[7]==79.27912594438196
        assert result[8]==81.07225099332106
        assert result[9]==80.5481964315308
        assert result[15]==49.35587761674702
        assert result[16]==54.7504025764895


    }

    @Test
    @CompileStatic
    def void highestHighsQATest(){

        double[] result
        def watch = new StopWatch('nanosecond')
        watch.withTimeRecording("total") {
            result= ArrayHelper.closureIterator(0, revHighs, 14){int start,double [] values,int prds ->
                return MathAnalysis.highestHigh(start,values,prds)
            }

        }
        ArrayHelper.log(result,log,true)
        watch.printResult()



    }

    @Test
    @CompileStatic
    def void lowestLowsQATest(){

        double[] result
        def watch = new StopWatch('nanosecond')
        watch.withTimeRecording("total") {
            result= ArrayHelper.closureIterator(0, revLow, 14){int start,double [] values,int prds ->
                return MathAnalysis.lowestLow(start,values,prds)
            }

        }
        ArrayHelper.log(result,log,true)
        watch.printResult()



    }



}
