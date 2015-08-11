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

    /**
     *
     * @param startIndex
     * @param revHighs highs in descending time order
     * @param revLows lows in descending time order
     * @param revClose close in ascending time order
     * @param periods
     * @param smooth
     * @return stochastic oscillator oin ascending time order
     */
    public static double[][] stochasticOscillator(int startIndex, double[] revHighs,double[] revLows,double[]
            close,int
            periods, int smooth){
        double[][] result=new double[3][close.length]
        double[] highestHighs= ArrayHelper.closureIterator(startIndex, revHighs, periods){int start,double [] values,int prds ->
            return MathAnalysis.highestHigh(start,values,prds)
        }


        double[] lowestLows=ArrayHelper.closureIterator(startIndex,revLows,periods){int start,double [] values,int prds ->
            return MathAnalysis.lowestLow(start,values,prds)
        }
        result[1]=highestHighs
        result[2]=lowestLows
        /* ascending order like prices */
        highestHighs=ArrayUtil.reverse(highestHighs)
        lowestLows=ArrayUtil.reverse(lowestLows)


        double[] stochasticOscillator=ArrayHelper.closureIterator(startIndex,close.length,periods){int start,int prds ->

            def soo=stochasticOscillatorFormula(highestHighs[start],lowestLows[start],close[start])
            log.debug("index: {} - highestHigh : {} - lowestLow: {} - close: {} -soo: {}",start,highestHighs[start],
                    lowestLows[start],close[start],soo)
            return soo
        }
        result[0]=stochasticOscillator

        return result
        /*
        double[] stochasticOscillatorSubArray=ArrayUtils.subarray(ArrayUtil.reverse(stochasticOscillator),periods-1,
                stochasticOscillator.length)
        def stochasticOscillatorFinal=MA.sma(0,stochasticOscillatorSubArray.length,stochasticOscillatorSubArray,smooth)
        */

    }
    public static short[] overBOverS(double[] stocasticOscillator,int overBThreshold,int overSThreshold){
        short[] overBOverS= ArrayHelper.closureIterator( stocasticOscillator){int i,double value ->
            short resultVal=0;
            if(value>overBThreshold)
                resultVal=1
            else if(value<=overSThreshold)
                resultVal=-1
            return resultVal
        }


    }
    public static short[] overBOverSContinous(short[] overBOverS){
        short[] continousSignal=new short[overBOverS.length]
        short prev=0;
        for (int i = 1; i < overBOverS.length; i++) {
            if(overBOverS[i]==0){
                continousSignal[i]=prev
            }
            else{
                continousSignal[i]=overBOverS[i]
            }
            prev=continousSignal[i];

        }
        return continousSignal

    }

    public static double stochasticOscillatorFormula(double highestHigh,double lowestLow, double close){


        double result=(highestHigh-lowestLow)!=0?((close-lowestLow)/(highestHigh-lowestLow))*100:0


    }
}
