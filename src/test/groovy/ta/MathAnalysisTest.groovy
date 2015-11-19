package ta

import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.junit.Test
import service.SecurityService
import util.ArrayUtil

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
        ArrayHelper.log(results,log,true)
        assertIsValidElapsedMax(results)
    }
    @Test
    def void getElapsedMaxTest1(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,12,13,22,23,24,25,26,27,28,29,30,31,32,33,34,35,
                   36,37] as double[]
        int[]results=MathAnalysis.getElapsedTrend(highs, 6, 1);
        log.debug('xElapsedMax');
        ArrayHelper.log(results,log,true)
        assertIsValidElapsedMax(results)
    }
    @Test
    def void getElapsedExtremaMaxTest(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,12,13,22,23,24,25,26,27,28,29,30,31,32,33,34,35,
                   36,37] as double[]
        int[]results=MathAnalysis.getElapsedExtrema(highs, 6, 1);
        log.debug('xElapsedExtremaMax');
        ArrayHelper.log(results,log,true)
        //assertIsValidElapsedMax(results)
    }


    @Test
    def void getElapsedMinTest(){
        def lows=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,9,10,9,13,14,15,16,17,18,19,20,21,21,22,23,24,25,25,26,27,28] as double[]
        int[]results=MathAnalysis.getElapsedTrend(lows, 25, -1);
        log.debug('xElapsedMin');
        assertIsValidElapsedMin(results)
    }
    @Test
    def void getElapsedExtremaMinTest(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,12,13,22,23,24,25,26,27,28,29,30,31,32,33,34,35,
                   36,37] as double[]
        int[]results=MathAnalysis.getElapsedExtrema(highs, 12, -1);
        log.debug('xElapsedExtremaMax');
        ArrayHelper.log(results,log,true)
        //assertIsValidElapsedMax(results)
    }

    @Test
    def void getElapsedExtremaMinGoogleTest(){
        SecurityService ss= SecurityService.instance
        Security s=ss.getSecurity("GOOGL");
        double[] prices=new double[s.getHistory().size()];
        s.getHistory().eachWithIndex { obj, i -> log.debug(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose}");
            prices[i]=obj.adjClose}
        int[]results=MathAnalysis.getElapsedExtrema(ArrayUtil.reverse(prices), 25, -1);
        ArrayHelper.log(results,log,true)
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
    def void slopeTest(){
        def values=[1,1,1,1,1,1,1,2,3,6,7,17,81,81,81,81,81,81,81,80,79,50,16,15,10,9,8,7,6,5,4,3.2,1,1,1,1,1] as
                double[]
        double[]slope =MathAnalysis.slope(values,5)
        log.debug('slope')
        ArrayHelper.log(slope,log,true)



    }

    @Test
    def void highestHighTest(){

        def revHighValues= TestDataSupport.SOO_INITIAL_HIGH_VALUES.reverse() as double[]
        def result=ArrayHelper.closureIterator(0,revHighValues,14){int startIndex,double [] values,int periods ->
            return MathAnalysis.highestHigh(startIndex,values,periods)
        }
        log.debug('Highest High: {}',result)
        assertIsValidHighestHigh(result)

        def revLowValues= TestDataSupport.SOO_INITIAL_LOW_VALUES.reverse() as double[]
        result=ArrayHelper.closureIterator(0,revLowValues,14){int startIndex,double [] values,int periods ->
            return MathAnalysis.lowestLow(startIndex,values,periods)
        }
        log.debug('Lowest Low: {}',result)
        assertIsValidLowestLow(result)

    }

    def void assertIsValidLowestLow(double[] result) {
        assert result[0] == 125.92
        assert result[1] == 125.92
        assert result[2] == 125.92
        assert result[3] == 125.92
        assert result[4] == 125.07
        assert result[5] == 124.57
        assert result[6] == 124.56
        assert result[16] == 124.56
        assert result[17] == 0
        assert result[result.length-1]==0
    }

    private void assertIsValidHighestHigh(double[] result) {
        assert result[0] == 130.06
        assert result[7] == 130.06
        assert result[8] == 129.29
        assert result[9] == 128.77
        assert result[10] == 128.27
        assert result[11] == 128.43
        assert result[16] == 128.43
        assert result[17] == 0
        assert result[result.length - 1] == 0
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
