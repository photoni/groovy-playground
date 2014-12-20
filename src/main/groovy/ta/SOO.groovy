package ta

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j;
import helpers.ArrayHelper;

/**
 * Stochastic Oscillator Overlay
 * groovy-playground
 * Created by filippo on 02/12/14.
 */
@Slf4j
@CompileStatic
public class SOO {

    public static double[] stochasticOscillator(int startIndex, double[] revHigh,double[] revLow, double[] revClose,int
            periods){

        def highestHighs= ArrayHelper.closureIterator(0, revHigh, 14){int start,double [] values,int prds ->
            return MathAnalysis.highestHigh(start,values,prds)
        }


        def lowestLows=ArrayHelper.closureIterator(0,revLow,14){int start,double [] values,int prds ->
            return MathAnalysis.lowestLow(start,values,prds)
        }
        def stochasticOscillator=ArrayHelper.closureIterator(0,revClose.length,14){int start,int prds ->
            return stochasticOscillatorFormula(highestHighs[start],lowestLows[start],revClose[start])
        }


    }

    public static double stochasticOscillatorFormula(double highestHigh,double lowestLow, double close){

        double result=(highestHigh-lowestLow)!=0?(close-lowestLow)/(highestHigh-lowestLow)*100:0


    }
}
