package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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
