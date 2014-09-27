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
     * ADX iterator
     * @param startIndex
     * @param endIndex
     * @param highs
     * @param lows
     * @param periods
     * @return
     */
    /**public static double[] adx(int startIndex, int endIndex,double[] values ,double[] highs,
                               double[] lows, int periods) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = dxFormula(startIndex+i, endIndex, values, highs, lows, periods);
            log.debug("dx {}: {}",i,result[i]);
        }

        result=Indicators.sma(startIndex, endIndex, result, periods);

        return result;
    }**/

    /**
     * DX
     * @param startIndex
     * @param endIndex
     * @param values
     * @param highs
     * @param lows
     * @param periods
     * @return
     */
    /*public static double dxFormula(int startIndex, int endIndex, double[] values,double[] highs,double[] lows, int periods){
        double dxPlus=diFormula(startIndex, endIndex, values, highs, lows, periods, false);
        double dxMinus=diFormula(startIndex, endIndex, values, highs, lows, periods, true);
        double denominator = dxPlus+dxMinus;
        log.debug("dx {} den: {} - dxP: {} - dxM: {}",startIndex,denominator);
        double dx=denominator!=0?(Math.abs(dxPlus-dxMinus)/denominator):0;
        return dx;
    }*/


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
