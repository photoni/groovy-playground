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
     * @param startIndex
     * @param endIndex
     * @param values
     * @param periods
     * @return
     */
    public static double[] wSmoothed1Iterator(int startIndex, int endIndex, double[] values,int periods) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-1; i++) {
            result[i] = wSmoothed1(startIndex+i, values, periods, (short) 0);
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

    /*
	 * Wilder's smoothing First Technique
	 *
	 * @param startIndex
	 * @param endIndex
	 * @param values
	 * @param N
	 * @param cursor
     * @return
	 */
    public static double wSmoothed1(int startIndex,double[] values, int N, short cursor) {
        int currentIndex=startIndex+cursor;
        int endIndex=currentIndex+N;
        double wsSmoothedPrev = 0;

        // Current smoothed = [Prior smoothed - (Prior smoothed)/N + Current]
        double result = 0D;
        if (currentIndex+N < endIndex-1 && cursor<N) {
            wsSmoothedPrev = wSmoothed1(startIndex+1, values, N, ++cursor);
            result = wsSmoothedPrev - (wsSmoothedPrev/N) + values[currentIndex];
        } else if (cursor==N || currentIndex+N == endIndex-1) {
            double sum = 0;
            for (int j = 0; j < N; j++) {
                sum += values[currentIndex + j];
            }

            result = sum;

        } else {
            result = 0;
        }

        return result;

    }


    /*
	 * Wilder's smoothing Second Technique
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param N
	 * @return
	 */
    public static double wSmoothed2(int startIndex, int endIndex, double[] values,int N,short cursor,boolean rev) {
        int currentIndex=startIndex+cursor;
        double smoothedDmPrev = 0;

        // Current smoothedDM = [(Prior smoothedDM x 13) + Current DM] / 14
        double result = 0D;
        if (currentIndex+N < endIndex-1 && cursor<N) {
            smoothedDmPrev = wSmoothed2(startIndex + 1, endIndex, values, N, ++cursor, rev);
            result = ((smoothedDmPrev*(N-1)) + values[currentIndex])/N;
        } else if (cursor==N || currentIndex+N == endIndex-1) {
            double sum = 0;
            for (int j = 0; j < N; j++) {
                sum += dm(values[currentIndex + j],values[currentIndex + j+1],rev);
            }

            result = sum / N;

        } else {
            result = 0;
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
