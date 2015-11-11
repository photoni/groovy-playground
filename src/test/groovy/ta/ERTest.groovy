package ta
import data.TestDataSupport
import groovy.util.logging.Slf4j
import helpers.ArrayHelper
import org.junit.Test
import util.MathUtil

@Slf4j
class ERTest {

    @Test
    public void erTest(){
        double[] result=ER.er(TestDataSupport.KAMA_VALUES,10);

        for (int i = 0; i < result.length; i++) {
            result[i]=MathUtil.nDecimal(result[i],4);
        }
        ArrayHelper.log(result,log,true)
    }

    @Test
    public void scTest(){
        double[] result=ER.sc(TestDataSupport.KAMA_VALUES,10,2,30);
        for (int i = 0; i < result.length; i++) {
            result[i]=MathUtil.nDecimal(result[i],4);
        }
        ArrayHelper.log(result,log,true)
    }

    @Test
    public void changeTest(){
        double[] result=ER.change(TestDataSupport.KAMA_VALUES,10);

        for (int i = 0; i < result.length; i++) {
            result[i]=MathUtil.nDecimal(result[i],2);
        }
        ArrayHelper.log(result,log,true)
    }
    @Test
    public void volatilityTest(){
        double[] result=ER.volatility(TestDataSupport.KAMA_VALUES,10);

        for (int i = 0; i < result.length; i++) {
            result[i]=MathUtil.nDecimal(result[i],2);
        }
        ArrayHelper.log(result,log,true)
    }



}
