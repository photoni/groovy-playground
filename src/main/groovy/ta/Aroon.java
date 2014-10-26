package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * groovy-playground
 * Created by filippo on 19/10/14.
 */
public class Aroon {
    private static Logger log= LoggerFactory.getLogger(Aroon.class);

    /**
     * combined Aroon indicators
     * @return AroonUp,AroonDown and AroonOscillator in this order
     */
    public static double[][] aroon(){
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
        int[]elapsedTrendDown=MathAnalysis.getElapsedTrend(lows, periods, 1);
        double[] aroonDown=aroonFormula(periods, elapsedTrendDown);
        return aroonDown;
    }

    public static double[] aroonOscillator(){
        return null;
    }

    /**
     * Helpers
     */
    private static double[] aroonFormula(int periods, int[] elapsedTrend) {
        double[] aroon=new double[elapsedTrend.length];
        for (int i = 0; i < elapsedTrend.length; i++) {
            aroon[i]=((double)(periods-elapsedTrend[i])/periods)*100;

        }
        return aroon;
    }

}
