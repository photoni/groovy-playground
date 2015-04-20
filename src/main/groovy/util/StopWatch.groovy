package util

/**
 * groovy-playground
 * Created by filippo on 20/12/14.
 */
class StopWatch {
    StopWatch(units) {
        this.units = units
    }
    private units
    private timeRecoreds = [:].withDefault { 0.0 }

    private long getTime(){
        if(units=='millisecond')
            return System.currentTimeMillis()
        else if(units=='nanosecond')
            return System.nanoTime()
    }

    def withTimeRecording(keyword, clos) {
        def beginTime = getTime()
        try {
            return clos.call()
        } finally {
            timeRecoreds[keyword] += getTime() - beginTime
        }
    }

    def printResult() {
        timeRecoreds.each { keyword, time ->
            println "timeResult: ${keyword}: ${time} ${units}"
        }
    }
}
