package stats

import com.fasterxml.jackson.databind.ObjectMapper
import data.TestDataSupport
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool
import groovyx.gpars.dataflow.Promise
import groovyx.gpars.pa.CallAsyncTask
import helpers.ArrayHelper
import org.codehaus.groovy.reflection.ReflectionUtils
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

import java.lang.reflect.Type

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
    public void kamaPrformanceBruteForceTest(){

        /*winners*/
        /*
        capital: 91005.79664486904 - eRP: 10 - f5: 8 - f2: 2 - slow: 25 - regression20: 30  - regression5: 10 - threshold: 0.01 - hypertrendSlopeThreshold: 0.2
        capital: 93507.91950147462 - eRP: 10 - f5: 8 - f2: 2 - slow: 35 - regression20: 30  - regression5: 10 - threshold: 0.01 - hypertrendSlopeThreshold: 0.2
        capital: 99268.81623576248 - eRP: 10 - f5: 8 - f2: 2 - slow: 40 - regression20: 30  - regression5: 10 - threshold: 0.01 - hypertrendSlopeThreshold: 0.2
        capital: 105985.19363184845 - eRP: 15 - f5: 10 - f2: 2 - slow: 30 - regression20: 20  - regression5: 4 - threshold: 0.01 - hypertrendSlopeThreshold: 0.4
         */

        def bestCapital=0;

        def ticker = "JPM"
        double[] prices = getPrices(ticker)
        /*
        def eRP=30;//Efficiency Ratio periods
        def f5=5;// slowest fast constant 5 periods
        def f2=2;// fastest constant 2 periods
        def slow=30;// slowest constant 30 periods
        def regression20=20;// regression periods 20
        def regression5=5;// regression periods 5
        def threshold=0.03;//neutral trend boundaries
        def hypertrendSlopeThreshold=0.2*/
        final def reverse = ArrayUtil.reverse(prices)
        for (int eRP=10;eRP<=40;eRP+=5) {
            for (int f5=6;f5<12;f5+=2) {
                for (int f2=2;f2<6;f2+=1) {
                    for (int slow=20;slow<=50;slow+=5) {
                        for (int regression20=15;regression20<=30;regression20+=5) {
                            for (int regression5=4;regression5<=10;regression5+=2) {
                                for (int thresholdInt=1;thresholdInt<7;thresholdInt++) {
                                    for (int hypertrendSlopeThresholdInt=1;hypertrendSlopeThresholdInt<5;
                                         hypertrendSlopeThresholdInt++) {
                                        float threshold=thresholdInt/100F;
                                        float hypertrendSlopeThreshold=hypertrendSlopeThresholdInt/10F;
                                        def ikama5Trend = KAMA.trend(reverse,eRP,f5,slow,regression20,threshold)
                                        def ikama2Trend = KAMA.trend(reverse,eRP,f2,slow,regression5,threshold)
                                        def kama2 = KAMA.kama(reverse,eRP,f2,slow);
                                        def ikama2Slope = MathAnalysis.slope(kama2,regression5)
                                        def hypertrend = KAMA.hypertrend(ikama2Slope,hypertrendSlopeThreshold)
                                        double[] ikamaConvergenceHyper=MathAnalysis.convergence(ikama5Trend,ikama2Trend,hypertrend)
                                        def perf=Performance.gainSignal(ikamaConvergenceHyper,reverse,false)

                                        double capital=100;
                                        capital = compoundCapital(perf, capital)
                                        if(capital>bestCapital) {
                                            bestCapital = capital
                                            log.debug("capital: {} - eRP: {} - f5: {} - f2: {} - slow: {} - regression20: {} " +
                                                    " - regression5: {} - threshold: {} - hypertrendSlopeThreshold: {}", capital,
                                                    eRP, f5, f2, slow, regression20, regression5,threshold,hypertrendSlopeThreshold)

                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }



    @Test
    public void macdPrformanceTest(){
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
        double[][] out=MA.macd(0,reverse.size(),reverse,12,36,9);
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
    public void macdPrformanceBruteForceTest(){
        /* winners  */
        /*AAPL
        capital: 69890.45346084643 - fastEma: 4 - slowEma: 24 - finalEma: 3
        capital: 72124.62372843911 - fastEma: 8 - slowEma: 32 - finalEma: 3
        capital: 148981.350995845 - fastEma: 16 - slowEma: 24 - finalEma: 3
        */
        /* JPM
        capital: 1411.881270953797 - fastEma: 12 - slowEma: 32 - finalEma: 3
        capital: 1435.1472130644747 - fastEma: 12 - slowEma: 40 - finalEma: 3
        capital: 1988.4268992016525 - fastEma: 20 - slowEma: 28 - finalEma: 3
         */
        def bestCapital=0;
        def ticker = "JPM"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)

        /*def fastEma = 12
        def slowEma = 36
        def finalEma = 9*/
        for (int fastEma=4;fastEma<=20;fastEma+=4) {
            for (int slowEma=24;slowEma<44;slowEma+=4) {
                for (int finalEma=3;finalEma<15;finalEma+=3) {
                    double[][] out=MA.macd(0,reverse.size(),reverse, fastEma, slowEma, finalEma);
                    def centerLineCrossSignal=out[5];
                    def perf=Performance.gainSignal(centerLineCrossSignal,reverse,false)
                    double capital=100;
                    capital = compoundCapital(perf, capital)
                    if(capital>bestCapital) {
                        bestCapital = capital
                        log.debug("capital: {} - fastEma: {} - slowEma: {} - finalEma: {}", capital, fastEma, slowEma, finalEma)

                    }
                }
            }
        }

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
        /* winners AAPL*/
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
        /* winners*/
        /*  AAPL
        capital: 150081.66948643822 - r1:10 - r2:15 - r3:90 - r4: 245 - r5: 290 - rcsp:5 - rct:15
        capital: 165189.43007903354 - r1:10 - r2:20 - r3:90 - r4: 240 - r5: 290 - rcsp:5 - rct:15
        capital: 185218.99725047845 - r1:10 - r2:20 - r3:90 - r4: 245 - r5: 290 - rcsp:5 - rct:15
        */
        /* JPM
        capital: 2846.4443736867315 - r1:10 - r2:30 - r3:170 - r4: 210 - r5: 340 - rcsp:5 - rct:5
        capital: 2938.144145344695 - r1:10 - r2:35 - r3:180 - r4: 200 - r5: 340 - rcsp:5 - rct:5
        capital: 3057.4275791037994 - r1:10 - r2:40 - r3:170 - r4: 210 - r5: 340 - rcsp:5 - rct:5
         */
        def ticker = "JPM"
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
        GParsPool.withPool(4) {
            for (int roc1Period = 5; roc1Period < 15; roc1Period += 5) {
                for (int roc2Period = 15; roc2Period < 50; roc2Period += 5) {
                    for (int roc3Period = 50; roc3Period < 200; roc3Period += 10) {
                        for (int roc4Period = 200; roc4Period < 250; roc4Period += 5) {
                            for (int roc5Period = 250; roc5Period < 350; roc5Period += 10) {
                                for (int rocCompositeSmoothPeriod = 5; rocCompositeSmoothPeriod < 30;
                                     rocCompositeSmoothPeriod += 5) {
                                    for (int rocCompositeThreshold = 5; rocCompositeThreshold < 30;
                                         rocCompositeThreshold += 5) {


                                        CallAsyncTask task={ roc1P,roc2P,roc3P,roc4P,roc5P,
                                                             rocCompositeSmoothP,rocCompositeT->
                                                    double[] rocCompositeSignal = ROC.compositeSignal(prices, roc1P,
                                                            roc2P, roc3P, roc4P,
                                                            roc5P,
                                                            rocCompositeSmoothP, rocCompositeT)
                                                    def perf = Performance.gainSignal(rocCompositeSignal, reverse, false)
                                                    double capital = 100;
                                                    capital = compoundCapital(perf, capital)
                                                    if (capital > bestCapital) {
                                                        bestCapital = capital
                                                        log.debug("capital: {} - r1:{} - r2:{} - r3:{} - r4: {} - r5: {} - " +
                                                                "rcsp:{} - " +
                                                                "rct:{} ", capital, roc1P, roc2P, roc3P, roc4P, roc5P,
                                                                rocCompositeSmoothP, rocCompositeT)
                                                    }
                                                }.callAsync(roc1Period,roc2Period,roc3Period,roc4Period,roc5Period,
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

    @Test
    public void parallel(){

            GParsPool.withPool(4) {
                for (int i=0;i<10;i++) {
                    CallAsyncTask task={
                        def sleepingTime = 100 * Random.newInstance().nextInt(7)
                        Thread.currentThread().sleep(sleepingTime)
                        println(" Thread ${Thread.currentThread().id} with sleeping time ${sleepingTime}")
                    }.callAsync();
                    println('-------------')
                }

            }


    }

    @Test
    public void legacyTest(){
        InputStream is=ReflectionUtils.getCallingClass(0).getResourceAsStream("/AAPL.json")
        ObjectMapper mapper = new ObjectMapper();
        List object = (ArrayList)mapper.readValue(is, new ArrayList<Object>().class);
        for (int i = 0; i < object.size(); i++) {
            log.debug("i: {}",object.get(i));
        }
        String ticker = "AAPL"
        double[] prices = getPrices(ticker)
        //ArrayHelper.log(prices,log,true)
        //println("jsonObect ${object.class}")


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
