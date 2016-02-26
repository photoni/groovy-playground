package stats

import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import org.vertx.java.core.json.impl.Json
import service.SecurityService
import ta.KAMA
import ta.MA
import ta.MathAnalysis
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

    //
}
