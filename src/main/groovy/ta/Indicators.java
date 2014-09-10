package ta;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ArrayUtil;



public class Indicators {
	
	private static Logger log= LoggerFactory.getLogger(Indicators.class);
	
	

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
			result[i] = emaFormula(startIndex + i, endIndex, values, periods,0);
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
	 * ADX iterator
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param periods
	 * @return
	 */
	public static double[] adx(int startIndex, int endIndex,double[] values ,double[] highs,
			double[] lows, int periods) {
		int length = endIndex - startIndex;
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = dxFormula(startIndex+i, endIndex, values, highs, lows, periods);
			log.debug("dx {}: {}",i,result[i]);
		}
		
		result=sma(startIndex, endIndex, result, periods);

		return result;
	}
	
	/**
	 * MACD iterator
	 * @param startIndex
	 * @param endIndex
	 * @param values
	 * @param periods
	 * @return
	 */
	public static double[][] macd(int startIndex, int endIndex, double[] values) {
		int length = endIndex - startIndex;
		double[][] result= new double[3][length];
		
		//MACD Line: (12-day EMA - 26-day EMA) 		
		double[] macdLineResult = new double[length];
		for (int i = 0; i < length; i++) {
			double macdLine =macdLineFormula(startIndex+i, endIndex, values);
			macdLineResult[i]=macdLine;
		}
		
		double[] macdSignalResult = new double[length];
		//Signal Line: 9-day EMA of MACD Line
		for (int i = 0; i < length; i++) {
			double macdSignal =emaFormula(startIndex+i, endIndex, macdLineResult,9,0);
			macdSignalResult[i]=macdSignal;
		}
		
		double[] macdHistogramResult = new double[length];
		//MACD Histogram: MACD Line - Signal Line
		for (int i = 0; i < length; i++) {
			double macdHistogram =macdLineResult[startIndex+i]-macdSignalResult[startIndex+i];
			macdHistogramResult[i]=macdHistogram;
		}	
		result[0]=macdLineResult;
		result[1]=macdSignalResult;
		result[2]=macdHistogramResult;
		
		return result;

	}
	
	
	/**
	 * DX
	 * @param startIndex
	 * @param endIndex
	 * @param values
	 * @param highs
	 * @param lows
	 * @param periods
	 * @param rev
	 * @return
	 */
	public static double dxFormula(int startIndex, int endIndex, double[] values,double[] highs,double[] lows, int periods){
		double dxPlus=diFormula(startIndex, endIndex, values, highs, lows, periods, false);
		double dxMinus=diFormula(startIndex, endIndex, values, highs, lows, periods, true);
		double denominator = dxPlus+dxMinus;
		log.debug("dx {} den: {} - dxP: {} - dxM: {}",startIndex,denominator);
		double dx=denominator!=0?(Math.abs(dxPlus-dxMinus)/denominator):0;
		return dx;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param values
	 * @param highs
	 * @param lows
	 * @param periods
	 * @param rev
	 * @return
	 */
	public static double diFormula(int startIndex, int endIndex, double[] values,double[] highs,double[] lows, int periods,boolean rev) {
		double atr=atrFormula(startIndex, endIndex, highs, lows, periods, (short)0);
		double dm=dmFormula(startIndex, endIndex, values, periods, (short)0, rev);
		double di=(dm/atr)*100;		
		return di;

	}

	
	
	
	/* period based formulas */
	private static double macdLineFormula(int startIndex, int endIndex,
			double[] values) {
		double ema12=emaFormula(startIndex, endIndex, values, 12, 0);
		double ema26=emaFormula(startIndex, endIndex, values, 26, 0);
		double emaLine=(ema26>0&&ema12>0)?ema12-ema26:0;
		return emaLine;
	}

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

	/* Wilder's smoothing
	 * ATR recursive formula. ATR 14 Day smoothed
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param periods
	 * @return
	 */
	private static double atrFormula(int startIndex, int endIndex, double[] highs, double[] lows,int periods,short cursor) {

		int currentIndex=startIndex+cursor;
		double atrPrev = 0;
		
		// Current ATR = [(Prior ATR x 13) + Current TR] / 14
		double result = 0D;
		if (currentIndex+periods < endIndex && cursor<periods) {
			atrPrev = atrFormula(startIndex, endIndex, highs,lows, periods,++cursor);
			result = ((atrPrev*(periods-1)) + tr(highs[currentIndex],lows[currentIndex]))/periods;
		} else if (cursor==periods || currentIndex+periods == endIndex) {
			double sum = 0;
			for (int j = 0; j < periods; j++) {
				sum += tr(highs[currentIndex + j],lows[currentIndex + j]);
			}

			result = sum / periods;

		} else {
			result = 0;
		}

		return result;
		
	}
	
	/*
	 * Wilder's smoothing
	 * DM recursive formula. DM 14 Day smoothed
	 * @param startIndex
	 * @param endIndex
	 * @param highs
	 * @param lows
	 * @param periods
	 * @return
	 */
	private static double dmFormula(int startIndex, int endIndex, double[] values,int periods,short cursor,boolean rev) {
		int currentIndex=startIndex+cursor;
		double smoothedDmPrev = 0;
		
		// Current smoothedDM = [(Prior smoothedDM x 13) + Current DM] / 14
		double result = 0D;
		if (currentIndex+periods < endIndex-1 && cursor<periods) {
			smoothedDmPrev = dmFormula(startIndex, endIndex, values, periods,++cursor,rev);
			result = ((smoothedDmPrev*(periods-1)) + dm(values[currentIndex],values[currentIndex+1],rev))/periods;
		} else if (cursor==periods || currentIndex+periods == endIndex-1) {
			double sum = 0;
			for (int j = 0; j < periods; j++) {
				sum += dm(values[currentIndex + j],values[currentIndex + j+1],rev);
			}

			result = sum / periods;

		} else {
			result = 0;
		}

		return result;
		
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
	 * Smoothing formula for EMA
	 * @param values the array of values ordered from newer to older
	 * 
	 * @param periods number of period to compute
	 * 
	 * @return
	 */
	private static double emaFormula(int startIndex, int endIndex,
			double[] values, int periods,int cursor) {

		int currentIndex=startIndex+cursor;
		double emaPrev = 0;
		// (2 / (Time periods + 1) )
		double multiplier = 2D / (periods + 1);
		// {Close - EMA(previous day)} x multiplier + EMA(previous day)
		double result = 0D;
		if (currentIndex+periods < endIndex && cursor<periods) {
			emaPrev = emaFormula(startIndex, endIndex, values, periods,++cursor);
			result = (values[currentIndex] - emaPrev) * multiplier + emaPrev;
		} else if (cursor==periods || currentIndex+periods == endIndex) {
			double sum = 0;
			for (int j = 0; j < periods; j++) {
				sum += values[currentIndex + j];
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
	
	/* point to point formulas */
	private static double tr(double high, double low) {
		return high - low;
	}
	
	private static double dm(double current, double previous,boolean rev) {
		double diff = current - previous;
		diff=rev?diff*-1:diff;
		return diff>0?diff:0;
	}
	
}
