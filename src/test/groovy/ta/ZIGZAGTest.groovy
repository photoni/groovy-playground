package ta

import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import model.Security
import org.junit.Test
import service.SecurityService

/**
 * groovy-playground
 * Created by filippo on 31/08/15.
 */
@Slf4j
class ZIGZAGTest {

    @Test
    def void zigZagTest(){
        double[] values=[100,105,106,105,105,109,111,115,111,110,109,105,104,101,102,105,107,107,107,108,110,111,114,115,
                    114,116,114,117,116,112,111,112,111,109,106,104,103]

        double[] result=ZIGZAG.zigZag(values,10)
        ArrayHelper.log(result,log,true)
        assertIsValidZigZag(result)
    }

    def void assertIsValidZigZag(double[] result) {
        assert result[0] == 100.0
        assert result[1] == 102.14285714285714
        assert result[2] == 104.28571428571429
        assert result[3] == 106.42857142857143
        assert result[4] == 108.57142857142857
        assert result[5] == 110.71428571428571
        assert result[6] == 112.85714285714286
        assert result[7] == 115.0
        assert result[8] == 112.66666666666667
        assert result[9] == 110.33333333333333
        assert result[10] == 108.0
        assert result[11] == 105.66666666666667
        assert result[12] == 103.33333333333333
        assert result[13] == 101.0
        assert result[14] == 102.14285714285714
        assert result[15] == 103.28571428571429
        assert result[16] == 104.42857142857143
        assert result[17] == 105.57142857142857
        assert result[18] == 106.71428571428571
        assert result[19] == 107.85714285714286
        assert result[20] == 109.0
        assert result[21] == 110.14285714285714
        assert result[22] == 111.28571428571428
        assert result[23] == 112.42857142857143
        assert result[24] == 113.57142857142857
        assert result[25] == 114.71428571428571
        assert result[26] == 115.85714285714286
        assert result[27] == 117.0
        assert result[28] == 115.44444444444444
        assert result[29] == 113.88888888888889
        assert result[30] == 112.33333333333333
        assert result[31] == 110.77777777777777
        assert result[32] == 109.22222222222223
        assert result[33] == 107.66666666666667
        assert result[34] == 106.11111111111111
        assert result[35] == 104.55555555555556
        assert result[36] == 103.0
    }

}
