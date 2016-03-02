package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ArrayUtil;

/**
 * The Aroon indicators fluctuate above/below a centerline (50) and are bound between 0 and 100.
 * These three levels are important for interpretation. At its most basic, the bulls have the edge when Aroon-Up is
 * above 50 and Aroon-Down is below 50. This indicates a greater propensity for new x-day highs than lows. The
 * converse is true for a downtrend. The bears have the edge when Aroon-Up is below 50 and Aroon-Down is above 50.
 * groovy-playground
 * Aroon signal(my own variation): 1 if the oscillator is above the bullishThreshold and is greater than 0 since 1 day and -1 if the
 * oscillator is below the bearishThreshold and is lower than 0 since 1 day. <b>A signal is given if the crossing of a
 * threshold is confirming a trend of the oscillator, either positive or negative</b>
 * Created by filippo on 19/10/14.
 */
public class AROON {
    private static Logger log= LoggerFactory.getLogger(AROON.class);

    public static final byte AROON_BULL=1;
    public static final byte AROON_BEAR=-1;
    public static final byte AROON_NEUTRAL=0;



    /**
     * Aroon-Up = ((25 - Days Since 25-day High)/25) x 100
     * @return
     */
    public static double[] aroonUp(double[] prices,int periods){
        //int[]elapsedTrendUp=MathAnalysis.getElapsedTrend(highs, periods, 1);
        int[]elapsedTrendUp=MathAnalysis.getElapsedExtrema(prices, periods, 1);
        double[] aroonUp=aroonFormula(periods, elapsedTrendUp);
        return aroonUp;
    }


    /**
     * Aroon-Down = ((25 - Days Since 25-day Low)/25) x 100
     * @return
     */
    public static double[] aroonDown(double[] prices,int periods){
        //int[]elapsedTrendDown=MathAnalysis.getElapsedTrend(lows, periods, -1);
        int[]elapsedTrendDown=MathAnalysis.getElapsedExtrema(prices, periods, -1);
        double[] aroonDown=aroonFormula(periods, elapsedTrendDown);
        return aroonDown;
    }

    public static double[] aroonOscillator(double[] prices,int periods){
        double[] aroonUp=aroonUp(prices,periods);
        double[] aroonDown=aroonDown(prices, periods);
        double[] aroonOscillator= new double[prices.length];
        for (int i = 0; i < aroonUp.length; i++) {
            aroonOscillator[i]=aroonUp[i]-aroonDown[i];
        }
        return aroonOscillator;
    }
    /**
     * @param   prices curve of  price
     * @param   periods how many days (backwards) to consider in computing indicators
     * @param   bullishThreshold above this value of the oscillator we consider a bullish phase
     * @param   bearThreshold below this value of the oscillator we consider a bearish phase
     * @return 1 if the oscillator is above the bullishThreshold and is greater than 0 since 1 day and -1 if the
     * oscillator is below the bearThreshold and is lower than 0 since 1 day. A signal is given if the crossing of a
     * threshold is confirming a trend of the oscillator, either positive or negative
     **/
    public static short[] aroonSignal(double[] prices,int periods,int bullishThreshold,int bearThreshold){
        double[] aroonOscillator=aroonOscillator(prices,periods);
        short[] aroonSignal=new short[aroonOscillator.length];

        for (int i = 0; i < aroonOscillator.length; i++) {
            if(i>1&&aroonOscillator[i]>=bullishThreshold && aroonOscillator[i-1]>0){
                aroonSignal[i]=AROON_BULL;
            }
            else if(i>1&&aroonOscillator[i]<=bearThreshold && aroonOscillator[i-1]<0)
                aroonSignal[i]=AROON_BEAR;
            else
                aroonSignal[i]=AROON_NEUTRAL;
        }

        return aroonSignal;
    }

    /**
     * @param   prices curve of  price
     * @param   periods how many days (backwards) to consider in computing indicators
     * @param   bullishThreshold above this value of the oscillator we consider a bullish phase
     * @param   bearThreshold below this value of the oscillator we consider a bearish phase
     * @return 1 Similar to #aroonSignal. In case of neutral signal it keep the last value
     **/
    public static double[] aroonCompoundSignal(double[] prices,int periods,int bullishThreshold,int bearThreshold){
        short[] aroonSignal=AROON.aroonSignal(prices, periods,bullishThreshold, bearThreshold);
        double[] aroonCompound=new double[aroonSignal.length];


        for (int i = 1; i < aroonSignal.length; i++) {
            if(aroonSignal[i]==AROON_NEUTRAL)
                aroonCompound[i]=aroonCompound[i-1];
            else
                aroonCompound[i]=aroonSignal[i];
        }

        return aroonCompound;
    }

    /**
     * Helpers
     */
    private static double[] aroonFormula(int periods, int[] elapsedTrend) {
        double[] aroon=new double[elapsedTrend.length];
        for (int i = 0; i < elapsedTrend.length; i++) {
            aroon[i]=Math.max(0,((double)(periods-elapsedTrend[i])/periods))*100;

        }
        return aroon;
    }

}
