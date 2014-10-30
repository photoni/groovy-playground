package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains methods concerning Mathematical Analysis(extrema, highs , lows,differentials)
 * groovy-playground
 * Created by filippo on 25/10/14.
 */
public class MathAnalysis {

    private static Logger log= LoggerFactory.getLogger(MathAnalysis.class);

    /**
     *
     * @param values
     * @return An array of 0,1,-1 where 1 and -1 corresponds respectively to local max and local min
     */
    public static int[] localMinMax(double[] values){
        int[] result= new int[values.length];
        for (int i = 1; i < values.length-1; i++) {
            result[i]=((values[i-1]<values[i]) && (values[i]>values[i+1]))?1:(((values[i-1]>values[i]) && (values[i]<values[i+1]))?-1:0);
        }
        return result;
    }
    /**
     *
     * @param highs
     * @param lows
     * @param maxElapsed the backward window
     * @return Two arrays containing respectively xElapsedMax and xElapsedMin within the given back elapsed
     */
    public static int[][] xElapsedMinMax(double[] highs,double[] lows, int maxElapsed){
        int[][] result= new int[2][highs.length];

        int signum=1;
        int[] elapsedMin = getElapsedTrend(lows, maxElapsed, signum);

        signum=-1;
        int[] elapsedMax = getElapsedTrend(highs, maxElapsed, signum);

        result[0]=elapsedMax;
        result[1]=elapsedMin;
        return result;
    }


    /**
     *
     * @param values
     * @param maxElapsed
     * @param signum 1 for uptrends and -1 for downtrends
     * @return An array containing the number of position the current trend is developing since
     */
    public static int[] getElapsedTrend(double[] values, int maxElapsed, int signum) {
        int[] elapsed= new int[values.length];
        //double extremeValue = signum == 1 ? Double.MIN_VALUE : Double.MAX_VALUE;
        double current= values[0];
        int xDay=0;
        for (int i = 0; i < values.length; i++) {
            if((values[i]-current)*signum<=0 && xDay<=maxElapsed){//keep trend
                elapsed[i]=xDay;

            }else if((values[i]-current)*signum>0){//reversal
                xDay=0;
                elapsed[i]=xDay;

            }else{//stop elapsed. Resetting all
                xDay=0;
                elapsed[i]=xDay;

            }
            current=values[i];
            xDay++;
        }
        return elapsed;
    }

    /**
     * Deltas. Combined Gain and Losses. Losses are expressed in Abs values
     * @param values
     * @return
     */
    public static double[][] deltas(double[] values){
        double[][] deltas=new double[2][values.length];
        double[] gains=new double[values.length];
        double[] losses=new double[values.length];
        for (int i = 0; i <(values.length-1) ; i++) {
            double delta=values[i] -values[i+1];
            if(delta>0)
                gains[i]=delta;
            else
                losses[i]=Math.abs(delta);
        }
        deltas[0]=gains;
        deltas[1]=losses;
        return deltas;
    }

}
