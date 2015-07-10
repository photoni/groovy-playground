package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.junit.Ignore
import org.junit.Test
import service.SecurityService
import util.ArrayUtil

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
        double[] aroonUp=Aroon.aroonUp(highs,10)
        ArrayHelper.log(aroonUp,log,true)
        assert aroonUp[0]==100
        assert aroonUp[9]==100
        assert aroonUp[10]==60
        assert aroonUp[11]==50
        assert aroonUp[12]==100
        assert aroonUp[16]==100
        assert aroonUp[17]==90
        assert aroonUp[19]==70
        assert aroonUp[20]==100
        assert aroonUp[aroonUp.size()-1]==100


    }

    @Test
    /*
    QA=OK
    */
    def void aroonDownTest(){
        def lows=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46] as double[]
        double[] aroonDown=Aroon.aroonDown(lows,10)
        ArrayHelper.log(aroonDown,log,true)
        assert aroonDown[0]==100
        assert aroonDown[12]==90
        assert aroonDown[13]==80
        assert aroonDown[17]==40
        assert aroonDown[20]==10
        assert aroonDown[21]==0
        assert aroonDown[22]==70
        assert aroonDown[28]==10
        assert aroonDown[29]==0
        assert aroonDown[aroonDown.size()-1]==0


    }

    @Test
    /*
    QA=OK
   */
    def void aroonOscillator(){
        def prices=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,
                   37,38,39,40,41,42,43,44,45,46] as double[]

        double[] aroonOscillator=Aroon.aroonOscillator(prices,10)

        assert aroonOscillator[0]==0
        assert aroonOscillator[10]==60
        assert aroonOscillator[11]==50
        assert aroonOscillator[12]==10
        assert aroonOscillator[21]==100
        assert aroonOscillator[22]==30
        assert aroonOscillator[23]==40
        assert aroonOscillator[24]==50
        assert aroonOscillator[25]==60
        assert aroonOscillator[29]==100
        assert aroonOscillator[aroonOscillator.size()-1]==100

        ArrayHelper.log(aroonOscillator,log,true)


    }

    @Test
    @Ignore
    def void aroonOscillatorGoogle(){
        SecurityService ss= SecurityService.instance
        Security s=ss.getSecurity("GOOGL");
        double[] prices=new double[s.getHistory().size()];
        s.getHistory().eachWithIndex { obj, i -> log.debug(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose}");
            prices[i]=obj.adjClose}

        double[] aroonOscillator=Aroon.aroonOscillator(ArrayUtil.reverse(prices),25)


        ArrayHelper.log(aroonOscillator,log,true)


    }


    @Test
    def void aroonSignal(){
        def prices=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,
                   37,38,39,40,41,42,43,44,45,46] as double[]

        byte[] aroonSignal=Aroon.aroonSignal(prices,25,50,-50)

        assert aroonSignal[0]==0
        assert aroonSignal[1]==0
        assert aroonSignal[32]==1
        assert aroonSignal[33]==1
        assert aroonSignal[34]==1
        assert aroonSignal[37]==1
        assert aroonSignal[38]==1
        assert aroonSignal[39]==1
        assert aroonSignal[40]==1
        assert aroonSignal[41]==1
        assert aroonSignal[42]==1
        assert aroonSignal[43]==1
        assert aroonSignal[45]==1
        assert aroonSignal[aroonSignal.size()-1]==1


        ArrayHelper.log(aroonSignal,log,true)

    }
}
