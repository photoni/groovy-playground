package algo;

/**
 * Represent a type of gene used to build the chromosome(sequence of gene).
 * A gene type is a type of indicator like macd,aroon...
 *
 * groovy-playground
 * Created by filippo on 20/06/15.
 */
public enum GeneType {macd(1),aroon(2);

    int code;

    GeneType(int code) {
        this.code=code;
    }

    /**
     * The unique code of the indicator
     *
     * @return
     */

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
