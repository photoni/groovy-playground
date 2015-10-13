package ta

import groovy.util.logging.Slf4j
import util.ArrayUtil;


@Slf4j
@Singleton
class TechnicalAnalisys {

	static final int PERIODS = 20

	Map<String,Map> multi(double[] prices,double[] highs,double[] lows,List<String> types, boolean cutOut){
		Map<String,double[]> overlays= new HashMap<String,double[]>()
		Map<String,double[]> indicators= new HashMap<String,double[]>()
		Map<String,double[]> markers= new HashMap<String,double[]>()
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
				//indicators.put("di+", ArrayUtil.reverse(dmCombined[0]))
				//indicators.put("di-", ArrayUtil.reverse(dmCombined[1]))
            }else if("iaroon".equalsIgnoreCase(obj)){
				indicators.put("iaroon-up", ArrayUtil.reverse(AROON.aroonUp(ArrayUtil.reverse(prices),25)))
				indicators.put("iaroon-down", ArrayUtil.reverse(AROON.aroonDown(ArrayUtil.reverse(prices),25)))
				//indicators.put("iaroon-high", ArrayUtil.reverse(MathAnalysis.getElapsedExtrema(ArrayUtil.reverse(prices),25,1)))
				//indicators.put("iaroon-low", ArrayUtil.reverse(MathAnalysis.getElapsedExtrema(ArrayUtil.reverse(prices),25,-1)))
				indicators.put("iaroon-signal", ArrayUtil.reverse(AROON.aroonSignal(ArrayUtil.reverse(prices),25,50,-50)))
				indicators.put("iaroon-oscillator", ArrayUtil.reverse(AROON.aroonOscillator(ArrayUtil.reverse(prices),25)))

			}else if("isoo".equalsIgnoreCase(obj)){

				double[][] oscillator = SOO.stochasticOscillator(0,	prices, 14,3)
				/*double[] hh= ArrayHelper.closureIterator(0, prices, 14){int start,double [] values,
																						 int prds ->
					return MathAnalysis.highestHigh(start,values,prds)
				}
				double[] ll=ArrayHelper.closureIterator(0,prices,14){int start,double [] values,
																						int prds ->
					return MathAnalysis.lowestLow(start,values,prds)
				}*/
				//indicators.put("isoo", ArrayUtil.reverse(oscillator))

				short[] overBOverS = SOO.overBOverS(oscillator[3], 80, 20)
				//indicators.put("isoo", ArrayUtil.reverse(oscillator[0]))
				//indicators.put("isooma", ArrayUtil.reverse(oscillator[3]))
				indicators.put("isooOver", ArrayUtil.reverse(overBOverS))
				indicators.put("isooOverContinous", ArrayUtil.reverse(SOO.overBOverSContinous(overBOverS)))
				overlays.put("hh", oscillator[1])
				overlays.put("ll", oscillator[2])

			}else if("irsi".equalsIgnoreCase(obj)){

				double[] rsi=RSI.rsi(prices,14)
				double[] overBOverS= RSI.overBOverS(rsi,70,30)
				//indicators.put("irsi", rsi)
				indicators.put("irsiOverBOverS", overBOverS)


			}else if("iroc".equalsIgnoreCase(obj)){
				double[] roc21=ROC.roc(prices,21)
				double[] roc63=ROC.roc(prices,63)
				double[] roc125=ROC.roc(prices,125)
				double[] roc250=ROC.roc(prices,250)
				indicators.put("iroc21", roc21)
				//indicators.put("iroc63", roc63)
				//indicators.put("iroc125", roc125)
				indicators.put("iroc250", roc250)
			}else if("izigzag".equalsIgnoreCase(obj)){
				double[] zigzag7=ZIGZAG.zigZag(ArrayUtil.reverse(prices),7)
				double[] zigzag10=ZIGZAG.zigZag(ArrayUtil.reverse(prices),10)
				//overlays.put("izigzag7", ArrayUtil.reverse(zigzag7))
				overlays.put("izigzag10", ArrayUtil.reverse(zigzag10))
			}else if("iretracement".equalsIgnoreCase(obj)){

				double[] zigzag10=ZIGZAG.zigZag(ArrayUtil.reverse(prices),10)
				overlays.put("izigzag10", ArrayUtil.reverse(zigzag10))
				RETRACEMENT retr= new RETRACEMENT();
				double[] retracement10=retr.fibonacci(ArrayUtil.reverse(prices),10)
				markers.put("iretracement",ArrayUtil.reverse(retracement10))
			}
		}
		result.put("i",indicators);
		result.put("o",overlays);
		result.put("m",markers);
		return result;
	}


}
