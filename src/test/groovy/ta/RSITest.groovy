package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.junit.Test
import service.SecurityService

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

    @Test
    def void gainLossQATest(){
        def setup=setupRsiQaTest()
        double[][] deltas =MathAnalysis.deltas setup['prices']
        double[] gains=deltas[0]
        assertIsValidGains gains
        double[] losses=deltas[1]
        assertIsValidLosses losses
    }

    @Test
    def void AverageGainLossQATest(){
        /*
            0.2384
            0.2214
            0.2077
            0.2199
            0.2042
            0.1896
            0.2167
            0.2040
            0.1895
            0.2286
            0.2123
            0.1971
            0.1831
            0.1807
            0.1389
            0.1539
            0.1429
            0.1327
            0.1567

         */
        def setup=setupRsiQaTest()
        double[] ag=RSI.averageGain setup['prices'],14
        ArrayHelper.log(ag,log,true)
        assert ag[18]==0.23838571428571445
        assert ag[17]==0.22135816326530627
        assert ag[16]==0.2076897230320702
        assert ag[15]==0.2199118856726365
        assert ag[14]==0.20420389383887677
        assert ag[13]==0.18961790142181414
        assert ag[12]==0.21666662274882748
        assert ag[11]==0.20404043540962521
        assert ag[10]==0.18946611859465198
        assert ag[9]==0.2286328244093197
        assert ag[8]==0.2123019083800826
        assert ag[7]==0.19713748635293382
        assert ag[6]==0.18305623732772425
        assert ag[5]==0.18065936323288703
        assert ag[4]==0.17059798014482364
        assert ag[3]==0.18938158699329416


        //double[] al=RSI.averageLoss setup['prices'],14
        //ArrayHelper.log(al,log,true)
    }




    /* Helpers */
    def setupRsiQaTest() {
        SecurityService ss = SecurityService.instance
        Map<String, String> mapping = new HashMap<String, String>()
        mapping.put("Date", "dateAsString")
        mapping.put("Close", "adjClose")
        Security s = ss.getSecurityFromCsv('cs-rsi.csv', mapping, "dd-MMM-yy", true)
        def myPrices = new double[s.getHistory().size()]
        s.getHistory().eachWithIndex { obj, i ->
            myPrices[i] = obj.adjClose;
            log.trace(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose}")
        }
        def result=[:]
        result['prices']=myPrices
        return result
    }

    private void assertIsValidGains(double[] gains) {
        assert gains[0] == 0.4686000000000021
        assert gains[1] == 0.0
        assert gains[2] == 0.0
        assert gains[3] == 0.34909999999999997
        assert gains[4] == 0.03979999999999961
        assert gains[5] == 0.1495000000000033
        assert gains[6] == 0.0
        assert gains[7] == 0.0
        assert gains[8] == 0.0
        assert gains[9] == 0.7378
        assert gains[10] == 0.0
        assert gains[11] == 0.03989999999999583
        assert gains[12] == 0.5683000000000007
        assert gains[13] == 0.0
        assert gains[14] == 0.0
        assert gains[15] == 0.37879999999999825
        assert gains[16] == 0.030000000000001137
        assert gains[17] == 0.0
        assert gains[18] == 0.0
        assert gains[19] == 0.6679999999999993
        assert gains[20] == 0.0
        assert gains[21] == 0.13970000000000482
        assert gains[22] == 0.0
        assert gains[23] == 0.23930000000000007
        assert gains[24] == 0.4187999999999974
        assert gains[25] == 0.3290000000000006
        assert gains[26] == 0.26910000000000167
        assert gains[27] == 0.49859999999999616
        assert gains[28] == 0.7154000000000025
        assert gains[29] == 0.0
        assert gains[30] == 0.059499999999999886

    }

    private void assertIsValidLosses(double[] losses) {
        assert losses[0] == 0.0
        assert losses[1] == 0.7576999999999998
        assert losses[2] == 1.1467000000000027
        assert losses[3] == 0.0
        assert losses[4] == 0.0
        assert losses[5] == 0.0
        assert losses[6] == 1.3260000000000005
        assert losses[7] == 0.4286999999999992
        assert losses[8] == 0.6680000000000064
        assert losses[9] == 0.0
        assert losses[10] == 0.5383999999999958
        assert losses[11] == 0.0
        assert losses[12] == 0.0
        assert losses[13] == 0.5782999999999987
        assert losses[14] == 0.18939999999999912
        assert losses[15] == 0.0
        assert losses[16] == 0.0
        assert losses[17] == 0.2791999999999959
        assert losses[18] == 0.0
        assert losses[19] == 0.0
        assert losses[20] == 0.4188000000000045
        assert losses[21] == 0.0
        assert losses[22] == 0.18950000000000244
        assert losses[23] == 0.0
        assert losses[24] == 0.0
        assert losses[25] == 0.0
        assert losses[26] == 0.0
        assert losses[27] == 0.0
        assert losses[28] == 0.0
        assert losses[29] == 0.5373000000000019
        assert losses[30] == 0.0
        assert losses[31] == 0.24869999999999948
        assert losses[32] == 0.0
    }
}
