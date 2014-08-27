package ta;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import util.ArrayUtil;

public class Indicators {

	/**
	 * @param values
	 *            the array of values ordered from newer to older
	 * @param periods
	 *            number of period to compute
	 * @return
	 */
	public static double[] sma(int startIndex, int endIndex, double[] values,
			int periods) {
		int length = endIndex - startIndex;
		double[] result = new double[length];
		for (int i = startIndex; i < length; i++) {
			int start = i;
			double sma = 0;
			if ((start + periods) <= length) {
				sma = smaFormula(start, periods, values);
			}
			result[start] = sma;
		}
		return result;
	}

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
	 * @param values
	 *            the array of values ordered from newer to older
	 * @param periods
	 *            number of period to compute
	 * @return
	 */
	public static double[] ema(int startIndex, int endIndex, double[] values,
			int periods) {
		int length = endIndex - startIndex;
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = emaFormula(startIndex + i, endIndex, values, periods);
		}

		return result;
	}

	/**
	 * @param values
	 *            the array of values ordered from newer to older
	 * @param periods
	 *            number of period to compute
	 * @return Rate of change
	 */
	public static double[] roc(int startIndex, int endIndex, double[] values,
			int periods) {
		int length = endIndex - startIndex;
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = rocFormula(startIndex + i, endIndex, values, periods);
		}

		return result;
	}

	/* formulas */

	private static double[] bollingherBandFormula(int startIndex, int periods,
			double[] values) {
		double[] result = new double[3];
		double sma = smaFormula(startIndex, periods, values);
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
			result[i] = atrFormula(startIndex + i, endIndex, highs,lows, periods);
		}

		return result;
	}

	/**
	 * ATR recursive formula
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param periods
	 * @return
	 */
	private static double atrFormula(int startIndex, int endIndex, double[] highs, double[] lows,int periods){
		double atrPrev = 0;
		
		// Current ATR = [(Prior ATR x 13) + Current TR] / 14
		double result = 0D;
		if ((startIndex + periods) < endIndex) {
			atrPrev = atrFormula(startIndex + 1, endIndex, highs,lows, periods);
			result = ((atrPrev*(periods-1)) + tr(highs[startIndex],lows[startIndex]))/periods;
		} else if ((startIndex + periods) == endIndex) {
			double sum = 0;
			for (int j = 0; j < periods; j++) {
				sum += tr(highs[startIndex + j],lows[startIndex + j]);
			}

			result = sum / periods;

		} else {
			result = 0;
		}

		return result;
		
	}
	private static double tr(double high, double low) {
		return high - low;
	}

	private static double rocFormula(int startIndex, int endIndex,
			double[] values, int periods) {
		double result = 0;

		int prevCloseIndex = startIndex + periods;
		if (prevCloseIndex < endIndex)
			result = ((values[startIndex] - values[prevCloseIndex]) / values[prevCloseIndex]) * 100;

		return result;
	}

	/*
	 * @param values the array of values ordered from newer to older
	 * 
	 * @param periods number of period to compute
	 * 
	 * @return
	 */
	private static double emaFormula(int startIndex, int endIndex,
			double[] values, int periods) {

		double emaPrev = 0;
		// (2 / (Time periods + 1) )
		double multiplier = 2D / (periods + 1);
		// {Close - EMA(previous day)} x multiplier + EMA(previous day)
		double result = 0D;
		if ((startIndex + periods) < endIndex) {
			emaPrev = emaFormula(startIndex + 1, endIndex, values, periods);
			result = (values[startIndex] - emaPrev) * multiplier + emaPrev;
		} else if ((startIndex + periods) == endIndex) {
			double sum = 0;
			for (int j = 0; j < periods; j++) {
				sum += values[startIndex + j];
			}

			result = sum / periods;

		} else {
			result = 0;
		}

		return result;
	}

	/*
	 * @param start start index
	 * 
	 * @param periods number of periods
	 * 
	 * @param values array of values
	 * 
	 * @return
	 */
	private static double smaFormula(int startIndex, int periods,
			double[] values) {
		double sma = 0;
		double sum = 0;
		for (int j = 0; j < periods; j++) {
			sum += values[startIndex + j];
		}
		sma = sum / periods;
		return sma;

	}
}
