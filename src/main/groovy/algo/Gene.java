package algo;

/**
 * groovy-playground
 * Created by filippo on 20/06/15.
 */
public class Gene {
    private GeneType geneType;
    private byte settings;

    public Gene(GeneType geneType, byte settings) {
        this.geneType = geneType;
        this.settings = settings;
    }

    public GeneType getGeneType() {
        return geneType;
    }

    public byte getSettings() {
        return settings;
    }
}
