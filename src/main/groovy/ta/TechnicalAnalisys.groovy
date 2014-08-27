package ta

import groovy.lang.Singleton;
import groovy.util.logging.Slf4j;

import com.tictactec.ta.lib.Core
import com.tictactec.ta.lib.MInteger
import com.tictactec.ta.lib.RetCode

@Slf4j
@Singleton
class TechnicalAnalisys {

	static final int PERIODS = 20

	/**
	 * LOOKS FINE!!!!!!
	 * Simple moving average. <br/>
	 * Smooth price action following. <br/>
	 * Full scan. <br/>
	 * Period 30 days. <br/>
	 * @param input
	 * @param out
	 * @return
	 */
	double[] sma(double[] input, boolean cutOut){
		/* The number of unit periods(days) to average together */
		int period = PERIODS
		def startIndex = 0
		def endIndex = input.length - 1
		MInteger begin = new MInteger()
		MInteger length = new MInteger()
		Core core=new Core()
		double[] out=new double[input.length]
		long start = System.nanoTime();
		RetCode retCode = core.sma(startIndex, endIndex, input, period, begin, length, out)
		long end = System.nanoTime();
		log.debug("sma: Total time nano : {}", end - start);
		double[] adjAut=adjustOutput( out, begin, length,cutOut)
		return adjAut
	}
	/**
	 * Weighted moving average. <br/>
	 * Close price action following. <br/>
	 * Full scan. <br/>
	 * Period 30 days. <br/>
	 * @param input
	 * @param out
	 * @return
	 */
	double[] wma(double[] input, boolean cutOut){
		/* The number of unit periods(days) to average together */
		int period = PERIODS
		def startIndex = 0
		def endIndex = input.length - 1
		MInteger begin = new MInteger()
		MInteger length = new MInteger()
		Core core=new Core()
		double[] out=new double[input.length]
		long start = System.nanoTime();
		RetCode retCode = core.wma(startIndex, endIndex, input, period, begin, length, out)
		long end = System.nanoTime();
		log.debug("wma: Total time nano : {}", end - start)
		double[] adjAut=adjustOutput( out, begin, length,cutOut)
		return adjAut
	}

	/**
	 * LOOKS WRONG!!!!!
	 * Exponential moving average. <br/>
	 * Close price action following. Smooth alder <br/>
	 * Full scan. <br/>
	 * Period 30 days. <br/>
	 * @param input
	 * @param out
	 * @return
	 */
	double[] ema(double[] input, boolean cutOut){
		/* The number of unit periods(days) to average together */
		int period = PERIODS
		def startIndex = 0
		def endIndex = input.length - 1
		MInteger begin = new MInteger()
		MInteger length = new MInteger()
		Core core=new Core()
		double[] out=new double[input.length]
		long start = System.nanoTime();
		RetCode retCode = core.ema(startIndex, endIndex, input, period, begin, length, out)
		long end = System.nanoTime();
		//log.debug("ema: Total time nano : {}", end - start)
		double[] adjAut=adjustOutput( out, begin, length,cutOut)
		return adjAut
	}

	/**
	 * Triangular moving average. <br/>
	 * Close price action following. Smooth alder <br/>
	 * Full scan. <br/>
	 * Period 30 days. <br/>
	 * @param input
	 * @param out
	 * @return
	 */
	double[] tma(double[] input, boolean cutOut){
		/* The number of unit periods(days) to average together */
		int period = PERIODS
		def startIndex = 0
		def endIndex = input.length - 1
		MInteger begin = new MInteger()
		MInteger length = new MInteger()
		Core core=new Core()
		double[] out=new double[input.length]
		long start = System.nanoTime()
		RetCode retCode = core.trima(startIndex, endIndex, input, period, begin, length, out)
		long end = System.nanoTime()
		log.debug("tma: Total time nano : {}", end - start)
		double[] adjAut=adjustOutput( out, begin, length,cutOut)
		return adjAut
	}





	Map<String,double[]> multi(double[] prices,double[] highs,double[] lows,List<String> types, boolean cutOut){
		Map<String,double[]> result= new HashMap<String,double[]>()
		types.each {obj ->
			if("tma".equalsIgnoreCase(obj))
				result.put("tma", tma(prices,cutOut))
			else if("sma".equalsIgnoreCase(obj))
				result.put("sma", sma(prices,cutOut))
			else if("wma".equalsIgnoreCase(obj))
				result.put("wma", wma(prices,cutOut))
			else if("ema".equalsIgnoreCase(obj))
				result.put("ema", ema(prices,cutOut))
			else if("isma".equalsIgnoreCase(obj))
				result.put("isma", Indicators.sma(0,prices.length,prices, 20))
			else if("iema".equalsIgnoreCase(obj))
				result.put("iema", Indicators.ema(0,prices.length,prices, 20))
			else if("iroc".equalsIgnoreCase(obj))
				result.put("iroc", Indicators.roc(0,prices.length,prices, 20))
			else if("iboll".equalsIgnoreCase(obj)){
				def bollingerBands = Indicators.bollingerBands(0,prices.length,prices, 20)
				result.put("iboll-lower", bollingerBands[0])
				result.put("iboll-middle", bollingerBands[1])
				result.put("iboll-higher", bollingerBands[2])
			}else if("iatr".equalsIgnoreCase(obj))
				result.put("iatr", Indicators.atr(0,prices.length,highs,lows, 14))
		}
		return result;
	}

	private double[] adjustOutput(double[] out, MInteger begin, MInteger length, boolean cutOut) {
		double[] adjAut=new double[out.length]
		if(cutOut)
			adjAut = Arrays.copyOfRange(out, begin.value, (length.value+begin.value-1));
		else
			out.eachWithIndex {obj,i ->
				if(i< length.value)adjAut[i]=obj
				else adjAut[i]=0
			}
		return adjAut
	}
}
