package ta

import groovy.lang.Singleton;
import groovy.util.logging.Slf4j;

import com.tictactec.ta.lib.Core
import com.tictactec.ta.lib.MInteger
import com.tictactec.ta.lib.RetCode

@Slf4j
@Singleton
class TechnicalAnalisys {

	/**
	 * Simple moving average. <br/>
	 * Full scan. <br/>
	 * Period 30 days. <br/>
	 * @param input
	 * @param out
	 * @return
	 */
	double[] sma(double[] input){
		/* The number of periods to average together */
		int period = 30
		def startIndex = 0
		def endIndex = input.length - 1
		MInteger begin = new MInteger()
		MInteger length = new MInteger()
		Core core=new Core()
		double[] out=new double[input.length]
		long start = System.nanoTime();
		RetCode retCode = core.sma(startIndex, endIndex, input, period, begin, length, out)
		long end = System.nanoTime();
		log.debug("Total time nano : {}", end - start);
		int[] cutOut = Arrays.copyOfRange(out, 0, length.value);
		return cutOut
	}
}
