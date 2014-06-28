package ta;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j
import model.Security

import org.junit.Test

import service.SecurityService

import com.tictactec.ta.lib.Core
import com.tictactec.ta.lib.MInteger

@Slf4j
class TATest {

	@Test
	public void smaTest() {
		/**
		 * The number of periods to average together.
		 */
		int PERIODS_AVERAGE = 30
		MInteger begin = new MInteger()
		MInteger length = new MInteger()
		SecurityService ss= SecurityService.instance
		Core core=new Core()
		Security s=ss.getSecurity("GOOGL")
		int historyLength=s.getHistory().size()
		double[] prices=new double[2480]
		s.getHistory().eachWithIndex { obj, i -> prices[i]=obj.adjClose}
		//s.getHistory().eachWithIndex { val,i -> log.debug(" price: ${i} - val : ${val.adjClose}")}
		TechnicalAnalisys ta=TechnicalAnalisys.instance
		double[] out= ta.sma(prices)
		//out.eachWithIndex { val,i -> log.debug(" aver: ${i} - val : ${val}")}
	}
}
