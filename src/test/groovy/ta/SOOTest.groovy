package ta

import data.TestDataSupport
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import util.StopWatch

/**
 * groovy-playground
 * Created by filippo on 30/10/14.
 */
@Slf4j
class SOOTest {
    double[] revHighs = TestDataSupport.SOO_INITIAL_CLOSE_VALUES.reverse() as double[]
    double[] revLow = TestDataSupport.SOO_INITIAL_CLOSE_VALUES.reverse() as double[]
    double[] revClose = TestDataSupport.SOO_INITIAL_CLOSE_VALUES.reverse() as double[]

    @Test
    @CompileStatic
    def void sooTest(){

        double[][] result
        def watch = new StopWatch('nanosecond')
        watch.withTimeRecording("total") {
            result=SOO.stochasticOscillator(0,revClose,14,3)

        }
        ArrayHelper.log(result,log,true)
        watch.printResult()
        assert result[0][0]==0
        assert result[0][1]==0
        assert result[0][13]==100.0
        assert result[0][14]==99.9135831565716
        assert result[0][15]==100.0
        assert result[0][16]==99.29692992734942
        assert result[0][20]==100.0
        assert result[0][21]==99.34737005671664
        assert result[0][26]==32.72727272727213
        assert result[0][27]==32.72727272727213
        assert result[0][28]==98.78787878787817
        assert result[0][29]==73.33333333333356


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
            result= ArrayHelper.closureIterator(0, revClose, 14){int start,double [] values,int prds ->
                return MathAnalysis.lowestLow(start,values,prds)
            }

        }
        ArrayHelper.log(result,log,true)
        watch.printResult()



    }



}
