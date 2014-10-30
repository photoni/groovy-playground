package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test

/**
 * groovy-playground
 * Created by filippo on 30/10/14.
 */
@Slf4j
class RSITest {

    @Test
    def void rsTest(){
        def values=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,8,10,9,13,14,15,16,17,18,19,20,18,17,14,16,24,25,25,26,27,
                    25]
        def reversedValues=values.reverse() as double[]
        double[] rs=RSI.rs(reversedValues,14)
        ArrayHelper.log(rs,log,true)
    }
    @Test
    def void averageGainTest(){
        def values=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,8,10,9,13,14,15,16,17,18,19,20,18,17,14,16,24,25,25,26,27,
                    25]
        def reversedValues=values.reverse() as double[]
        double[] rs=RSI.averageGain(reversedValues,14)
        ArrayHelper.log(rs,log,true)
    }

    @Test
    def void averageLossTest(){
        def values=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,8,10,9,13,14,15,16,17,18,19,20,18,17,14,16,24,25,25,26,27,
                    25]
        def reversedValues=values.reverse() as double[]
        double[] rs=RSI.averageLoss(reversedValues,14)
        ArrayHelper.log(rs,log,true)
    }
}
