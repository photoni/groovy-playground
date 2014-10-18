package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interpretation:<br/>
 * At its most basic the Average Directional Index (ADX) can be used to determine if a security is trending or not.
 * This determination helps traders choose between a trend following system or a non-trend following system.
 * Wilder suggests that a strong trend is present when ADX is above 25 and no trend is present when below 20.
 * There appears to be a gray zone between 20 and 25. As noted above, chartists may need to adjust the settings to increase sensitivity and signals.
 * ADX also has a fair amount of lag because of all the smoothing techniques. Many technical analysts use 20 as the key level for ADX.
 *
 * groovy-playground
 * Created by filippo on 21/09/14.
 */

public class ADX {
    private static Logger log= LoggerFactory.getLogger(Indicators.class);

    /**
     * DM iterator, combined. Price descending by date
     * @param startIndex
     * @param endIndex
     * @param highs
     * @param lows
     * @return
     */
    public static double[][] dm(int startIndex, int endIndex, double[] highs,double[] lows) {
        int length = endIndex - startIndex;
        double[][] result = new double[2][length];
        double[] dmMinus=dm(0, lows.length, lows,true);
        double[] dmPlus=dm(0, highs.length, highs,false);
        for(int i=0;i<length;i++){
            double dmPlusCombined=0;
            double dmMinusCombined=0;
            if(dmPlus[i]>dmMinus[i])
                dmPlusCombined=dmPlus[i];
            else
                dmMinusCombined=dmMinus[i];
            dmPlus[i]=dmPlusCombined;
            dmMinus[i]=dmMinusCombined;
        }
        result[0]=dmPlus;
        result[1]=dmMinus;
        return result;
    }

    /**
     * DX . Price descending by date
     * @param startIndex
     * @param endIndex
     * @param highs
     * @param lows
     * @return
     */
    public static double[] dx(int startIndex, int endIndex, double[] highs,double[] lows,int periods) {

        int length = endIndex - startIndex;
        double[][] dmCombined=ADX.dm(0, endIndex, highs, lows);
        double[] dmPlus=dmCombined[0];
        double[] dmPlusSmoothed14=Smooth.wSmoothed1Iterator(0,dmPlus.length,dmPlus,periods);

        double[] dmMinus=dmCombined[1];
        double[] dmMinusSmoothed14=Smooth.wSmoothed1Iterator(0,dmMinus.length,dmMinus,periods);

        double[] trM1=Indicators.trM1(0, endIndex, highs, lows);
        double[] trM1Smoothed14=Smooth.wSmoothed1Iterator(0,trM1.length,trM1,periods);

        double[] diPlus=ADX.di(dmPlusSmoothed14,trM1Smoothed14);
        double[] diMinus=ADX.di(dmMinusSmoothed14,trM1Smoothed14);

        double[] result = new double[length-periods];
        for(int i =0;i<length-periods;i++){

            result[i]=Math.abs((diPlus[i]-diMinus[i])/(diPlus[i]+diMinus[i]))*100;
        }
        return result;

    }

    /**
     * ADX . Price descending by date
     * @param startIndex
     * @param endIndex
     * @param highs
     * @param lows
     * @return
     */
    public static double[] adx(int startIndex, int endIndex, double[] highs,double[] lows,int periods) {

        double[] dx = dx(startIndex,endIndex,highs,lows,periods);
        double[] adx = Smooth.wSmoothedIterator(startIndex,dx.length,dx,periods,2);

        return adx;


    }

    /**
     * DI . Price descending by date
     * @param values
     * @param trM1Smoothed
     * @return
     */
    public static double[] di(double[] values,double[] trM1Smoothed) {

        double[] result = new double[values.length];

        for(int i =0;i<values.length;i++){
            if(trM1Smoothed[i]>0)
                result[i]=(values[i]/trM1Smoothed[i])*100;

        }
        return result;

    }




    /**
     * DM iterator. Price descending by date
     * @param startIndex
     * @param endIndex
     * @param values
     * @param reverse reverse true if calculating dmMinus
     * @return
     */
    public static double[] dm(int startIndex, int endIndex, double[] values,boolean reverse) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-1; i++) {
            result[i] = dm(values[i],values[i+1],reverse);
        }

        return result;
    }






    /**
     * Directional movement. CurrentValue-PreviousValue or vice versa
     * @param current
     * @param previous
     * @param rev tell's the direction of the subtraction
     * @return
     */
    private static double dm(double current, double previous,boolean rev) {
        double diff = rev?previous - current:current - previous;
        return diff>0?diff:0;
    }
}
