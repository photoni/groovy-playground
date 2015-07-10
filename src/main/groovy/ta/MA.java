package ta;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ArrayUtil;

/**
 *  MACD
 *
 *  MOVING AVERAGES CONVERGENCE/DIVERGENCE
 *  Calculation:
 *  MACD Line: (12-day EMA - 26-day EMA)
 *  Signal Line: 9-day EMA of MACD Line
 *  MACD Histogram: MACD Line - Signal Line
 *  Interpretation;
 *  Signal Line Crossovers - A bullish crossover occurs when the MACD turns up and crosses above the signal line. A bearish crossover occurs when the MACD turns down and crosses below the signal line
 *  Center line Crossovers - A bullish centerline crossover occurs when the MACD Line moves above the zero line to
 *  turn positive. This happens when the 12-day EMA of the underlying security moves above the 26-day EMA. A bearish centerline crossover occurs when the MACD moves below the zero line to turn negative. This happens when the 12-day EMA moves below the 26-day EMA.
 *
 *  Created by filippo on 4/27/15.
 */
public class MA {
    private static Logger log= LoggerFactory.getLogger(MA.class);
    /**
     * MOVING AVERAGES
     */

    /**
     * @param values
     *            the array of values ordered from older to newer
     * @param periods
     *            number of period to compute
     * @return The Simple Moving Average
     *
     */
    public static double[] sma(int startIndex, int endIndex, double[] values,
                               int periods) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = startIndex+periods-1; i < length; i++) {
            int start = i;
            double sma = 0;
            if ((start) <= length) {
                sma = smaFormula(start, periods, values);
            }
            result[start] = sma;
        }
        return result;
    }

    /**
     * @param values
     *            the array of values ordered from older to newer
     * @param periods
     *            number of period to compute
     * @return
     */
    public static double[] ema(int startIndex, int endIndex, double[] values,
                               int periods) {
        int loops = endIndex - startIndex;
        double[] result = new double[loops];
        for (int i = 0; i < loops; i++) {

            double ema=0;
            ema = emaFormula(startIndex + i, endIndex, values, periods, 0);


            result[startIndex+i] = ema;
        }

        return result;
    }



    /**
     * MACD iterator
     * @param startIndex
     * @param endIndex
     * @param values from older to newer
     * @return
     */
    public static double[][] macd(int startIndex, int endIndex, double[] values,int shortPeriods,int longPeriods) {
        int length = endIndex - startIndex;
        double[][] result= new double[7][length];

        //MACD Line: (12-day EMA - 26-day EMA)
        double[] macdLineResult = new double[length];
        double[] ema12Result = new double[length];
        double[] ema26Result = new double[length];
        double[] centerLineCrossResult = new double[length];
        for (int i = 0; i < length; i++) {
            double[] macdLine =macdLineFormula(startIndex+i, endIndex, values,shortPeriods,longPeriods);

            macdLineResult[i]=macdLine[0];
            ema12Result[i]=macdLine[1];
            ema26Result[i]=macdLine[2];
            centerLineCrossResult[i]=macdLine[3];
        }

        double[] macdSignalResult = new double[length];
        double[] signalLineCrossResult = new double[length];
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
            signalLineCrossResult[i]=macdHistogram>0?1:(macdHistogram<0?-1:0);
        }



        result[0]=macdLineResult;
        result[1]=macdSignalResult;
        result[2]=macdHistogramResult;
        result[3]=ema12Result;
        result[4]=ema26Result;
        result[5]=centerLineCrossResult;
        result[6]=signalLineCrossResult;

        return result;

    }



    /*
	 * Smoothing formula for EMA
	 * @param values the array of values ordered from older to newer
	 *
	 * @param periods number of period to compute
	 *
	 * @return
	 */
    public static double emaFormula(int startIndex, int endIndex,
                                    double[] values, int periods,int cursor) {

        int backwardIterations=periods-1;
        int currentIndex=startIndex+cursor;
        double emaPrev = 0;
        // (2 / (Time periods + 1) )
        double multiplier = 2D / (periods + 1);
        // {Close - EMA(previous day)} x multiplier + EMA(previous day)
        double result = 0D; //
        if (currentIndex>backwardIterations  && Math.abs(cursor)<(backwardIterations)){
            emaPrev = emaFormula(startIndex, endIndex, values, periods,--cursor);
            double close = values[currentIndex];
            result = (close - emaPrev) * multiplier + emaPrev;
            log.debug("ema - index: {}- cursor: {}- ema: {} - close:{} - emaPrev:{}",currentIndex,cursor,result,close,
                    emaPrev);

        } else if (Math.abs(cursor)==backwardIterations || (currentIndex-backwardIterations)==0) {

            result = smaFormula(currentIndex,periods,values);
            log.debug("sma -    index: {}- cursor: {}- ema: {} - emaPrev:{}",currentIndex,cursor,result,emaPrev);

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
    public static double smaFormula(int startIndex, int periods,
                                     double[] values) {
        double sma = 0;
        double sum = 0;
        for (int j = 0; j < periods; j++) {
            sum += values[startIndex - j];
        }
        sma = sum / periods;
        log.debug("index: {}, sum: {}, periods:{}, sma: {}",startIndex,sum,periods,sma);
        return sma;

    }
    /* period based formulas */
    public static double[] macdLineFormula(int startIndex, int endIndex,
                                         double[] values) {
        double ema12=emaFormula(startIndex, endIndex, values, 12, 0);
        double ema26=emaFormula(startIndex, endIndex, values, 26, 0);
        double emaLine=(ema26>0&&ema12>0)?ema12-ema26:0;
        /* Centerline Crossovers */
        double centerLineCross=emaLine>0?1:(emaLine<0?-1:0);

        return new double[]{emaLine,ema12,ema26,centerLineCross};
    }

    /* period based formulas */
    public static double[] macdLineFormula(int startIndex, int endIndex,
                                           double[] values,int shortPeriods,int longPeriods) {
        double ema12=emaFormula(startIndex, endIndex, values, shortPeriods, 0);
        double ema26=emaFormula(startIndex, endIndex, values, longPeriods, 0);
        double emaLine=(ema26>0&&ema12>0)?ema12-ema26:0;
        /* Centerline Crossovers */
        double centerLineCross=emaLine>0?1:(emaLine<0?-1:0);

        return new double[]{emaLine,ema12,ema26,centerLineCross};
    }


}
