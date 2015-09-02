package ta;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ArrayUtil;



public class Indicators {
	
	private static Logger log= LoggerFactory.getLogger(Indicators.class);




    /**
     * BOLLINGER
     */

	/**
	 * @param values
	 *            the array of values ordered from newer to older
	 * @param periods
	 *            number of period to compute
	 * @return
	 */
	public static double[][] bollingerBands(int startIndex, int endIndex,
			double[] values, int periods) {
		int length = endIndex - startIndex;
		double[][] result = new double[3][length];
		for (int i = startIndex; i < length; i++) {
			int start = i;
			double[] bollingerBand = new double[3];
			if ((start + periods) <= length) {
				bollingerBand = bollingherBandFormula(start, periods, values);
			}
			result[0][i] = bollingerBand[0];
			result[1][i] = bollingerBand[1];
			result[2][i] = bollingerBand[2];
		}
		return result;
	}



    /**
     * WILDER:
     * Average True range.
     */

	/**
	 * ATR iterator
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param periods
	 * @return
	 */
	public static double[] atr(int startIndex, int endIndex, double[] highs,
			double[] lows, int periods) {
		int length = endIndex - startIndex;
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = atrFormula(startIndex + i, endIndex, highs,lows, periods,(short)0);
		}

		return result;
	}

    /**
     * TRM1 iterator. Price descending by date
     * @param startIndex
     * @param endIndex
     * @param highs
     * @param lows
     * @return
     */
    public static double[] trM1(int startIndex, int endIndex, double[] highs,double[] lows) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = trM1(highs[i],lows[i]);
        }

        return result;
    }
    /**
     * TRM2 iterator. Price descending by date
     * @param startIndex
     * @param endIndex
     * @param highs
     * @param prices
     * @return
     */
    public static double[] trM2(int startIndex, int endIndex, double[] highs,double[] prices) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-1; i++) {
            result[i] = trM2(highs[startIndex+i],prices[startIndex+i+1]);
        }

        return result;
    }
    /**
     * TRM3 iterator. Price descending by date
     * @param startIndex
     * @param endIndex
     * @param lows
     * @param prices
     * @return
     */
    public static double[] trM3(int startIndex, int endIndex, double[] lows,double[] prices) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-1; i++) {
            result[i] = trM3(lows[i],prices[i+1]);
        }

        return result;
    }


    /* FORMULAS */

	public static double[] bollingherBandFormula(int startIndex, int periods,
												 double[] values) {
		double[] result = new double[3];
		double sma = MA.smaFormula(startIndex, periods, values);
		double[] sliced = ArrayUtil.slice(values, startIndex, startIndex
				+ periods);
		StandardDeviation sdFormula = new StandardDeviation();
		double sd = sdFormula.evaluate(sliced);
		double sdx2 = sd * 2;
		result[0] = sma - sdx2;
		result[1] = sma;
		result[2] = sma + sdx2;

		return result;
	}

	/* Wilder's smoothing
	 * ATR recursive formula. ATR 14 Day smoothed
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param periods
	 * @return
	 */
	public static double atrFormula(int startIndex, int endIndex, double[] highs, double[] lows,int periods,short cursor) {

		int currentIndex=startIndex+cursor;
		double atrPrev = 0;
		
		// Current ATR = [(Prior ATR x 13) + Current TR] / 14
		double result = 0D;
		if (currentIndex+periods < endIndex && cursor<periods) {
			atrPrev = atrFormula(startIndex, endIndex, highs,lows, periods,++cursor);
			result = ((atrPrev*(periods-1)) + trM1(highs[currentIndex], lows[currentIndex]))/periods;
		} else if (cursor==periods || currentIndex+periods == endIndex) {
			double sum = 0;
			for (int j = 0; j < periods; j++) {
				sum += trM1(highs[currentIndex + j], lows[currentIndex + j]);
			}

			result = sum / periods;

		} else {
			result = 0;
		}

		return result;
		
	}


	
	/* point to point formulas */

    /*Wilder started with a concept called True Range (TR), which is defined as the greatest of the following:
    Method 1: Current High less the current Low
    Method 2: Current High less the previous Close (absolute value)
    Method 3: Current Low less the previous Close (absolute value)
    */

    /**
     *
     * @param currentHigh
     * @param currentLow
     * @return  True range Method1
     */
	private static double trM1(double currentHigh, double currentLow) {
		return currentHigh - currentLow;
	}

    /**
     *
     * @param currentHigh
     * @param prevClose
     * @return  True range Method2
     */
    private static double trM2(double currentHigh, double prevClose) {
        return Math.abs(currentHigh - prevClose);
    }
    /**
     *
     * @param currentLow
     * @param prevClose
     * @return  True range Method3
     */
    private static double trM3(double currentLow, double prevClose) {
        return Math.abs(currentLow - prevClose);
    }

    /* point to point formulas */
    private static double tr1(double high, double low) {
        return high - low;
    }
	

	
}
