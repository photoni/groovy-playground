package ta

import groovy.util.logging.Slf4j
import model.Security
import org.junit.Test
import service.SecurityService

/**
 * groovy-playground
 * Created by filippo on 21/09/14.
 */
@Slf4j
class ADXTest {

    /*@Test
    public void adxTest() {
        double[] out=ADX.adx(0, prices.length, prices, highs, lows, 14)
        out.eachWithIndex { val,i -> log.trace(" adx: ${i} - val : ${val}")}

    }

    @Test
    public void adxQATest() {
        def setup=setupAdxQaTest()
        double[] out=ADX.adx(0, setup['prices'].length, setup['prices'], setup['highs'], setup['lows'], 14)
        out.eachWithIndex { val,i -> log.trace(" adx: ${i} - val : ${val}")}

    }*/

    /**
     * True Range Methodology1
     */
    @Test
    public void trM1QATest() {
        def setup=setupAdxQaTest()
        double[] trM1=Indicators.trM1(0, setup['prices'].length-1, setup['highs'], setup['lows'])
        assertValidTRM1(trM1)
    }

    /**
     * DMPlus not combined
     */
    @Test
    public void dmPlusQATest() {
        def setup=setupAdxQaTest()
        double[] dmPlus=ADX.dm(0, setup['prices'].length, setup['highs'],false)
        assertValidDMPlus(dmPlus)
    }

    /**
     * DMMinus not combined
     */
    @Test
    public void dmMinusQATest() {
        def setup=setupAdxQaTest()
        double[] dmMinus=ADX.dm(0, setup['prices'].length, setup['lows'],true)
        assertValidDMMinus(dmMinus)
    }

    /**
     * +DM -DM without smoothing
     */
    @Test
    public void dmCombinedQATest() {
        def setup=setupAdxQaTest()

        double[][] dmCombined=ADX.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
        double[] dmMinus=dmCombined[1];
        double[] dmPlus=dmCombined[0];

        log.debug("DM MINUS")
        assertValidDMMinusCombined(dmMinus)

        log.debug("DM PLUS")
        assertValidDMPlusCombined(dmPlus)
    }


    /**
     * +DM14 and -DM14
     */
    @Test
    public void dmSmoothedQATest() {
        def setup=setupAdxQaTest()
        double[][] dmCombined=ADX.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
        double[] dmPlus=dmCombined[0];
        double[] dmPlusSmoothed14=Smooth.wSmoothed1Iterator(0,dmPlus.length,dmPlus,14)
        assertValidDM14Plus(dmPlusSmoothed14)
        log.debug("DM PLUS SMOOTHED")
        double[] dmMinus=dmCombined[1];
        double[] dmMinusSmoothed14=Smooth.wSmoothed1Iterator(0,dmMinus.length,dmMinus,14)
        assertDM14Minus(dmMinusSmoothed14)
        log.debug("DM MINUS SMOOTHED")

    }

    /**
     * +DI4 and -DI4
     */
    @Test
    public void diCombinedQATest() {
        def setup=setupAdxQaTest()
        double[][] dmCombined=ADX.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
        double[] dmPlus=dmCombined[0];
        double[] dmPlusSmoothed14=Smooth.wSmoothed1Iterator(0,dmPlus.length,dmPlus,14)

        double[] dmMinus=dmCombined[1];
        double[] dmMinusSmoothed14=Smooth.wSmoothed1Iterator(0,dmMinus.length,dmMinus,14)

        double[] trM1=Indicators.trM1(0, setup['prices'].length, setup['highs'], setup['lows'])
        double[] trM1Smoothed14=Smooth.wSmoothed1Iterator(0,trM1.length,trM1,14)

        double[] diPlus=ADX.di(dmPlusSmoothed14,trM1Smoothed14)
        assertValidDIPlus(diPlus)

        double[] diMinus=ADX.di(dmMinusSmoothed14,trM1Smoothed14)
        assertValidDIMinus(diPlus)
    }

    /**
     * DX 14
     */
    @Test
    public void dxQATest() {
        def setup=setupAdxQaTest()
        double[] dx=ADX.dx(0, setup['prices'].length, setup['highs'], setup['lows'],14)
        assertIsValidDX(dx)
    }

    /**
     * ADX 14
     */
    @Test
    public void adxQATest() {
        def setup=setupAdxQaTest()
        double[] adx=ADX.adx(0, setup['prices'].length, setup['highs'], setup['lows'],14)
        assert adx[473]==28.114437170585955
        assert adx[474]==29.954292323573434
        assert adx[475]==32.25667407943681
        assert adx[476]==33.70788849599704
    }



    @Test
    public void trM1SmoothedQATest() {
        def setup=setupAdxQaTest()
        double[] trM1=Indicators.trM1(0, setup['prices'].length, setup['highs'], setup['lows'])
        assertValidTRM1(trM1)
        double[] trM1Smoothed14=Smooth.wSmoothed1Iterator(0,trM1.length,trM1,14)
        assertValidTRM1Smoothed14(trM1Smoothed14)

    }
    /* Helpers */
    def setupAdxQaTest() {
        SecurityService ss = SecurityService.instance
        Map<String, String> mapping = new HashMap<String, String>()
        mapping.put("Date", "dateAsString")
        mapping.put("Close", "adjClose")
        mapping.put("High", "high");
        mapping.put("Low", "low");
        Security s = ss.getSecurityFromCsv('cs-adx.csv', mapping, "dd-MMM-yy", true)
        def myPrices = new double[s.getHistory().size()]
        def myHighs = new double[s.getHistory().size()]
        def myLows = new double[s.getHistory().size()]
        s.getHistory().eachWithIndex { obj, i ->
            myPrices[i] = obj.adjClose; myHighs[i] = obj.high; myLows[i] = obj.low;
            log.trace(" index: ${i}: - date: ${obj.dateAsString} - close: ${obj.adjClose} - high: ${obj.high} - low: ${obj.low}")
        }
        def result=[:]
        result['prices']=myPrices
        result['highs']=myHighs
        result['lows']=myLows
        return result
    }

    private void assertValidTRM1(double[] trM1) {
        assert trM1[0] == 0.36999999999999744
        assert trM1[1] == 0.509999999999998
        assert trM1[489] == 0.7800000000000011
        assert trM1[490] == 0.740000000000002
        assert trM1[491] == 1.0300000000000011
        assert trM1[492] == 0.5899999999999999
        assert trM1[493] == 1.240000000000002
        assert trM1[494] == 1.0200000000000031
        assert trM1[495] == 1.0100000000000016
        assert trM1[496] == 1.3000000000000007
        assert trM1[497] == 0.75
        assert trM1[498] == 0.879999999999999
        assert trM1[499] == 0.7900000000000027
        assert trM1[500] == 0.610000000000003
        assert trM1[501] == 0.48999999999999844
        assert trM1[502] == 0.9600000000000009
    }

    private void assertValidTRM1Smoothed14(double[] trM1Smoothed14) {
        assert trM1Smoothed14[0] == 8.679368149497945
        assert trM1Smoothed14[1] == 8.835163015573592
        assert trM1Smoothed14[20] == 6.718781265155942
        assert trM1Smoothed14[486] == 12.640557580174939
        assert trM1Smoothed14[487] == 12.35290816326532
        assert trM1Smoothed14[488] == 12.12928571428573
        assert trM1Smoothed14[489] == 12.190000000000015
        assert trM1Smoothed14[490] == 0.0
        assert trM1Smoothed14[502] == 0.0
    }

    private void assertValidDMPlus(double[] dmPlus) {
        assert dmPlus.length == 504
        assert dmPlus[0] == 0.04999999999999716
        assert dmPlus[1] == 0.10000000000000142
        assert dmPlus[484] == 0.6000000000000014
        assert dmPlus[485] == 0.5700000000000003
        assert dmPlus[489] == 0.5399999999999991
        assert dmPlus[493] == 0.0
        assert dmPlus[494] == 0.17999999999999972
        assert dmPlus[495] == 0.0
        assert dmPlus[500] == 0.0
        assert dmPlus[501] == 0.16999999999999815
        assert dmPlus[502] == 0.08000000000000185
        assert dmPlus[503] == 0.0
    }

    private void assertValidDMMinus(double[] dmMinus) {
        assert dmMinus.length == 504
        assert dmMinus[0] == 0.0
        assert dmMinus[4] == 0.44000000000000483
        assert dmMinus[488] == 0.5
        assert dmMinus[489] == 0.0
        assert dmMinus[490] == 0.05000000000000071
        assert dmMinus[491] == 0.9100000000000001
        assert dmMinus[492] == 0.3099999999999987
        assert dmMinus[493] == 0.4299999999999997
        assert dmMinus[496] == 0.6499999999999986
        assert dmMinus[497] == 0.33000000000000185
        assert dmMinus[498] == 0.14999999999999858
        assert dmMinus[499] == 0.17999999999999972
        assert dmMinus[500] == 1.2200000000000024
        assert dmMinus[501] == 0.0
        assert dmMinus[502] == 0.08999999999999986
        assert dmMinus[503] == 0.0
    }

    private void assertValidDMMinusCombined(double[] dmMinus) {
        assert dmMinus.length == 504
        assert dmMinus[0] == 0.0
        assert dmMinus[4] == 0.44000000000000483
        assert dmMinus[487] == 0.6999999999999993
        assert dmMinus[488] == 0.5
        assert dmMinus[489] == 0.0
        assert dmMinus[490] == 0.05000000000000071
        assert dmMinus[491] == 0.9100000000000001
        assert dmMinus[492] == 0.3099999999999987
        assert dmMinus[493] == 0.4299999999999997
        assert dmMinus[496] == 0.6499999999999986
        assert dmMinus[497] == 0.33000000000000185
        assert dmMinus[498] == 0.14999999999999858
        assert dmMinus[499] == 0.17999999999999972
        assert dmMinus[500] == 1.2200000000000024
        assert dmMinus[501] == 0.0
        assert dmMinus[502] == 0.08999999999999986
        assert dmMinus[503] == 0.0
    }

    private void assertValidDMPlusCombined(double[] dmPlus) {
        assert dmPlus.length == 504
        assert dmPlus[0] == 0.04999999999999716
        assert dmPlus[1] == 0.10000000000000142
        assert dmPlus[484] == 0.6000000000000014
        assert dmPlus[485] == 0.5700000000000003
        assert dmPlus[488] == 0.0
        assert dmPlus[489] == 0.5399999999999991
        assert dmPlus[493] == 0.0
        assert dmPlus[494] == 0.17999999999999972
        assert dmPlus[495] == 0.0
        assert dmPlus[500] == 0.0
        assert dmPlus[501] == 0.16999999999999815
        assert dmPlus[502] == 0.0
        assert dmPlus[503] == 0.0
    }

    private void assertValidDM14Plus(double[] dmPlusSmoothed14) {
        assert dmPlusSmoothed14.length == 503
        assert dmPlusSmoothed14[0] == 2.9257753299932308
        assert dmPlusSmoothed14[1] == 3.0244863611323423
        assert dmPlusSmoothed14[20] == 1.9960078210736771
        assert dmPlusSmoothed14[480] == 2.4438658174914862
        assert dmPlusSmoothed14[481] == 2.265701649606216
        assert dmPlusSmoothed14[482] == 2.2892171611143857
        assert dmPlusSmoothed14[483] == 2.3791569427385673
        assert dmPlusSmoothed14[484] == 1.7437074767953824
        assert dmPlusSmoothed14[485] == 1.2316849750104102
        assert dmPlusSmoothed14[486] == 0.71258381924198
        assert dmPlusSmoothed14[487] == 0.7673979591836708
        assert dmPlusSmoothed14[488] == 0.8264285714285686
        assert dmPlusSmoothed14[489] == 0.889999999999997
        assert dmPlusSmoothed14[490] == 0.0
        assert dmPlusSmoothed14[502] == 0.0
    }

    private void assertDM14Minus(double[] dmMinusSmoothed14) {
        assert dmMinusSmoothed14[0] == 1.7218984149414427
        assert dmMinusSmoothed14[1] == 1.8936015889151379
        assert dmMinusSmoothed14[20] == 1.1036817362655438
        assert dmMinusSmoothed14[480] == 2.9616229333465305
        assert dmMinusSmoothed14[481] == 3.1894400820654942
        assert dmMinusSmoothed14[482] == 3.434781626839763
        assert dmMinusSmoothed14[483] == 3.698995598135129
        assert dmMinusSmoothed14[484] == 3.9835337210686004
        assert dmMinusSmoothed14[485] == 4.289959391920031
        assert dmMinusSmoothed14[486] == 4.619956268221572
        assert dmMinusSmoothed14[487] == 4.889183673469387
        assert dmMinusSmoothed14[488] == 4.511428571428572
        assert dmMinusSmoothed14[489] == 4.32
        assert dmMinusSmoothed14[490] == 0.0
        assert dmMinusSmoothed14[502] == 0.0
    }

    private void assertValidDIMinus(double[] diPlus) {
        assert diPlus[485] == 33.1330849725;
        assert diPlus[486] == 36.548674684;
        assert diPlus[487] == 39.5792117034;
        assert diPlus[488] == 37.1945115129;
        assert diPlus[489] == 35.4388843314;
        assert diPlus[490] == 0;
        assert diPlus[502] == 0;
    }

    private void assertValidDIPlus(double[] diPlus) {
        assert diPlus[485] == 9.5127993549945;
        assert diPlus[486] == 5.6372815417539375;
        assert diPlus[487] == 6.21228579571031;
        assert diPlus[488] == 6.8134974383133775;
        assert diPlus[489] == 7.301066447908087;
        assert diPlus[490] == 0;
        assert diPlus[502] == 0;
    }

    private void assertIsValidDX(double[] dx) {
        assert dx[0] == 25.90278451373333
        assert dx[472] == 10.34107375609986
        assert dx[473] == 4.196320181748763
        assert dx[474] == 0.023329497349510703
        assert dx[475] == 13.390886664153806
        assert dx[476] == 13.390886664153806
        assert dx[477] == 1.873176216898483
        assert dx[478] == 6.450091331636891
        assert dx[479] == 6.450091331636888
        assert dx[480] == 9.57835895551122
        assert dx[481] == 16.933353483672033
        assert dx[482] == 20.013359683725945
        assert dx[483] == 21.714470746185693
        assert dx[484] == 39.10829257738574
        assert dx[485] == 55.387022663499764
        assert dx[486] == 73.27413174381128
        assert dx[487] == 72.86707736157095
        assert dx[488] == 69.03519336277274
        assert dx[489] == 65.83493282149722
    }

}
