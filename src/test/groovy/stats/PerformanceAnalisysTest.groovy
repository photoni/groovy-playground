package stats

import data.CSV
import groovy.util.logging.Slf4j
import org.apache.commons.lang.ArrayUtils
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.codehaus.groovy.runtime.ArrayUtil
import org.junit.Test
import util.CSVUtil

@Slf4j
class PerformanceAnalisysTest {

    @Test
    public void selectBetterPerfOfSample(){
        def sampleTicker="AAPL"
        CSV csv=new CSV(CSVUtil.entriesFromURI("/var/data/pig/beuteForce${sampleTicker}.csv"));
        List<Map> rows= csv.linesObjects
        def performanceByParams=[:]
        rows.each {
            if (it['tick']!=sampleTicker) {
                def capital= Double.parseDouble(it['capital'])


                    def key=paramsKey(it)
                    def capitals=performanceByParams.get(key)
                    if(capitals==null){
                        capitals=[]
                    }
                    capitals.add(capital)
                    performanceByParams.put(key,capitals)

            }

        }
        def topPerformer="";
        def topPerformerVal=0;
        performanceByParams.entrySet().each {
            def key=it.getKey()
            List values=it.getValue()
            Mean m = new Mean()

            Double[] array = (Double[])values.toArray()
            def pm=m.evaluate(ArrayUtils.toPrimitive(array))
            if(topPerformerVal<pm){
                topPerformer=key
                topPerformerVal=pm

            }
        }
        log.debug("top performer: {} - {}",topPerformer,topPerformerVal)
    }

    def paramsKey(Map it) {
        String key = it['r1'] + "-" + it['r2'] + "-" + it['r3'] + "-" + it['r4'] + "-" + it['r5'] + "-" + it['r6'] +
                "-" + it['rcsp'] + "-" + it['rct']
        key
    }
}
