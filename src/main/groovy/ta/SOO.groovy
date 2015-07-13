package ta

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j;
import helpers.ArrayHelper
import org.apache.commons.lang.ArrayUtils
import util.ArrayUtil;

/**
 * Stochastic Oscillator. It follows the speed or the momentum of price. As a rule, the momentum changes direction before price.
 * As such, bullish and bearish divergences in the Stochastic Oscillator can be used to foreshadow reversals.
 * Can be used to identify bull and bear set-ups to anticipate a future reversal. Because the
 * Stochastic Oscillator is range bound, is also useful for identifying overbought and oversold levels.
 * The Stochastic Oscillator is above 50 when the close is in the upper half of the range and below 50 when the close is in the lower half.
 * Low readings (below 20) indicate that price is near its low for the given time period. High readings (above 80) indicate that price is near its high for the given time period
 * groovy-playground
 * Created by filippo on 02/12/14.
 */
@Slf4j
@CompileStatic
public class SOO {

    public static double[] stochasticOscillator(int startIndex, double[] revHighs,double[] revLows,double[]
            revClose,int
            periods){
        def highestHighs= ArrayHelper.closureIterator(startIndex, revHighs, periods){int start,double [] values,int prds ->
            return MathAnalysis.highestHigh(start,values,prds)
        }


        def lowestLows=ArrayHelper.closureIterator(startIndex,revLows,periods){int start,double [] values,int prds ->
            return MathAnalysis.lowestLow(start,values,prds)
        }
        double[] stochasticOscillator=ArrayHelper.closureIterator(startIndex,revClose.length,periods){int start,int prds ->

            def soo=stochasticOscillatorFormula(highestHighs[start],lowestLows[start],revClose[start])
            log.debug("index: {} - highestHigh : {} - lowestLow: {} - close: {} -soo: {}",start,highestHighs[start],
                    lowestLows[start],revClose[start],soo)
            return soo
        }
        double[] stochasticOscillatorSubArray=ArrayUtils.subarray(ArrayUtil.reverse(stochasticOscillator),periods-1,
                stochasticOscillator.length)
        def stochasticOscillatorFinal=MA.sma(0,stochasticOscillatorSubArray.length,stochasticOscillatorSubArray,3)

    }

    public static double stochasticOscillatorFormula(double highestHigh,double lowestLow, double close){


        double result=(highestHigh-lowestLow)!=0?((close-lowestLow)/(highestHigh-lowestLow))*100:0


    }
}
