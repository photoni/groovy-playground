package stats

import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import org.vertx.java.core.json.impl.Json
import service.SecurityService
import ta.AROON
import ta.KAMA
import ta.MA
import ta.MathAnalysis
import ta.ROC
import ta.SOO
import util.ArrayUtil

@Slf4j
class PerformanceTest {
    @Test
    public void kamaPrformanceTest(){

        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        def eRP=30;//Efficiency Ratio periods
        def f5=5;// slowest constant 5 periods
        def f2=2;// fastest constant 2 periods
        def slow=30;// slowest constant 30 periods
        def regression20=20;// regression periods 20
        def regression5=5;// regression periods 5
        def threshold=0.03;//neutral trend boundaries
        def hypertrendSlopeThreshold=0.2
        final def reverse = ArrayUtil.reverse(prices)
        def ikama5Trend = KAMA.trend(reverse,eRP,f5,slow,regression20,threshold)

        def ikama2Trend = KAMA.trend(reverse,eRP,f2,slow,regression5,threshold)
        def kama2 = KAMA.kama(reverse,eRP,f2,slow);
        def ikama2Slope = MathAnalysis.slope(kama2,regression5)
        def hypertrend = KAMA.hypertrend(ikama2Slope,hypertrendSlopeThreshold)

        double[] ikamaConvergenceHyper=MathAnalysis.convergence(ikama5Trend,ikama2Trend,hypertrend)
        double[] ikamaConvergence=MathAnalysis.convergence(ikama5Trend,ikama2Trend)



        def perfHyper=Performance.gainSignal(ikamaConvergenceHyper,reverse,false)
        def perf=Performance.gainSignal(ikamaConvergence,reverse,false)
        ArrayHelper.log(perf,log,false)
       log.debug("----------")
        ArrayHelper.log(perfHyper,log,false)
        double capital=100;
        for (int i = 0; i < perfHyper.length; i++) {
            capital=capital+(capital*perfHyper[i])
        }
        log.debug("hyperCapital: {}",capital)
        capital=100;
        capital = compoundCapital(perf, capital)
        log.debug("capital: {}",capital)

    }

    @Test
    public void macdPrformanceTest(){
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
        double[][] out=MA.macd(0,reverse.size(),reverse,12,36);
        def centerLineCrossSignal=out[5];
        def compoundSignal=out[7];
        def perfcenter=Performance.gainSignal(centerLineCrossSignal,reverse,false)
        ArrayHelper.log(perfcenter,log,false)
        log.debug("----------")
        def perfCompound=Performance.gainSignal(compoundSignal,reverse,false)
        ArrayHelper.log(perfCompound,log,false)
        double capital=100;
        capital = compoundCapital(perfcenter, capital)
        log.debug("center: {}",capital)

        capital=100;
        capital = compoundCapital(perfCompound, capital)
        log.debug("compound: {}",capital)
    }

    @Test
    public void aroonPrformanceTest(){
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)

        def periods = 25
        def bullishThreshold=40
        def bearishThreshold=-40
        double[] aroonCompoundSignal=AROON.aroonCompoundSignal(reverse, periods, bullishThreshold, bearishThreshold)
        def perf=Performance.gainSignal(aroonCompoundSignal,reverse,false)
        ArrayHelper.log(perf,log,false)

        double capital=100;
        capital = compoundCapital(perf, capital)
        log.debug("center: {}",capital)


    }

    @Test
    public void aroonPrformanceBruteForceTest(){
        /* winners */
        /*
        capital: 110436.18258912767 - periods: 30 - bullishThreshold: 50 - bearishThreshold:-75
        capital: 127696.14304468011 - periods: 35 - bullishThreshold: 40 - bearishThreshold:-65
        */
         
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
        def bestCapital=0;
        /*
        def periods = 25
        def bullishThreshold=40
        def bearishThreshold=-40*/

        for (int periods=10;periods<50;periods+=5) {
            for ( int bullishThreshold=30;bullishThreshold<=80;bullishThreshold+=5) {
                for (int bearishThreshold=-80;bearishThreshold<=-30;bearishThreshold+=5) {
                    double[] aroonCompoundSignal=AROON.aroonCompoundSignal(reverse, periods, bullishThreshold, bearishThreshold)
                    def perf=Performance.gainSignal(aroonCompoundSignal,reverse,false)
                    double capital=100;
                    capital = compoundCapital(perf, capital)
                    if(capital>bestCapital){
                        bestCapital=capital
                    log.debug("capital: {} - periods: {} - bullishThreshold: {} - bearishThreshold:{}",capital,
                            periods,bullishThreshold,bearishThreshold)
                    }
                }
            }
        }


    }

    @Test
    public void sooPrformanceTest(){
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
        def periods = 14
        def finalSmoothingPeriods = 3
        def overBoughtThreshold = 70
        def overSoldThreshold = 30
        double[][] oscillator = SOO.stochasticOscillator(0, prices, periods, finalSmoothingPeriods)
        short[] overBOverS = SOO.overBOverS(oscillator[3], overBoughtThreshold, overSoldThreshold,1)
        double[] overBOverSContinous=SOO.overBOverSContinous(overBOverS)
        def perf=Performance.gainSignal(overBOverSContinous,reverse,false)
        ArrayHelper.log(perf,log,false)

        double capital=100;
        capital = compoundCapital(perf, capital)
        log.debug("center: {}",capital)


    }

    @Test
    public void rocPrformanceTest(){
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)

        def roc1Period = 13
        def roc2Period = 21
        def roc3Period = 150
        def roc4Period = 200
        def roc5Period = 250
        def rocCompositeSmoothPeriod = 10
        def rocCompositeThreshold = 15

        Double[] perf = ROC.compositeSignal(prices, roc1Period, roc2Period, roc3Period, roc4Period, roc5Period,
                rocCompositeSmoothPeriod, rocCompositeThreshold, reverse)
        ArrayHelper.log(perf,log,false)

        double capital=100;
        capital = compoundCapital(perf, capital)
        log.debug("capital: {}",capital)


    }

    @Test
    public void rocPrformanceBruteForceTest(){
        /* winners */
        /* capital: 150081.66948643822 - r1:10 - r2:15 - r3:90 - r4: 245 - r5: 290 - rcsp:5 - rct:15
        capital: 165189.43007903354 - r1:10 - r2:20 - r3:90 - r4: 240 - r5: 290 - rcsp:5 - rct:15
        capital: 185218.99725047845 - r1:10 - r2:20 - r3:90 - r4: 245 - r5: 290 - rcsp:5 - rct:15
        */
        def ticker = "AAPL"
        def bestCapital=0;
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
/*
        def roc1Period = 13
        def roc2Period = 21
        def roc3Period = 150
        def roc4Period = 200
        def roc5Period = 250
        def rocCompositeSmoothPeriod = 10
        def rocCompositeThreshold = 15*/

        for (int roc1Period=5; roc1Period<15;roc1Period+=5) {
            for (int roc2Period=15; roc2Period<50;roc2Period+=5) {
                for (int roc3Period=50; roc3Period<200;roc3Period+=10) {
                    for (int roc4Period=200; roc4Period<250;roc4Period+=5) {
                        for (int roc5Period=250; roc5Period<350;roc5Period+=10) {
                            for (int rocCompositeSmoothPeriod=5; rocCompositeSmoothPeriod<30;
                                 rocCompositeSmoothPeriod+=5) {
                                for (int rocCompositeThreshold=5; rocCompositeThreshold<30;
                                     rocCompositeThreshold+=5) {
                                    double[] rocCompositeSignal = ROC.compositeSignal(prices, roc1Period, roc2Period, roc3Period, roc4Period,
                                            roc5Period,
                                            rocCompositeSmoothPeriod, rocCompositeThreshold)
                                    def perf = Performance.gainSignal(rocCompositeSignal, reverse, false)
                                    double capital=100;
                                    capital = compoundCapital(perf, capital)
                                    if(capital>bestCapital){
                                        bestCapital=capital
                                        log.debug("capital: {} - r1:{} - r2:{} - r3:{} - r4: {} - r5: {} - rcsp:{} - " +
                                            "rct:{} ",capital,roc1Period,roc2Period,roc3Period,roc4Period,roc5Period,
                                            rocCompositeSmoothPeriod,rocCompositeThreshold)
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }


    }




    def double compoundCapital(Double[] perf, double capital) {
        for (int i = 0; i < perf.length; i++) {
            capital = capital + (capital * perf[i])
        }
        capital
    }

    def double[] getPrices(String ticker) {
        def ss = SecurityService.instance
        def s = ss.getSecurity(ticker)
        def prices = new double[s.getHistory().size()];
        s.getHistory().eachWithIndex { obj, i -> prices[i] = obj.adjClose }
        return prices
    }


    //
}
