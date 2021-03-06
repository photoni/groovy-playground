package ta;

import helpers.ArrayHelper;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains methods concerning Mathematical Analysis(extrema, highs , lows,differentials) groovy-playground Created by
 * filippo on 25/10/14.
 */
public class MathAnalysis {

    private static Logger log = LoggerFactory.getLogger(MathAnalysis.class);

    /**
     * @param values
     * @return An array of 0,1,-1 where 1 and -1 corresponds respectively to local max and local min
     */
    public static int[] localMinMax(double[] values) {
        int[] result = new int[values.length];
        for (int i = 1; i < values.length - 1; i++) {
            result[i] = ((values[i - 1] < values[i]) && (values[i] > values[i + 1])) ? 1 : (((values[i - 1] > values[i]) && (values[i] < values[i + 1])) ? -1 : 0);
        }
        return result;
    }

    /**
     * @param highs
     * @param lows
     * @param maxElapsed the backward window
     * @return Two arrays containing respectively xElapsedMax and xElapsedMin within the given back elapsed
     */
    public static int[][] xElapsedMinMax(double[] highs, double[] lows, int maxElapsed) {
        int[][] result = new int[2][highs.length];

        int signum = 1;
        int[] elapsedMin = getElapsedTrend(lows, maxElapsed, signum);

        signum = -1;
        int[] elapsedMax = getElapsedTrend(highs, maxElapsed, signum);

        result[0] = elapsedMax;
        result[1] = elapsedMin;
        return result;
    }


    /**
     * @param values
     * @param maxElapsed
     * @param signum     1 for uptrends and -1 for downtrends
     * @return An array containing the number of position the current trend is developing since
     */
    public static int[] getElapsedTrend(double[] values, int maxElapsed, int signum) {
        int[] elapsed = new int[values.length];
        //double extremeValue = signum == 1 ? Double.MIN_VALUE : Double.MAX_VALUE;
        double current = values[0];
        int xDay = 0;
        for (int i = 0; i < values.length; i++) {
            if ((values[i] - current) * signum < 0 && xDay <= maxElapsed) {//keep trend
                elapsed[i] = xDay;

            } else if ((values[i] - current) * signum > 0) {//reversal
                xDay = 0;
                elapsed[i] = xDay;

            } else if (xDay > maxElapsed) {//stop elapsed. Resetting all
                elapsed[i] = xDay;

            } else {//keep elapsed.
                //xDay=0;
                elapsed[i] = xDay;

            }
            current = values[i];
            xDay++;
        }
        return elapsed;
    }

    /**
     * @param values
     * @param maxElapsed
     * @param signum     1 for uptrends and -1 for downtrends
     * @return An array containing the number of position from the extrema point computed in the period
     */
    public static int[] getElapsedExtrema(double[] values, int maxElapsed, int signum) {
        int[] elapsed = new int[values.length];
        double extremeValueInitial = signum == 1 ? Double.MIN_VALUE : Double.MAX_VALUE;

        double current = values[0];
        int xDay = 0;
        for (int i = 0; i < values.length; i++) {
            if (i >= maxElapsed) {
                double extrema = extremeValueInitial;
                int extremaIndex = i;

                for (int j = 0; j <= maxElapsed; j++) {
                    int index = i - j;
                    if ((values[index] - extrema) * signum > 0) {
                        extrema = values[index];
                        extremaIndex = index;
                    }
                }
                elapsed[i] = i - extremaIndex;
            }
        }
        return elapsed;
    }

    /**
     * Deltas. Combined Gain and Losses. Losses are expressed in Abs values
     *
     * @param values
     * @return two array of length n-1 containing gain and losses
     */
    public static double[][] deltas(double[] values) {
        double[][] deltas = new double[2][values.length - 1];
        double[] gains = new double[values.length - 1];
        double[] losses = new double[values.length - 1];
        for (int i = 0; i < (values.length - 1); i++) {
            double delta = values[i] - values[i + 1];
            if (delta > 0)
                gains[i] = delta;
            else
                losses[i] = Math.abs(delta);
        }
        deltas[0] = gains;
        deltas[1] = losses;
        return deltas;
    }

    /**
     * Daily rate of changes
     *
     * @param values
     * @return array of length n-1 containing gains
     */
    public static double[] rocs(double[] values,int period) {

        int lenght = values.length - period;
        if(lenght<=5) return null;
        double[] gains = new double[lenght];
        for (int i = period; i < (gains.length - 1); i++) {
                gains[i-period] = gain(values[i-period],values[i]);
        }
        return gains;
    }


    public static double[] slope(double[] values, int periods) {
        double[] slope = new double[values.length];


        for (int i = periods - 1; i < values.length; i++) {
            SimpleRegression sr = new SimpleRegression();
            log.debug("-----> i: {}", i);
            for (int j = periods - 1; j >= 0; j--) {
                log.debug("adding--> {} to {}", values[i - j], i - j);
                sr.addData(i - j, values[i - j]);
            }
            slope[i] = sr.getSlope();
            log.debug("slope --> {}", slope[i]);
        }
        return slope;
    }

    public static double[] trend(double[] slopes, float threshold) {
        double[] trend = new double[slopes.length];
        for (int i = 1; i < slopes.length; i++) {
            if (slopes[i] > 0 && slopes[i] > threshold)
                trend[i] = 1;
            else if (slopes[i] < 0 && Math.abs(slopes[i]) > threshold)
                trend[i] = -1;
            else
                trend[i] = trend[i - 1];
        }

        return trend;
    }

    public static double[] convergence(double[] slowest, double[] fastest) {
        double[] convergence = new double[slowest.length];
        for (int i = 1; i < slowest.length; i++) {
            if (slowest[i] == fastest[i])
                convergence[i] = slowest[i];
            else
                convergence[i] = convergence[i - 1];
        }

        return convergence;
    }

    public static double[] convergence(double[] slowest, double[] fastest,double[] hypertrend) {
        double[] convergence = new double[slowest.length];
        for (int i = 1; i < slowest.length; i++) {
            if (slowest[i] == fastest[i])
                convergence[i] = slowest[i];
            else if(hypertrend[i]==1)
                convergence[i] = fastest[i - 1];
            else
                convergence[i] = convergence[i - 1];
        }

        return convergence;
    }



    /**
     * @param startIndex the index to start from(included)
     * @param highs      the array of values. It must be in descending order
     * @param periods    how many position to span the computation
     * @return the highest high
     */
    public static double highestHigh(int startIndex, double[] highs, int periods) {
        int endIndex = startIndex + periods;
        assert startIndex >= 0 && endIndex <= highs.length;
        double highest = Double.NEGATIVE_INFINITY;
        for (int i = startIndex; i < endIndex; i++) {
            if (highs[i] > highest)
                highest = highs[i];
        }
        return highest;
    }


    /**
     * @param startIndex the index to start from(included)
     * @param lows       the array of values. It must be in descending order
     * @param periods    how many position to span the computation
     * @return the lowest low
     */
    public static double lowestLow(int startIndex, double[] lows, int periods) {
        int endIndex = startIndex + periods;
        assert startIndex >= 0 && endIndex <= lows.length;
        double lowest = Double.POSITIVE_INFINITY;
        for (int i = startIndex; i < endIndex; i++) {
            if (lows[i] < lowest)
                lowest = lows[i];
        }
        return lowest;
    }


    public static double gain(double from, double to) {

        return (to - from) / from;
    }

}
