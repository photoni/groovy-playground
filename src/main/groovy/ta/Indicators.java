package ta;

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
			double sma = 0;
			double sum = 0;
			if ((i + periods) <= length) {
				for (int j = 0; j < periods; j++) {
					sum += values[i + j];
				}
			}
			sma = sum / periods;
			result[i] = sma;
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
				sum += values[startIndex+j];
			}

			result = sum / periods;

		} else {
			result = 0;
		}

		return result;
	}
}
