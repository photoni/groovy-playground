package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Aroon indicators fluctuate above/below a centerline (50) and are bound between 0 and 100.
 * These three levels are important for interpretation. At its most basic, the bulls have the edge when Aroon-Up is
 * above 50 and Aroon-Down is below 50. This indicates a greater propensity for new x-day highs than lows. The
 * converse is true for a downtrend. The bears have the edge when Aroon-Up is below 50 and Aroon-Down is above 50.
 * groovy-playground
 * Created by filippo on 19/10/14.
 */
public class Aroon {
    private static Logger log= LoggerFactory.getLogger(Aroon.class);

    /**
     * combined Aroon indicators
     * @return AroonUp,AroonDown and AroonOscillator in this order
     */
    public static double[] aroonSignal(){
        return null;
    }

    /**
     * Aroon-Up = ((25 - Days Since 25-day High)/25) x 100
     * @return
     */
    public static double[] aroonUp(double[] highs,int periods){
        int[]elapsedTrendUp=MathAnalysis.getElapsedTrend(highs, periods, 1);
        double[] aroonUp=aroonFormula(periods, elapsedTrendUp);
        return aroonUp;
    }


    /**
     * Aroon-Down = ((25 - Days Since 25-day Low)/25) x 100
     * @return
     */
    public static double[] aroonDown(double[] lows,int periods){
        int[]elapsedTrendDown=MathAnalysis.getElapsedTrend(lows, periods, -1);
        double[] aroonDown=aroonFormula(periods, elapsedTrendDown);
        return aroonDown;
    }

    public static double[] aroonOscillator(double[] highs,double[] lows,int periods){
        double[] aroonUp=aroonUp(highs,periods);
        double[] aroonDown=aroonDown(lows, periods);
        double[] aroonOscillator= new double[highs.length];
        for (int i = 0; i < aroonUp.length; i++) {
            aroonOscillator[i]=aroonUp[i]-aroonDown[i];
        }
        return aroonOscillator;
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
