package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test

/**
 * groovy-playground
 * Created by filippo on 26/10/14.
 */
@Slf4j
class AroonTest {

    @Test
    def void aroonUpTest(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37] as double[]
        double[] aroonUp=Aroon.aroonUp(highs,25)
        ArrayHelper.log(aroonUp,log,true)

    }

    @Test
    def void aroonDownTest(){
        def lows=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,9,10,9,13,14,15,16,17,18,19,20,21,21,22,23,24,25,25,26,27,28] as double[]
        double[] aroonDown=Aroon.aroonUp(lows,25)
        ArrayHelper.log(aroonDown,log,true)

    }
}
