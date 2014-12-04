package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import util.ArrayUtil

import java.lang.reflect.Array

/**
 *
 * groovy-playground
 * Created by filippo on 25/10/14.
 */
@Slf4j
class MathAnalysisTest {
    @Test
    def void getElapsedMaxTest(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37] as double[]
        int[]results=MathAnalysis.getElapsedTrend(highs, 25, 1);
        log.debug('xElapsedMax');
        assertIsValidElapsedMax(results)
    }


    @Test
    def void getElapsedMinTest(){
        def lows=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,9,10,9,13,14,15,16,17,18,19,20,21,21,22,23,24,25,25,26,27,28] as double[]
        int[]results=MathAnalysis.getElapsedTrend(lows, 25, -1);
        log.debug('xElapsedMin');
        assertIsValidElapsedMin(results)
    }

    @Test
    def void getDeltaTest(){
        def values=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,8,10,9,13,14,15,16,17,18,19,20,18,17,14,16,24,25,25,26,27,
               25]
        double[][] deltas =MathAnalysis.deltas(values.reverse() as double[])
        double[] gains=deltas[0]
        double[] losses=deltas[1]
        log.debug('gains')
        ArrayHelper.log(gains,log,true)
        assert gains[2]==1
        assert gains[5]==8
        assert gains[6]==2

        log.debug('losses')
        ArrayHelper.log(losses,log,true)
        assert losses[0]==2
        assert losses[7]==3
        assert losses[8]==1


    }

    @Test
    def void highestHighTest(){
        def initialValues=[127.01,127.62,126.59,127.35,128.17,128.43,127.37,126.42,126.9,126.85,125.65,125.72,127.16,
                            127.72,127.69,128.22,128.27,128.09,128.27,127.74,128.77,129.29,130.06,129.12,129.29,128.47,
                            128.09,128.65,129.14,128.64]
        def revValues= initialValues.reverse() as double[]
        def result=ArrayHelper.closureIterator(0,revValues,14){int startIndex,double [] values,int periods ->
            return MathAnalysis.highestHigh(startIndex,values,periods)
        }
        log.debug('Highest High: {}',result)
        assert result[0]==130.06
        assert result[7]==130.06
        assert result[8]==129.29
        assert result[9]==128.77
        assert result[10]==128.27
        assert result[11]==128.43
        assert result[16]==128.43
        assert result[17]==0
        assert result[result.length-1]==0
    }

    private void assertIsValidElapsedMin(int[] results) {
        assert results[0] == 0
        assert results[1] == 1
        assert results[2] == 2
        assert results[3] == 3
        assert results[4] == 4
        assert results[5] == 5
        assert results[6] == 6
        assert results[7] == 7
        assert results[8] == 8
        assert results[9] == 9
        assert results[10] == 10
        assert results[11] == 11
        assert results[12] == 12
        assert results[13] == 13
        assert results[14] == 14
        assert results[15] == 0
        assert results[16] == 1
        assert results[17] == 2
        assert results[18] == 0
    }

    private void assertIsValidElapsedMax(int[] results) {
        assert results[0] == 0
        assert results[1] == 0
        assert results[2] == 0
        assert results[3] == 0
        assert results[4] == 0
        assert results[5] == 0
        assert results[6] == 0
        assert results[7] == 1
        assert results[8] == 2
        assert results[9] == 3
        assert results[10] == 4
        assert results[11] == 5
        assert results[12] == 0
        assert results[13] == 0
        assert results[14] == 0
        assert results[15] == 0
        assert results[16] == 0
        assert results[17] == 1
    }
}
