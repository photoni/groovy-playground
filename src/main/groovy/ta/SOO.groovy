package ta

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j;
import helpers.ArrayHelper;

/**
 * Stochastic Oscillator. It follows the speed or the momentum of price. As a rule, the momentum changes direction before price.
 * As such, bullish and bearish divergences in the Stochastic Oscillator can be used to foreshadow reversals.
 * Can be used to identify bull and bear set-ups to anticipate a future reversal. Because the
 * Stochastic Oscillator is range bound, is also useful for identifying overbought and oversold levels.
 * The Stochastic Oscillator is above 50 when the close is in the upper half of the range and below 50 when the close is in the lower half.
 * Low readings (below 20) indicate that price is near its low for the given time period.
 * High readings (above 80) indicate that price is near its high for the given time period
 * groovy-playground
 * Created by filippo on 02/12/14.
 */
@Slf4j
@CompileStatic
public class SOO {

    public static double[] stochasticOscillator(int startIndex, double[] revClose,int
            periods){
        def highestHighs= ArrayHelper.closureIterator(0, revClose, periods){int start,double [] values,int prds ->
            return MathAnalysis.highestHigh(start,values,prds)
        }


        def lowestLows=ArrayHelper.closureIterator(0,revClose,periods){int start,double [] values,int prds ->
            return MathAnalysis.lowestLow(start,values,prds)
        }
        def stochasticOscillator=ArrayHelper.closureIterator(0,revClose.length,periods){int start,int prds ->
            return stochasticOscillatorFormula(highestHighs[start],lowestLows[start],revClose[start])
        }


    }

    public static double stochasticOscillatorFormula(double highestHigh,double lowestLow, double close){

        double result=(highestHigh-lowestLow)!=0?(close-lowestLow)/(highestHigh-lowestLow)*100:0


    }
}
