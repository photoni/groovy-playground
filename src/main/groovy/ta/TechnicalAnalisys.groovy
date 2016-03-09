package ta

import groovy.util.logging.Slf4j
import util.ArrayUtil;


@Slf4j
@Singleton
class TechnicalAnalisys {

    static final int PERIODS = 20

    Map<String, Map> multi(double[] prices, double[] highs, double[] lows, List<String> types, boolean cutOut) {
        Map<String, double[]> overlays = new HashMap<String, double[]>()
        Map<String, double[]> indicators = new HashMap<String, double[]>()
        Map<String, double[]> markers = new HashMap<String, double[]>()
        Map<String, Map> result = new HashMap<String, Map>()
        types.each { obj ->
            if ("isma".equalsIgnoreCase(obj)) {
                def sma = MA.sma(0, prices.length, ArrayUtil.reverse(prices), 20)
                overlays.put("isma", ArrayUtil.reverse(sma))
            } else if ("iema".equalsIgnoreCase(obj)) {
                def ema = MA.ema(0, prices.length, ArrayUtil.reverse(prices), 20)
                overlays.put("iema", ArrayUtil.reverse(ema))
            } else if ("ikama".equalsIgnoreCase(obj)) {
                def kama2 = KAMA.kama(ArrayUtil.reverse(prices),30,2,30);
                overlays.put("ikama2", ArrayUtil.reverse(kama2))
                def kama5 = KAMA.kama(ArrayUtil.reverse(prices),30,5,30);
                overlays.put("ikama5", ArrayUtil.reverse(kama5))
                def ikama5Slope = MathAnalysis.slope(kama5,15)
                indicators.put("ikama5Slope", ArrayUtil.reverse(ikama5Slope))
                def ikama5Trend = KAMA.trend(ArrayUtil.reverse(prices),30,5,30,20,0.03)
                indicators.put("ikama5Trend", ArrayUtil.reverse(ikama5Trend))
                def ikama2Slope = MathAnalysis.slope(kama2,5)
                indicators.put("ikama2Slope", ArrayUtil.reverse(ikama2Slope))
                def ikama2Trend = KAMA.trend(ArrayUtil.reverse(prices),30,2,30,5,0.03)
                indicators.put("ikama2Trend", ArrayUtil.reverse(ikama2Trend))

                def hypertrend = KAMA.hypertrend(ikama2Slope,0.5)


                def ikamaConvergence=MathAnalysis.convergence(ikama5Trend,ikama2Trend)
                def ikamaConvergenceHyper=MathAnalysis.convergence(ikama5Trend,ikama2Trend,hypertrend)

                //  def ikamaConvergence=MathAnalysis.convergence(ikama5Trend,ikama2Trend)
                indicators.put("ikamaConv", ArrayUtil.reverse(ikamaConvergence))
                indicators.put("ikamaConvHyper", ArrayUtil.reverse(ikamaConvergenceHyper))


            } else if ("iboll".equalsIgnoreCase(obj)) {
                def bollingerBands = Indicators.bollingerBands(0, prices.length, prices, 20)
                result.put("iboll-lower", bollingerBands[0])
                result.put("iboll-middle", bollingerBands[1])
                result.put("iboll-higher", bollingerBands[2])
            } else if ("imacd".equalsIgnoreCase(obj)) {
                def macd = MA.macd(0, prices.length, ArrayUtil.reverse(prices), 12, 36,9)
                indicators.put("macd-line", ArrayUtil.reverse(macd[0]))
                indicators.put("macd-signal", ArrayUtil.reverse(macd[1]))
                indicators.put("macd-histogram", ArrayUtil.reverse(macd[2]))
                indicators.put("centerLineCross", ArrayUtil.reverse(macd[5]))
                indicators.put("signalLineCross", ArrayUtil.reverse(macd[6]))
                indicators.put("compoundLineCross", ArrayUtil.reverse(macd[7]))
                overlays.put("macd-ema12", ArrayUtil.reverse(macd[3]))
                overlays.put("macd-ema26", ArrayUtil.reverse(macd[4]))
            } else if ("iatr".equalsIgnoreCase(obj))
                result.put("iatr", Indicators.atr(0, prices.length, highs, lows, 14))
            else if ("iadx".equalsIgnoreCase(obj)) {
                indicators.put("adx", ArrayUtil.reverse(ADX.adx(0, prices.length, ArrayUtil.reverse(highs), ArrayUtil
                        .reverse(lows), 14)))
                double[][] dmCombined = ADX.dm(0, prices.length, ArrayUtil.reverse(highs), ArrayUtil.reverse(lows))
                //indicators.put("di+", ArrayUtil.reverse(dmCombined[0]))
                //indicators.put("di-", ArrayUtil.reverse(dmCombined[1]))
            } else if ("iaroon".equalsIgnoreCase(obj)) {
                indicators.put("iaroon-up", ArrayUtil.reverse(AROON.aroonUp(ArrayUtil.reverse(prices), 25)))
                indicators.put("iaroon-down", ArrayUtil.reverse(AROON.aroonDown(ArrayUtil.reverse(prices), 25)))
                //indicators.put("iaroon-high", ArrayUtil.reverse(MathAnalysis.getElapsedExtrema(ArrayUtil.reverse
				// (prices),25,1)))
                //indicators.put("iaroon-low", ArrayUtil.reverse(MathAnalysis.getElapsedExtrema(ArrayUtil.reverse
				// (prices),25,-1)))
                indicators.put("iaroon-signal", ArrayUtil.reverse(AROON.aroonSignal(ArrayUtil.reverse(prices), 25,
						50, -50)))
                indicators.put("iaroon-compound-signal", ArrayUtil.reverse(AROON.aroonCompoundSignal(ArrayUtil.reverse(prices),
                        25, 50, -50)))
                indicators.put("iaroon-oscillator", ArrayUtil.reverse(AROON.aroonOscillator(ArrayUtil.reverse(prices)
						, 25)))

            } else if ("isoo".equalsIgnoreCase(obj)) {

                double[][] oscillator = SOO.stochasticOscillator(0, prices, 14, 3)
                /*double[] hh= ArrayHelper.closureIterator(0, prices, 14){int start,double [] values,
                                                                                         int prds ->
                    return MathAnalysis.highestHigh(start,values,prds)
                }
                double[] ll=ArrayHelper.closureIterator(0,prices,14){int start,double [] values,
                                                                                        int prds ->
                    return MathAnalysis.lowestLow(start,values,prds)
                }*/
                //indicators.put("isoo", ArrayUtil.reverse(oscillator))

                short[] overBOverS = SOO.overBOverS(oscillator[3], 80, 20,1)
                //indicators.put("isoo", ArrayUtil.reverse(oscillator[0]))
                //indicators.put("isooma", ArrayUtil.reverse(oscillator[3]))
                indicators.put("isooOver", ArrayUtil.reverse(overBOverS))
                indicators.put("isooOverContinous", ArrayUtil.reverse(SOO.overBOverSContinous(overBOverS)))
                overlays.put("hh", oscillator[1])
                overlays.put("ll", oscillator[2])

            } else if ("irsi".equalsIgnoreCase(obj)) {

                double[] rsi = RSI.rsi(prices, 14)
                double[] overBOverS = RSI.overBOverS(rsi, 70, 30)
                //indicators.put("irsi", rsi)
                indicators.put("irsiOverBOverS", overBOverS)


            } else if ("iroc".equalsIgnoreCase(obj)) {
                //13 21 34 55 89 144 233
                double[] roc13 = ROC.roc(prices, 13)
                double[] roc21 = ROC.roc(prices, 21)
                //double[] roc34 = ROC.roc(prices, 34)
                //double[] roc100 = ROC.roc(prices, 100)
                double[] roc150 = ROC.roc(prices, 150)
                double[] roc200 = ROC.roc(prices, 200)
                double[] roc250 = ROC.roc(prices, 250)
                double[] rocComposite=ROC.composite(roc13,roc21,roc150,roc200,roc250)
                double[] rocCompositeSmooth=ArrayUtil.reverse(MA.sma(0,rocComposite.length,ArrayUtil
                        .reverse(rocComposite),7))
                double[] rocCompositeSignal=ROC.compositeSignal(rocCompositeSmooth,10)
                indicators.put("iroc13", roc13)
                indicators.put("iroc21", roc21)
                //indicators.put("iroc34", MA.sma(0,roc34.length,roc34,3))
                //indicators.put("iroc34", roc34)
                indicators.put("iroc120", roc150)
                indicators.put("iroc200", roc200)
                indicators.put("iroc233", roc250)
                indicators.put("irocComposite", rocCompositeSmooth)
                indicators.put("irocCompositeSignal", rocCompositeSignal )
            } else if ("izigzag".equalsIgnoreCase(obj)) {
                double[] zigzag7 = ZIGZAG.zigZag(ArrayUtil.reverse(prices), 10)
                double[] zigzag10 = ZIGZAG.zigZag(ArrayUtil.reverse(prices), 15)
                //overlays.put("izigzag7", ArrayUtil.reverse(zigzag7))
                overlays.put("izigzag10", ArrayUtil.reverse(zigzag10))
            } else if ("iretracement".equalsIgnoreCase(obj)) {

                double[] zigzag10 = ZIGZAG.zigZag(ArrayUtil.reverse(prices), 7)
                overlays.put("izigzag10", ArrayUtil.reverse(zigzag10))
                RETRACEMENT retr = new RETRACEMENT();
                double[] retracement = retr.fibonacci(ArrayUtil.reverse(prices), 7)
                markers.put("iretracement", ArrayUtil.reverse(retracement))
                double[] retracementInd = retr.fibonacciIndicator(ArrayUtil.reverse(prices), 7)
                indicators.put("iretracementInd", ArrayUtil.reverse(retracementInd))
                double[] retracementSignal = retr.fibonacciSignal(ArrayUtil.reverse(prices), 7,1)
                indicators.put("iretracementSignal", ArrayUtil.reverse(retracementSignal))
            }
        }
        result.put("i", indicators);
        result.put("o", overlays);
        result.put("m", markers);
        return result;
    }


}
