package stats

import com.fasterxml.jackson.databind.ObjectMapper
import data.CSV
import groovy.io.FileType
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool
import groovyx.gpars.pa.CallAsyncTask
import helpers.ArrayHelper
import org.apache.commons.math3.random.EmpiricalDistribution
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.codehaus.groovy.reflection.ReflectionUtils
import org.junit.Test
import service.SecurityService
import ta.AROON
import ta.KAMA
import ta.MA
import ta.MathAnalysis
import ta.ROC
import ta.SOO
import util.ArrayUtil
import util.CSVUtil
import util.MathUtil

@Slf4j
class PerformanceTest {
    @Test
    public void kamaPrformanceTest() {

        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        def eRP = 30;//Efficiency Ratio periods
        def f5 = 5;// slowest constant 5 periods
        def f2 = 2;// fastest constant 2 periods
        def slow = 30;// slowest constant 30 periods
        def regression20 = 20;// regression periods 20
        def regression5 = 5;// regression periods 5
        def threshold = 0.03;//neutral trend boundaries
        def hypertrendSlopeThreshold = 0.2
        final def reverse = ArrayUtil.reverse(prices)
        def ikama5Trend = KAMA.trend(reverse, eRP, f5, slow, regression20, threshold)

        def ikama2Trend = KAMA.trend(reverse, eRP, f2, slow, regression5, threshold)
        def kama2 = KAMA.kama(reverse, eRP, f2, slow);
        def ikama2Slope = MathAnalysis.slope(kama2, regression5)
        def hypertrend = KAMA.hypertrend(ikama2Slope, hypertrendSlopeThreshold)

        double[] ikamaConvergenceHyper = MathAnalysis.convergence(ikama5Trend, ikama2Trend, hypertrend)
        double[] ikamaConvergence = MathAnalysis.convergence(ikama5Trend, ikama2Trend)



        def perfHyper = Performance.gainSignal(ikamaConvergenceHyper, reverse, false)
        def perf = Performance.gainSignal(ikamaConvergence, reverse, false)
        ArrayHelper.log(perf, log, false)
        log.debug("----------")
        ArrayHelper.log(perfHyper, log, false)
        double capital = 100;
        for (int i = 0; i < perfHyper.length; i++) {
            capital = capital + (capital * perfHyper[i])
        }
        log.debug("hyperCapital: {}", capital)
        capital = 100;
        capital = compoundCapital(perf, capital)
        log.debug("capital: {}", capital)

    }

    @Test
    public void kamaPrformanceBruteForceTest() {

        /*winners*/
        /*
        capital: 91005.79664486904 - eRP: 10 - f5: 8 - f2: 2 - slow: 25 - regression20: 30  - regression5: 10 -
        threshold: 0.01 - hypertrendSlopeThreshold: 0.2
        capital: 93507.91950147462 - eRP: 10 - f5: 8 - f2: 2 - slow: 35 - regression20: 30  - regression5: 10 -
        threshold: 0.01 - hypertrendSlopeThreshold: 0.2
        capital: 99268.81623576248 - eRP: 10 - f5: 8 - f2: 2 - slow: 40 - regression20: 30  - regression5: 10 -
        threshold: 0.01 - hypertrendSlopeThreshold: 0.2
        capital: 105985.19363184845 - eRP: 15 - f5: 10 - f2: 2 - slow: 30 - regression20: 20  - regression5: 4 -
        threshold: 0.01 - hypertrendSlopeThreshold: 0.4
         */

        def bestCapital = 0;

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
        for (int eRP = 10; eRP <= 40; eRP += 5) {
            for (int f5 = 6; f5 < 12; f5 += 2) {
                for (int f2 = 2; f2 < 6; f2 += 1) {
                    for (int slow = 20; slow <= 50; slow += 5) {
                        for (int regression20 = 15; regression20 <= 30; regression20 += 5) {
                            for (int regression5 = 4; regression5 <= 10; regression5 += 2) {
                                for (int thresholdInt = 1; thresholdInt < 7; thresholdInt++) {
                                    for (int hypertrendSlopeThresholdInt = 1; hypertrendSlopeThresholdInt < 5;
                                         hypertrendSlopeThresholdInt++) {
                                        float threshold = thresholdInt / 100F;
                                        float hypertrendSlopeThreshold = hypertrendSlopeThresholdInt / 10F;
                                        def ikama5Trend = KAMA.trend(reverse, eRP, f5, slow, regression20, threshold)
                                        def ikama2Trend = KAMA.trend(reverse, eRP, f2, slow, regression5, threshold)
                                        def kama2 = KAMA.kama(reverse, eRP, f2, slow);
                                        def ikama2Slope = MathAnalysis.slope(kama2, regression5)
                                        def hypertrend = KAMA.hypertrend(ikama2Slope, hypertrendSlopeThreshold)
                                        double[] ikamaConvergenceHyper = MathAnalysis.convergence(ikama5Trend,
                                                ikama2Trend, hypertrend)
                                        def perf = Performance.gainSignal(ikamaConvergenceHyper, reverse, false)

                                        double capital = 100;
                                        capital = compoundCapital(perf, capital)
                                        if (capital > bestCapital) {
                                            bestCapital = capital
                                            log.debug("capital: {} - eRP: {} - f5: {} - f2: {} - slow: {} - " +
                                                    "regression20: {} " +
                                                    " - regression5: {} - threshold: {} - hypertrendSlopeThreshold: " +
                                                    "{}", capital,
                                                    eRP, f5, f2, slow, regression20, regression5, threshold,
                                                    hypertrendSlopeThreshold)

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
    public void macdPrformanceTest() {
        //def ticker = "AAPL"
        //double[] prices = getPrices(ticker)
        //final def reverse = ArrayUtil.reverse(prices)

        String ticker = "AAPL"
        def ss = SecurityService.instance
        double[] pricesT = ss.getLegacyPrices(ticker)


        pricesT = ArrayUtil.reverse(pricesT)





        double[][] out = MA.macd(0, pricesT.size(), pricesT, 16, 24, 3);
        def centerLineCrossSignal = out[5];
        def compoundSignal = out[7];
        def perfcenter = Performance.gainSignal(centerLineCrossSignal, pricesT, false)
        ArrayHelper.log(perfcenter, log, false)
        log.debug("----------")
        def perfCompound = Performance.gainSignal(compoundSignal, pricesT, false)
        ArrayHelper.log(perfCompound, log, false)
        double capital = 100;
        capital = compoundCapital(perfcenter, capital)
        log.debug("center: {}", capital)

        capital = 100;
        capital = compoundCapital(perfCompound, capital)
        log.debug("compound: {}", capital)
    }


    @Test
    public void macdPrformanceBruteForceTest() {
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
        def bestCapital = 0;
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)

        /*def fastEma = 12
        def slowEma = 36
        def finalEma = 9*/
        for (int fastEma = 4; fastEma <= 20; fastEma += 4) {
            for (int slowEma = 24; slowEma < 44; slowEma += 4) {
                for (int finalEma = 3; finalEma < 15; finalEma += 3) {
                    double[][] out = MA.macd(0, reverse.size(), reverse, fastEma, slowEma, finalEma);
                    def centerLineCrossSignal = out[5];
                    def perf = Performance.gainSignal(centerLineCrossSignal, reverse, false)
                    double capital = 100;
                    capital = compoundCapital(perf, capital)
                    if (capital > bestCapital) {
                        bestCapital = capital
                        log.debug("capital: {} - fastEma: {} - slowEma: {} - finalEma: {}", capital, fastEma,
                                slowEma, finalEma)

                    }
                }
            }
        }

    }

    @Test
    public void aroonPrformanceTest() {
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)

        def periods = 25
        def bullishThreshold = 40
        def bearishThreshold = -40
        double[] aroonCompoundSignal = AROON.aroonCompoundSignal(reverse, periods, bullishThreshold, bearishThreshold)
        def perf = Performance.gainSignal(aroonCompoundSignal, reverse, false)
        ArrayHelper.log(perf, log, false)

        double capital = 100;
        capital = compoundCapital(perf, capital)
        log.debug("center: {}", capital)


    }

    @Test
    public void aroonPrformanceBruteForceTest() {
        /* winners AAPL*/
        /*
        capital: 110436.18258912767 - periods: 30 - bullishThreshold: 50 - bearishThreshold:-75
        capital: 127696.14304468011 - periods: 35 - bullishThreshold: 40 - bearishThreshold:-65
        */

        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
        def bestCapital = 0;
        /*
        def periods = 25
        def bullishThreshold=40
        def bearishThreshold=-40*/

        for (int periods = 10; periods < 50; periods += 5) {
            for (int bullishThreshold = 30; bullishThreshold <= 80; bullishThreshold += 5) {
                for (int bearishThreshold = -80; bearishThreshold <= -30; bearishThreshold += 5) {
                    double[] aroonCompoundSignal = AROON.aroonCompoundSignal(reverse, periods, bullishThreshold,
                            bearishThreshold)
                    def perf = Performance.gainSignal(aroonCompoundSignal, reverse, false)
                    double capital = 100;
                    capital = compoundCapital(perf, capital)
                    if (capital > bestCapital) {
                        bestCapital = capital
                        log.debug("capital: {} - periods: {} - bullishThreshold: {} - bearishThreshold:{}", capital,
                                periods, bullishThreshold, bearishThreshold)
                    }
                }
            }
        }


    }

    @Test
    public void sooPrformanceTest() {
        def ticker = "AAPL"
        double[] prices = getPrices(ticker)
        final def reverse = ArrayUtil.reverse(prices)
        def periods = 14
        def finalSmoothingPeriods = 3
        def overBoughtThreshold = 70
        def overSoldThreshold = 30
        double[][] oscillator = SOO.stochasticOscillator(0, prices, periods, finalSmoothingPeriods)
        short[] overBOverS = SOO.overBOverS(oscillator[3], overBoughtThreshold, overSoldThreshold, 1)
        double[] overBOverSContinous = SOO.overBOverSContinous(overBOverS)
        def perf = Performance.gainSignal(overBOverSContinous, reverse, false)
        ArrayHelper.log(perf, log, false)

        double capital = 100;
        capital = compoundCapital(perf, capital)
        log.debug("center: {}", capital)


    }

    @Test
    public void sdTest() {
        def ss = SecurityService.instance
        double[] prices = ss.getLegacyPrices('AAPL')
        double[] reverse = ArrayUtil.reverse(prices)
        double[] gains= new double[reverse.size()-1]
        for (int i = 1; i < reverse.length; i++) {
            gains[i-1]=MathAnalysis.gain(reverse[i-1],reverse[i])*100
        }
        EmpiricalDistribution ed= new EmpiricalDistribution();
        ed.load(gains);
        StatisticalSummary stats=ed.getSampleStats();
        log.debug("sd: {}, mean: {}", stats.getStandardDeviation(),stats.getMean())


    }


    @Test
    public void rocPrformanceTest() {
        //-----AAPL
        //5-25-30-120-200-240-5-25
        //r1=5, r2=20, r3=50, r4=70, r5=230, r6=240, rcsp=5, rct=25
        //10-25-50-120-230-260-5-15

        //IBM
        //10-20-40-110-220-260-10-5 - 394.05134252115283

        //GOOGL
        //10-20-40-120-140-260-10-10 - 431.64044971704124
        //r1=10---r4=20--r6=240-10-5 - the most
        def roc1Period = 10
        def roc4Period = 20
        def roc6Period = 240
        def rocCompositeSmoothPeriod = 10
        def rocCompositeThreshold = 5
        def params=['roc1Period':10,'roc4Period':20,'roc6Period':240,'rocCompositeSmoothPeriod':10,
                    'rocCompositeThreshold':5,]
        ArrayList list = doListHistories()
        EmpiricalDistribution ed= new EmpiricalDistribution();
        double[] capitals=new double[list.size()];
        for (int i = 0; i < list.size() ; i++) {
            String file = list.get(i);
            String tick = file.split("\\.")[0]

            double capital=rocCapital(tick,params)
            log.debug("computing symbol :{} - capital:{} ", tick,capital)
            capitals[i]=capital;
        }
        ed.load(capitals);
        StatisticalSummary ss=ed.getSampleStats();
        def min=ss.getMin();
        def max=ss.getMax();
        def mean=ss.getMean()
        log.debug("min:{} - max:{} - mean:{}", min,max,mean)

    }

    def rocCapital(String ticker,Map params) {
        def roc1Period = params['roc1Period']
        def roc4Period = params['roc4Period']
        def roc6Period = params['roc6Period']
        def rocCompositeSmoothPeriod = params['rocCompositeSmoothPeriod']
        def rocCompositeThreshold = params['rocCompositeThreshold']
        Double[] perf = compute(ticker, rocCompositeSmoothPeriod, rocCompositeThreshold, roc1Period,
                roc4Period, roc6Period)
        double capital = 100;
        capital = compoundCapital(perf, capital)
        //ArrayHelper.log(perf,log,false)
        //log.debug("capital: {}", capital)
        capital
    }

    @Test
    public void rocPrformanceMSCITest() {
        def ticker = "AAPL"
        def roc1Period = 120
        def roc2Period = 240

        def rocCompositeSmoothPeriod = 5
        def rocCompositeThreshold = 1
        Double[] perf = compute(ticker, rocCompositeSmoothPeriod, rocCompositeThreshold, roc1Period, roc2Period)
        double capital = 100;
        capital = compoundCapital(perf, capital)
        ArrayHelper.log(perf,log,false)
        log.debug("capital: {}", capital)
    }



    @Test
    public void rocPrformanceBruteForceTest() {
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
        def masterTicker = "AAPL"
        def bestCapital = 0;
        //double[] prices = getPrices(ticker)
        //final def reverse = ArrayUtil.reverse(prices)
        def ss = SecurityService.instance
        double[] prices = ss.getLegacyPrices(masterTicker)


        double[] reverse = ArrayUtil.reverse(prices)
        def distribution= [[],[],[],[],[],[]]

/*
        def roc1Period = 13
        def roc2Period = 21
        def roc3Period = 150
        def roc4Period = 200
        def roc5Period = 250
        def rocCompositeSmoothPeriod = 10
        def rocCompositeThreshold = 15*/
        GParsPool.withPool(4) {
            for (int roc1Period = 5; roc1Period < 20; roc1Period += 5) {
                        for (int roc4Period = 20; roc4Period < 130; roc4Period += 10) {
                                for (int roc6Period = 130; roc6Period < 300; roc6Period += 20) {
                                    //for (int roc7Period = 250; roc7Period < 350; roc7Period += 20) {
                                        for (int rocCompositeSmoothPeriod = 5; rocCompositeSmoothPeriod < 30;
                                             rocCompositeSmoothPeriod += 5) {
                                            for (int rocCompositeThreshold = 5; rocCompositeThreshold < 30;
                                                 rocCompositeThreshold += 5) {


                                                CallAsyncTask task = { roc1P, roc4P, roc6P,
                                                                       rocCompositeSmoothP, rocCompositeT ->
                                                    /*double[] rocCompositeSignal = ROC.compositeSignal(prices,
    roc1P,
                                                            roc2P, roc3P, roc4P,
                                                            roc5P,
                                                            rocCompositeSmoothP, rocCompositeT)*/
                                                    double[] rocCompositeSignal = ROC.compositeSignal(rocCompositeSmoothP, rocCompositeT, prices, roc1P,roc4P, roc6P);
                                                    def perf = Performance.gainSignal(ArrayUtil.reverse
                                                            (rocCompositeSignal), reverse, false)
                                                    double capital = 100;
                                                    capital = compoundCapital(perf, capital)
                                                    if (capital > bestCapital) {
                                                        bestCapital = capital
                                                        log.debug("capital: {} - r1:{} - r4: {} " +
                                                                "- r6: {}" +
                                                                " " +
                                                                "- " +
                                                                "rcsp:{} - " +
                                                                "rct:{} ", capital, roc1P, roc4P,roc6P,rocCompositeSmoothP, rocCompositeT)
                                                    }
                                                    //def perfLevel=[0,10000,10000,20000,20000,30000,30000,40000,
//                                                  40000,50000,50000]
                                                    def perfLevel=[0,100,100,200,600,800,800,900,900,1000,1000]
                                                    switch ( capital ) {

                                                        case { perfLevel[0]<= it && it < perfLevel[1] }:
                                                            distribution[0].add(["r1":roc1P,"r4":roc4P,"r6":roc6P,"rcsp":rocCompositeSmoothP,"rct":rocCompositeT])
                                                            break
                                                        case { perfLevel[2]<= it && it < perfLevel[3] }:
                                                            distribution[1].add(["r1":roc1P,"r4":roc4P,"r6":roc6P,"rcsp":rocCompositeSmoothP,"rct":rocCompositeT])
                                                            break
                                                        case { perfLevel[4]<= it && it < perfLevel[5] }:
                                                            distribution[2].add(["r1":roc1P,"r4":roc4P,"r6":roc6P,"rcsp":rocCompositeSmoothP,"rct":rocCompositeT])
                                                            break
                                                        case { perfLevel[6]<= it && it < perfLevel[7] }:
                                                            distribution[3].add(["r1":roc1P,"r4":roc4P,"r6":roc6P,"rcsp":rocCompositeSmoothP,"rct":rocCompositeT])
                                                            break
                                                        case { perfLevel[8]<= it && it < perfLevel[9] }:
                                                            distribution[4].add(["r1":roc1P,"r4":roc4P,"r6":roc6P,"rcsp":rocCompositeSmoothP,"rct":rocCompositeT])
                                                            break
                                                        case { perfLevel[10]<= it }:
                                                            distribution[5].add(["r1":roc1P,"r4":roc4P,"r6":roc6P,"rcsp":rocCompositeSmoothP,"rct":rocCompositeT])
                                                            break
                                                        default:
                                                            break

                                                    }



                                                }.callAsync(roc1Period, roc4Period, roc6Period, rocCompositeSmoothPeriod,
                                                        rocCompositeThreshold)


                                            }
                                        }
                                    //}
                                }

                        }


            }
        }
        log.debug("distribution3 size : {} ",distribution[3].size())
        log.debug("distribution4 size : {} ",distribution[4].size())
        log.debug("distribution5 size : {} ",distribution[5].size())
        def tickers=[];
        CSV csv= new CSV()
        String[] header = ['tick', 'r1', 'r2', 'r3', 'r4', 'r5', 'r6', 'rcsp', 'rct', 'capital', 'signalsNum']
        csv.setHeader(header)
        ArrayList list = doListHistories()

        for (int i = 0; i < list.size() ; i++) {
            String file=list.get(i);
            String tick=file.split("\\.")[0]
            log.debug("computing symbol :{} ",tick)
            if(i%2==0)
                log.debug("computing symbol block:{} ",i)
            for (int j = 0; j < distribution[5].size(); j++) {

                def dist = distribution[5][j]
                if(dist==null)
                    continue
                String key=extractKey(dist)
                /*log.debug("computing 5: {} - {} - {} - {} - {} - {} - {} - {} - {}", tick,dist["r1"],dist["r2"],
                        dist["r3"],dist["r4"],dist["r5"],dist["r6"],dist["rcsp"],dist["rct"])*/
                def perf=compute(tick,dist["rcsp"],dist["rct"],dist["r1"],dist["r4"],dist["r6"])
                double capital = 100;
                capital = compoundCapital(perf, capital)
                //log.debug("capital: {}", capital)
                //map.get(key).add(capital)
                dist.putAt("tick",tick)
                dist.putAt("capital",capital)
                dist.putAt("signalsNum",perf.size())
                String[] line=extractLine(csv.getHeader(),dist)
                csv.addLine(line)
            }
            for (int j = 0; j < distribution[4].size(); j++) {
                def dist = distribution[4][j]
                if(dist==null)
                    continue
                String key=extractKey(dist)
                /*log.debug("computing 4: {} - {} - {} - {} - {} - {} - {} - {} - {}", tick,dist["r1"],dist["r2"],
                        dist["r3"],dist["r4"],dist["r5"],dist["r6"],dist["rcsp"],dist["rct"])*/
                def perf=compute(tick,dist["rcsp"],dist["rct"],dist["r1"],dist["r4"],dist["r6"])
                double capital = 100;
                capital = compoundCapital(perf, capital)
                //log.debug("capital: {}", capital)
                dist.putAt("tick",tick)
                dist.putAt("capital",capital)
                dist.putAt("signalsNum",perf.size())
                String[] line=extractLine(csv.getHeader(),dist)
                csv.addLine(line)
            }
            for (int j = 0; j < distribution[3].size(); j++) {
                def dist = distribution[3][j]
                if(dist==null)
                    continue
                String key=extractKey(dist)
                /*log.debug("computing 3: {} - {} - {} - {} - {} - {} - {} - {} - {}", tick,dist["r1"],dist["r2"],
                        dist["r3"],dist["r4"],dist["r5"],dist["r6"],dist["rcsp"],dist["rct"])*/
                def perf=compute(tick,dist["rcsp"],dist["rct"],dist["r1"],dist["r4"],dist["r6"])
                double capital = 100;
                capital = compoundCapital(perf, capital)
                //log.debug("capital: {}", capital)
                dist.putAt("tick",tick)
                dist.putAt("capital",capital)
                dist.putAt("signalsNum",perf.size())
                String[] line=extractLine(csv.getHeader(),dist)
                csv.addLine(line)
            }
            //tickers.add(tick)
        }

        CSVUtil.write("/var/data/pig/beuteForce${masterTicker}.csv",csv.getCsv());





    }
    @Test
    public void readResults(){
        List<String[]> allLines=CSVUtil.entriesFromURI("/var/data/pig/beuteForceAAPL.csv")
        CSV csv= new CSV(allLines)
        csv.getHeader().each {
            log.debug(it)
        }

        log.debug(Arrays.toString(csv.getHeader()))
        log.debug(Arrays.toString(csv.getLines().get(0)))
        log.debug(Arrays.toString(csv.getLines().get(1)))

    }



    @Test
    public void listHistories(){

        ArrayList list = doListHistories()





    }


    @Test
    public void parallel() {

        GParsPool.withPool(4) {
            for (int i = 0; i < 10; i++) {
                CallAsyncTask task = {
                    def sleepingTime = 100 * Random.newInstance().nextInt(7)
                    Thread.currentThread().sleep(sleepingTime)
                    println(" Thread ${Thread.currentThread().id} with sleeping time ${sleepingTime}")
                }.callAsync();
                println('-------------')
            }

        }


    }

    @Test
    public void legacyTest() {
        ArrayList list = doListHistories()
        double[] capitals=new double[list.size()]
        EmpiricalDistribution ed= new EmpiricalDistribution();
        for (int i = 0; i < list.size() ; i++) {
            String file=list.get(i);
            String tick=file.split("\\.")[0]
            double capital=doLegacy(tick)
            capitals[i]=capital
            log.debug("ticker: {} - capital: {}", tick,capital)

        }

        ed.load(capitals);
        StatisticalSummary ss=ed.getSampleStats();
        def min=ss.getMin();
        def max=ss.getMax();
        def mean=ss.getMean()
        log.debug("min:{} - max:{} - mean:{}", min,max,mean)
    }

    @Test
    public void legacySingleTest() {

            def capital=doLegacy("MSFT")
            log.debug("capital:  {}",capital)

    }


    def double doLegacy(String tick) {
        InputStream is = ReflectionUtils.getCallingClass(0).getResourceAsStream("/histories/${tick}.json")
        ObjectMapper mapper = new ObjectMapper();
        List list = (ArrayList) mapper.readValue(is, new ArrayList<Object>().class);
        double[] pricesT = new double[list.size()]
        double[] rateT = new double[list.size()]
        double[] signalT = new double[list.size()]
        for (int i = 0; i < list.size(); i++) {
            pricesT[i] = list.get(i)['v']
            rateT[i] = list.get(i)['r']
            if (rateT[i] > 0)
                signalT[i] = 1
            else
                signalT[i] = -1
            //log.debug("i: {}",list.get(i));
        }
        pricesT = ArrayUtil.reverse(pricesT)
        rateT = ArrayUtil.reverse(rateT)
        signalT = ArrayUtil.reverse(signalT)
/*
        for (int i = 0; i < pricesT.length; i++) {

            log.debug("i: {} - v: {} - r:{} - s: {}", i, pricesT[i], rateT[i], signalT[i]);
        }*/

        def perf = Performance.gainSignal(signalT, pricesT, false)

        //ArrayHelper.log(perf,log,false)
        double capital = 100;
        capital = compoundCapital(perf, capital)
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

    def ArrayList doListHistories() {
        def list = []

        //def dir = new File("/home/filippo/projects/pig/technical_analysis/src/test/resources/histories/")
        def dir = new File("/home/filippo/projects/pig/groovy-playground/src/test/resources/histories/")

        dir.eachFileRecurse(FileType.FILES) { file ->
            list << file.getName()
        }
        list
    }

    def String extractKey(dist) {
        def key = dist["r1"] + "-" + dist["r4"] + "-" + dist["r6"]+ "-" + dist["rcsp"] + "-" + dist["rct"]
        key
    }

    def String[] extractLine(String[] header,Map dist) {
        def result=new LinkedList()
        for (String key:header){
            result.add(dist[key])
        }
        result
    }

    def Double[] compute(String ticker, int rocCompositeSmoothPeriod, int rocCompositeThreshold, int roc1Period, int roc4Period,int roc6Period) {
        def ss = SecurityService.instance
        double[] prices = ss.getLegacyPrices(ticker)
        double[] reverse = ArrayUtil.reverse(prices)
        double[] rocCompositeSignal = ROC.compositeSignal(rocCompositeSmoothPeriod, rocCompositeThreshold, prices,
                roc1Period, roc4Period,roc6Period)
        def perf = Performance.gainSignal(ArrayUtil.reverse(rocCompositeSignal), reverse, false)
        perf
    }

    def Double[] compute(String ticker, int rocCompositeSmoothPeriod, int rocCompositeThreshold, int roc1Period, int
            roc2Period) {
        def ss = SecurityService.instance
        double[] prices = ss.getLegacyPrices(ticker)
        double[] reverse = ArrayUtil.reverse(prices)
        double[] rocCompositeSignal = ROC.compositeSignal(rocCompositeSmoothPeriod, rocCompositeThreshold, prices,
                roc1Period,
                roc2Period)
        def perf = Performance.gainSignal(ArrayUtil.reverse(rocCompositeSignal), reverse, false)
        perf
    }


    //
}
