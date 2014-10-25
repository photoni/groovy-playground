package ta

import groovy.util.logging.Slf4j
import org.junit.Test

/**
 *
 * groovy-playground
 * Created by filippo on 25/10/14.
 */
@Slf4j
class MathAnalysisTest {
    @Test
    def void getElapsedMaxTest(){
        def highs=[1,2,3,4,5,6,7,6,6,5,4,3,13,14,15,16,17,16,15,11,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37] as double[]
        int[]results=MathAnalysis.getElapsedTrend(highs, 25, 1);
        log.debug('xElapsedMax');
        assert results[0]==0
        assert results[1]==0
        assert results[2]==0
        assert results[3]==0
        assert results[4]==0
        assert results[5]==0
        assert results[6]==0
        assert results[7]==1
        assert results[8]==2
        assert results[9]==3
        assert results[10]==4
        assert results[11]==5
        assert results[12]==0
        assert results[13]==0
        assert results[14]==0
        assert results[15]==0
        assert results[16]==0
        assert results[17]==1
    }

    @Test
    def void getElapsedMinTest(){
        def lows=[1,1,2,3,3,3,3,4,5,6,6,7,8,9,10,9,9,10,9,13,14,15,16,17,18,19,20,21,21,22,23,24,25,25,26,27,28] as double[]
        int[]results=MathAnalysis.getElapsedTrend(lows, 25, -1);
        log.debug('xElapsedMin');
        assert results[0]==0
        assert results[1]==1
        assert results[2]==2
        assert results[3]==3
        assert results[4]==4
        assert results[5]==5
        assert results[6]==6
        assert results[7]==7
        assert results[8]==8
        assert results[9]==9
        assert results[10]==10
        assert results[11]==11
        assert results[12]==12
        assert results[13]==13
        assert results[14]==14
        assert results[15]==0
        assert results[16]==1
        assert results[17]==2
        assert results[18]==0
    }
}
