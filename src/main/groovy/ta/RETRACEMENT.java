package ta;

import com.sun.jersey.server.wadl.generators.resourcedoc.xhtml.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ArrayUtil;

import java.text.DecimalFormat;

/**
 * created by filippo on 9/2/15.
 */
public class RETRACEMENT {
    private static Logger log = LoggerFactory.getLogger(RETRACEMENT.class);
    private static final DecimalFormat df = new DecimalFormat("#.000");
    private double level38 = 0.382;
    private double level50 = 0.50;
    private double level61 = 0.618;

    public RETRACEMENT(double level38, double level50, double level61) {
        this.level38 = level38;
        this.level50 = level50;
        this.level61 = level61;
    }

    public RETRACEMENT() {
    }


    /**
     * @param values
     * @param minimumReatracementRate price movements under this rate are ignored
     * @return the fibonacci levels
     */
    public double[] fibonacci(double[] values, int minimumReatracementRate) {

        double[] result = new double[values.length];
        double[] zigzag = ZIGZAG.zigZag(values, minimumReatracementRate);
        int trendStartIndex = 0;
        double trendStartVal = zigzag[0];
        double impulse = 0;
        for (int i = 2; i < zigzag.length; i++) {
            double val_2 = zigzag[i - 2];
            double val_1 = zigzag[i - 1];
            double val = zigzag[i];
            log.debug("val: {} - val_1:{} - val_2:{}", df.format(val), df.format(val_1), df.format(val_2));
            if (ArrayUtil.isPivot(val_2, val_1, val)) {
                log.debug("isPivot val_1:{}", val_1);
                impulse = MathAnalysis.gain(trendStartVal, val_1);
                impulse = Math.abs(impulse);
                trendStartVal = val_1;
                trendStartIndex = i - 1;
            }
            double rate = MathAnalysis.gain(trendStartVal, val);
            double rate_1 = MathAnalysis.gain(trendStartVal, val_1);
            if (isCrossinLevels(rate_1, rate, impulse))
                result[i - 1] = val_1;

        }
        return result;
    }

    /* @param values
    *  @param minimumReatracementRate
    *  @return 0 if fibonacci is neutral(i.e no level have been crossed). 1,2,3(-1,-2,-3) depending which is the last
     *  level of fibonacci that has been crossed. The signum depends if reatracement levels refers to a positive
     *  impulse or negative slope
    */
    public double[] fibonacciIndicator(double[] values, int minimumReatracementRate) {

        double[] result = new double[values.length];
        double[] zigzag = ZIGZAG.zigZag(values, minimumReatracementRate);
        double[] fibonacciLevels = fibonacci(values, minimumReatracementRate);
        int level = 0;
        int signum = 1;
        for (int i = 2; i < zigzag.length; i++) {
            double val_2 = zigzag[i - 2];
            double val_1 = zigzag[i - 1];
            double val = zigzag[i];
            /* The signum determines if we are in an ascending or descending slope */


            log.debug("val: {} - val_1:{} - val_2:{} - level:{}", df.format(val), df.format(val_1), df.format(val_2));
            if (ArrayUtil.isPivot(val_2, val_1, val)) {
                /* whenever we find a pivot we reset the level */
                log.debug("pivot:val_1:{}", df.format(val_1));
                level = 0;
                if (ArrayUtil.isUpperPivot(val_2, val_1, val)) {
                    /* after a maximum we are in a negative slope */
                    signum = -1;

                } else if (ArrayUtil.isLowerPivot(val_2, val_1, val)) {
                    /* after a minimum we are in a positive slope */
                    signum = 1;

                }
            }
            if (fibonacciLevels[i] != 0)
                level += 1;

            result[i] = level * signum;

        }

        return result;
    }

    /* @param values
    *  @param minimumReatracementRate
    *  @return 1 if fibonacci indicator is positive and -1 if negative. When neutral keeps the last value
    */
    public double[] fibonacciSignal(double[] values, int minimumReatracementRate,int retracementThreshold) {

        double[] fibonacciIndicator = fibonacciIndicator(values, minimumReatracementRate);
        double[] result = new double[fibonacciIndicator.length];
        for (int i = 1; i < fibonacciIndicator.length; i++) {
            if (result[i - 1] >= 0 && fibonacciIndicator[i] < -retracementThreshold)
                result[i] = -1;
            else if(result[i - 1] <= 0 && fibonacciIndicator[i] > retracementThreshold)
                result[i] = 1;
            else
                result[i]=result[i-1];
        }
        return result;
    }

    private boolean isCrossinLevels(double rate_1, double rate, double impulse) {
        return isCrossingLevel(rate_1, rate, level38, impulse) || isCrossingLevel(rate_1, rate, level50, impulse) || isCrossingLevel
                (rate_1, rate, level61, impulse);
    }

    private boolean isCrossingLevel(double rate_1, double rate, double level, double impulse) {
        if (log.isDebugEnabled())
            log.debug("isCrossingLevel level:{} - rate_1:{} - rate:{} - impulse:{}", level, df.format(rate_1), df.format(rate),
                    df.format(impulse));

        double retracement = Math.abs(rate / impulse);
        double retracement_1 = Math.abs(rate_1 / impulse);
        boolean result = (retracement_1 < level && level < retracement) || (retracement_1 > level && level > retracement);

        log.debug("isCrossingLevel result ratracement:{} - retracement_1:{} - result:{}", df.format(retracement),
                df.format(retracement_1), result);
        return result;

    }


}
