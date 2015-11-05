package ta;

public class KAMA {

    public static double[] change(double[] values,int periods){
        double[] result=new double[values.length];
        for (int i = periods; i < values.length; i++) {
            result[i]=changeFormula(values[i],values[i-periods]);
        }
        return result;
    }

    public static double changeFormula(double close,double priorClose){
        return Math.abs(close-priorClose);
    }
}
