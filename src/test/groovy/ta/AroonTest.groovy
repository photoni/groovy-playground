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
    /*
    QA=OK
     */
    def void aroonUpTest(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46] as double[]
        double[] aroonUp=Aroon.aroonUp(highs,25)
        assert aroonUp[0]==100
        assert aroonUp[7]==96
        assert aroonUp[8]==92
        assert aroonUp[9]==88
        assert aroonUp[10]==84
        assert aroonUp[11]==80
        assert aroonUp[17]==96
        assert aroonUp[18]==92
        assert aroonUp[19]==88
        assert aroonUp[aroonUp.size()-1]==100
        ArrayHelper.log(aroonUp,log,true)

    }

    @Test
    def void aroonDownTest(){
        def lows=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46] as double[]
        double[] aroonDown=Aroon.aroonDown(lows,25)
        ArrayHelper.log(aroonDown,log,true)

    }

    @Test
    def void aroonOscillator(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46] as double[]
        double[] aroonUp=Aroon.aroonUp(highs,25)

        def lows=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46] as double[]
        double[] aroonDown=Aroon.aroonUp(lows,25)

        double[] aroonOscillator=Aroon.aroonOscillator(highs,lows,25)

        ArrayHelper.log(aroonOscillator,log,true)


    }
}
