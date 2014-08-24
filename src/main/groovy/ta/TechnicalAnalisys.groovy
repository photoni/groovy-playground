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
		log.debug("ema: Total time nano : {}", end - start)
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


	Map<String,double[]> multi(double[] input,List<String> types, boolean cutOut){
		Map<String,double[]> result= new HashMap<String,double[]>()
		types.each {obj ->
			if("tma".equalsIgnoreCase(obj))
				result.put("tma", tma(input,cutOut))
			else if("sma".equalsIgnoreCase(obj))
				result.put("sma", sma(input,cutOut))
			else if("wma".equalsIgnoreCase(obj))
				result.put("wma", wma(input,cutOut))
			else if("ema".equalsIgnoreCase(obj))
				result.put("ema", ema(input,cutOut))
			else if("isma".equalsIgnoreCase(obj))
				result.put("isma", Indicators.sma(0,input.length,input, 20))
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
