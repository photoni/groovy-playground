package ta

import groovy.lang.Singleton;
import groovy.util.logging.Slf4j
import org.apache.commons.lang.ArrayUtils
import util.ArrayUtil;


@Slf4j
@Singleton
class TechnicalAnalisys {

	static final int PERIODS = 20

	Map<String,Map> multi(double[] prices,double[] highs,double[] lows,List<String> types, boolean cutOut){
		Map<String,double[]> overlays= new HashMap<String,double[]>()
		Map<String,double[]> indicators= new HashMap<String,double[]>()
		Map<String,Map> result= new HashMap<String,Map>()
		types.each {obj ->
			if("isma".equalsIgnoreCase(obj)) {
				def sma = MA.sma(0,prices.length,ArrayUtil.reverse(prices), 20)
				overlays.put("isma", ArrayUtil.reverse(sma))
			}
			else if("iema".equalsIgnoreCase(obj)) {
				def ema = MA.ema(0, prices.length, ArrayUtil.reverse(prices), 20)
				overlays.put("iema", ArrayUtil.reverse(ema))
			}
			else if("iroc".equalsIgnoreCase(obj))
				result.put("iroc", Indicators.roc(0,prices.length,prices, 20))
			else if("iboll".equalsIgnoreCase(obj)){
				def bollingerBands = Indicators.bollingerBands(0,prices.length,prices, 20)
				result.put("iboll-lower", bollingerBands[0])
				result.put("iboll-middle", bollingerBands[1])
				result.put("iboll-higher", bollingerBands[2])
			}else if("imacd".equalsIgnoreCase(obj)){
				def macd = MA.macd(0,prices.length,ArrayUtil.reverse(prices),12,26)
				indicators.put("macd-line", ArrayUtil.reverse(macd[0]))
				indicators.put("macd-signal", ArrayUtil.reverse(macd[1]))
				indicators.put("macd-histogram", ArrayUtil.reverse(macd[2]))
				indicators.put("centerLineCross", ArrayUtil.reverse(macd[5]))
				indicators.put("signalLineCross", ArrayUtil.reverse(macd[6]))
				overlays.put("macd-ema12", ArrayUtil.reverse(macd[3]))
				overlays.put("macd-ema26", ArrayUtil.reverse(macd[4]))
			}else if("iatr".equalsIgnoreCase(obj))
				result.put("iatr", Indicators.atr(0,prices.length,highs,lows, 14))
            else if("iadx".equalsIgnoreCase(obj)){
				indicators.put("adx", ArrayUtil.reverse(ADX.adx(0, prices.length, ArrayUtil.reverse(highs), ArrayUtil
						.reverse(lows),14)))
                double[][] dmCombined=ADX.dm(0,prices.length, ArrayUtil.reverse(highs), ArrayUtil.reverse(lows))
				indicators.put("di+", ArrayUtil.reverse(dmCombined[0]))
				indicators.put("di-", ArrayUtil.reverse(dmCombined[1]))
            }else if("iaroon".equalsIgnoreCase(obj)){
				indicators.put("iaroon-up", ArrayUtil.reverse(Aroon.aroonUp(ArrayUtil.reverse(prices),25)))
				indicators.put("iaroon-down", ArrayUtil.reverse(Aroon.aroonDown(ArrayUtil.reverse(prices),25)))
				//indicators.put("iaroon-high", ArrayUtil.reverse(MathAnalysis.getElapsedExtrema(ArrayUtil.reverse(prices),25,1)))
				//indicators.put("iaroon-low", ArrayUtil.reverse(MathAnalysis.getElapsedExtrema(ArrayUtil.reverse(prices),25,-1)))
				indicators.put("iaroon-signal", ArrayUtil.reverse(Aroon.aroonSignal(ArrayUtil.reverse(prices),25,50,-50)))
				indicators.put("iaroon-oscillator", ArrayUtil.reverse(Aroon.aroonOscillator(ArrayUtil.reverse(prices),25)))

			}else if("isoo".equalsIgnoreCase(obj)){

				double[] oscillator = SOO.stochasticOscillator(0, ArrayUtil.reverse(prices),ArrayUtil.reverse(prices),
						ArrayUtil
						.reverse(prices), 14,3)
				//indicators.put("isoo", ArrayUtil.reverse(oscillator))

				short[] overBOverS = SOO.overBOverS(oscillator, 80, 20)
				indicators.put("isooOver", ArrayUtil.reverse(overBOverS))
				indicators.put("isooOverContinous", ArrayUtil.reverse(SOO.overBOverSContinous(overBOverS)))

			}
		}
		result.put("i",indicators);
		result.put("o",overlays);
		return result;
	}


}
