package ta

import groovy.lang.Singleton;
import groovy.util.logging.Slf4j;


@Slf4j
@Singleton
class TechnicalAnalisys {

	static final int PERIODS = 20






	Map<String,double[]> multi(double[] prices,double[] highs,double[] lows,List<String> types, boolean cutOut){
		Map<String,double[]> result= new HashMap<String,double[]>()
		types.each {obj ->
			if("isma".equalsIgnoreCase(obj))
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
			}else if("imacd".equalsIgnoreCase(obj)){
				def macd = Indicators.macd(0,prices.length,prices)
				result.put("macd-line", macd[0])
				result.put("macd-signal", macd[1])
				result.put("macd-histogram", macd[2])
			}else if("iatr".equalsIgnoreCase(obj))
				result.put("iatr", Indicators.atr(0,prices.length,highs,lows, 14))
            else if("adx".equalsIgnoreCase(obj)){
                result.put("adx", ADX.adx(0, prices.length, highs, lows,14))
                double[][] dmCombined=ADX.dm(0, setup['prices'].length, setup['highs'], setup['lows'])
                result.put("di+", dmCombined[0])
                result.put("di-", dmCombined[1])
            }
		}
		return result;
	}


}
