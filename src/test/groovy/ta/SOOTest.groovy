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
    //double[] revHighs = TestDataSupport.SOO_INITIAL_CLOSE_VALUES.reverse() as double[]
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
        ArrayHelper.log(result[0],log,true)
        watch.printResult()
        assert result[0][0] == 0.0
        assert result[0][1] == 0.0
        assert result[0][2] == 0.0
        assert result[0][3] == 0.0
        assert result[0][4] == 0.0
        assert result[0][5] == 0.0
        assert result[0][6] == 0.0
        assert result[0][7] == 0.0
        assert result[0][8] == 0.0
        assert result[0][9] == 0.0
        assert result[0][10] == 0.0
        assert result[0][11] == 0.0
        assert result[0][12] == 0.0
        assert result[0][13] == 0.0
        assert result[0][14] == 68.88888888889004
        assert result[0][15] == 0.0
        assert result[0][16] == 28.42105263157887
        assert result[0][17] == 100.0
        assert result[0][18] == 49.09090909090906
        assert result[0][19] == 92.12121212121242
        assert result[0][20] == 93.33333333333252
        assert result[0][21] == 52.72727272727282
        assert result[0][22] == 63.6363636363641
        assert result[0][23] == 32.72727272727213
        assert result[0][24] == 32.72727272727213
        assert result[0][25] == 98.78787878787817
        assert result[0][26] == 73.33333333333356


    }
    @Test
    @CompileStatic
    def void highestHighsQATest(){

        double[] result
        def watch = new StopWatch('nanosecond')
        watch.withTimeRecording("total") {
            result= ArrayHelper.closureIterator(0, revClose, 14){int start,double [] values,int prds ->
                return MathAnalysis.highestHigh(start,values,prds)
            }

        }

        ArrayHelper.log(result,log,true)
        watch.printResult()
        assert result[0]==128.71
        assert result[1]==128.71
        assert result[2]==128.71
        assert result[3]==128.71
        assert result[4]==128.71
        assert result[5]==128.71
        assert result[6]==128.71
        assert result[7]==128.71
        assert result[8]==128.71
        assert result[9]==128.71
        assert result[10]==128.01
        assert result[11]==128.01
        assert result[12]==128.01
        assert result[13]==128.01
        assert result[17]==0.0
        assert result[18]==0.0
        assert result[19]==0.0
        assert result[20]==0.0
        assert result[21]==0.0
        assert result[22]==0.0
        assert result[23]==0.0
        assert result[24]==0.0
        assert result[25]==0.0
        assert result[26]==0.0



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

        assert result[1] == 127.06
        assert result[2] == 127.06
        assert result[3] == 127.06
        assert result[4] == 127.06
        assert result[5] == 127.06
        assert result[6] == 127.06
        assert result[7] == 127.06
        assert result[8] == 127.06
        assert result[9] == 127.06
        assert result[10] == 127.06
        assert result[11] == 127.06
        assert result[12] == 127.11
        assert result[13] == 127.11
        assert result[14] == 0.0
        assert result[15] == 0.0
        assert result[16] == 0.0
        assert result[17] == 0.0
        assert result[18] == 0.0
        assert result[19] == 0.0
        assert result[20] == 0.0
        assert result[21] == 0.0
        assert result[22] == 0.0
        assert result[23] == 0.0
        assert result[24] == 0.0
        assert result[25] == 0.0
        assert result[26] == 0.0




    }



}
