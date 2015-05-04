package ta;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ArrayUtil;

/**
 * MOOVING AVERAGES
 *
 * Trendrating SA Created by filippo on 4/27/15.
 */
public class MA {
    private static Logger log= LoggerFactory.getLogger(MA.class);
    /**
     * MOVING AVERAGES
     */

    /**
     * @param values
     *            the array of values ordered from newer to older
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
     *            the array of values ordered from newer to older
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
     *  MOVING AVERAGES CONVERGENCE/DIVERGENCE
     */

    /**
     * MACD iterator
     * @param startIndex
     * @param endIndex
     * @param values
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



    /*
	 * Smoothing formula for EMA
	 * @param values the array of values ordered from newer to older
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
    public static double macdLineFormula(int startIndex, int endIndex,
                                         double[] values) {
        double ema12=emaFormula(startIndex, endIndex, values, 12, 0);
        double ema26=emaFormula(startIndex, endIndex, values, 26, 0);
        double emaLine=(ema26>0&&ema12>0)?ema12-ema26:0;
        return emaLine;
    }


}
