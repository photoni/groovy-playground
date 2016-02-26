package ta;

import util.ArrayUtil;

/**
 * Created by filippo on 11/20/15.
 */
public class KAMA {

    /**
     * @param values
     * @param periods look back period
     * @param fastest fastest moving constant(suggested 2)
     * @param slowest slowest moving constant(suggested 30)
     * @return
     */
    public static double[] kama(double[] values, int periods, int fastest, int slowest) {

        double[] sc = ER.sc(values, periods, fastest, slowest);
        double[] kama = MA.ema(0, values.length, values, sc, periods);
        return kama;
    }

    /**
     * Define a trend indicator on KAMA based on the most favourable simple regression(i.e better slope). When the
     * slope of the regrassion cross the threshold boundaries a change in the trend happens
     *
     * @param values
     * @param erPeriods         look back period for efficiency ratio
     * @param fastest           fastest moving constant(suggested 2)
     * @param slowest           slowest moving constant(suggested 30)
     * @param threshold         neutral trend boundaries in absolute value. The value is suggested to be within
     *                          boundaries [0 - 0.1].
     * @param regressionPeriods the look back period for the simple regression
     * @return 1 for a bullish trend and -1 for a bearish trend
     */
    public static double[] trend(double[] values, int erPeriods, int fastest, int slowest, int regressionPeriods, float
            threshold) {

        double[] kama = kama(values, erPeriods, fastest, slowest);
        double[] slope = MathAnalysis.slope(kama, regressionPeriods);
        double[] trend = MathAnalysis.trend(slope, threshold);
        return trend;
    }

    /**
     * Detect exceptional movements
     *
     * @param slope the slope on which detect crashes
     * @param threshold         neutral trend boundaries in absolute value. The value is suggested to be within
     *                          boundaries [0.1 - 0.7].

     * @return 1 if an hyper-trend context is detected or 0
     */
    public static double[] hypertrend(double[] slope, float threshold) {

        double[] hypertrend = new double[slope.length];
        for (int i = 0; i < slope.length; i++) {
            hypertrend[i]=(Math.abs(slope[i])>Math.abs(threshold))?1D:0D;
        }

        return hypertrend;
    }
}
