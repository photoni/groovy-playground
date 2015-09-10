package ta;

import util.ArrayUtil;

/**
 * created by filippo on 9/2/15.
 */
public class RETRACEMENT {
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
     * @return the zigzag for the given rate and fibonacci levels
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
            if (ArrayUtil.isPivot(val_2, val_1, val)) {
                impulse = MathAnalysis.gain(trendStartVal, val_1);
                trendStartVal = val_1;
                trendStartIndex = i - 1;
            }
            double rate = MathAnalysis.gain(trendStartVal, val);
            double rate_1 = MathAnalysis.gain(trendStartVal, val - 1);
            if (isCrossinLevels(rate_1, rate, impulse))
                result[i - 1] = val_1;

        }
        return result;
    }

    private boolean isCrossinLevels(double rate_1, double rate, double impulse) {
        return isCrossingLevel(rate_1, rate, level38, impulse) || isCrossingLevel(rate_1, rate, level50, impulse) || isCrossingLevel
                (rate_1, rate, level61, impulse);
    }

    private boolean isCrossingLevel(double rate_1, double rate, double level, double impulse) {
        if (impulse > 0) {
            double retracement = Math.abs(rate/impulse);
            double retracement_1 = Math.abs(rate_1/impulse);
            return ((retracement_1 < level && level < retracement) || (retracement_1 > level && level > retracement));
        }else
            return false;
    }


}
