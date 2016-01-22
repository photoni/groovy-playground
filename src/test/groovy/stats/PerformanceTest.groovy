package stats

import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import service.SecurityService
import ta.KAMA
import ta.MathAnalysis
import util.ArrayUtil

@Slf4j
class PerformanceTest {
    @Test
    public void kamaPrformanceTest(){
        def ss= SecurityService.instance
        def s=ss.getSecurity("AAPL")
        def prices=new double[s.getHistory().size()];
        s.getHistory().eachWithIndex { obj, i -> prices[i]=obj.adjClose}
        def eRP=30;//Efficiency Ratio periods
        def f5=5;// slowest constant 5 periods
        def f2=2;// fastest constant 2 periods
        def slow=30;// slowest constant 30 periods
        def regression20=20;// regression periods 20
        def regression5=5;// regression periods 5
        def threshold=0.03;//neutral trend boundaries
        final def reverse = ArrayUtil.reverse(prices)
        def ikama5Trend = KAMA.trend(reverse,eRP,f5,slow,regression20,threshold)

        def ikama2Trend = KAMA.trend(reverse,eRP,f2,slow,regression5,threshold)
        def kama2 = KAMA.kama(prices,eRP,f2,slow);
        def ikama2Slope = MathAnalysis.slope(kama2,regression5)
        def hypertrend = KAMA.hypertrend(ikama2Slope,0.5)

        double[] ikamaConvergence=MathAnalysis.convergence(ikama5Trend,ikama2Trend,hypertrend)

        def perf=Performance.gainSignal(ikamaConvergence,reverse)
        ArrayHelper.log(perf,log,false)

    }
}
