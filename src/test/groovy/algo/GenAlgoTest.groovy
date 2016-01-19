package algo

import groovy.util.logging.Slf4j
import org.apache.commons.math3.genetics.AbstractListChromosome
import org.apache.commons.math3.genetics.BinaryChromosome
import org.apache.commons.math3.genetics.BinaryMutation
import org.apache.commons.math3.genetics.Chromosome
import org.apache.commons.math3.genetics.ElitisticListPopulation
import org.apache.commons.math3.genetics.FixedGenerationCount
import org.apache.commons.math3.genetics.GeneticAlgorithm
import org.apache.commons.math3.genetics.InvalidRepresentationException
import org.apache.commons.math3.genetics.OnePointCrossover
import org.apache.commons.math3.genetics.Population
import org.apache.commons.math3.genetics.RandomKey
import org.apache.commons.math3.genetics.RandomKeyMutation
import org.apache.commons.math3.genetics.StoppingCondition
import org.apache.commons.math3.genetics.TournamentSelection
import org.junit.Test
import ta.AROON
import ta.ER
import ta.KAMA
import ta.MA
import ta.MathAnalysis
import ta.RETRACEMENT
import ta.SOO
import util.ArrayUtil

@Slf4j
class GenAlgoTest {

    private static int counter = 0;

    @Test
    public void induceperm(){
        List<String> origData = Arrays.asList(['a', 'b', 'c', 'd', 'e','f','g'] as String[]);

        List<String> permutedData1 = Arrays.asList(['d', 'b', 'c', 'a', 'e','f','g'] as String[]);
        List<String> permutedData2 = Arrays.asList(['a', 'b',  'd','c', 'e','f','g'] as String[]);
        List<String> permutedData3 = Arrays.asList(['b','a','c', 'd', 'e','f','g'] as String[]);
        log.debug("perm: {}",RandomKey.inducedPermutation(origData, permutedData1))
        log.debug("perm: {}",RandomKey.inducedPermutation(origData, permutedData2))
        log.debug("perm: {}",RandomKey.inducedPermutation(origData, permutedData3))


    }

    @Test
    public void evolveBinaryCromosome(){
        // initialize a new genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(
                new OnePointCrossover<Integer>(),
                1,
                new BinaryMutation(),
                0.10,
                new TournamentSelection(2)
        );
        BinaryChromosome chrome1 = new DummyBinaryChromosome([0, 0, 0, 0, 0,0,1]);
        BinaryChromosome chrome2 = new DummyBinaryChromosome([1, 0, 0, 0, 0,0,1]);
        BinaryChromosome chrome3 = new DummyBinaryChromosome([0, 1, 0, 0, 0,0,1]);
        BinaryChromosome chrome4 = new DummyBinaryChromosome([0, 1, 1, 0, 0,0,1]);
        BinaryChromosome chrome5 = new DummyBinaryChromosome([0, 1, 0, 1, 0,0,1]);

        Population initialPop = new ElitisticListPopulation(30, 0.2);
        initialPop.addChromosome(chrome1)
        initialPop.addChromosome(chrome2)
        initialPop.addChromosome(chrome3)
        initialPop.addChromosome(chrome4)
        initialPop.addChromosome(chrome5)


        StoppingCondition stopCond = new FixedGenerationCount(20);

// run the algorithm
        Population finalPopulation = ga.evolve(initialPop, stopCond);

// best chromosome from the final population
        Chromosome bestFinal = finalPopulation.getFittestChromosome();

        log.debug("best final: {}",bestFinal)

    }

    @Test
    public void evolveRandomKey() {
        // initialize a new genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(
                new OnePointCrossover<Integer>(),
                1,
                new RandomKeyMutation(),
                0.10,
                new TournamentSelection(2)
        );

// initial population
        Population initialPop = new ElitisticListPopulation(20, 0.2);

        List<String> origData = Arrays.asList(['a', 'b', 'c', 'd', 'e'] as String[]);

        List<String> permutedData1 = Arrays.asList(['d', 'b', 'c', 'a', 'e'] as String[]);
        List<String> permutedData2 = Arrays.asList(['a', 'b',  'd','c', 'e'] as String[]);
        List<String> permutedData3 = Arrays.asList(['b','a','c', 'd', 'e'] as String[]);

        DummyRandomKey drk1 = new DummyRandomKey(RandomKey.inducedPermutation(origData, permutedData1));
        DummyRandomKey drk2 = new DummyRandomKey(RandomKey.inducedPermutation(origData, permutedData2));
        DummyRandomKey drk3 = new DummyRandomKey(RandomKey.inducedPermutation(origData, permutedData3));


        initialPop.addChromosome(drk1)
        initialPop.addChromosome(drk1)
        initialPop.addChromosome(drk1)

        initialPop.addChromosome(drk2)
        initialPop.addChromosome(drk2)
        initialPop.addChromosome(drk2)

        initialPop.addChromosome(drk3)
        initialPop.addChromosome(drk3)
        initialPop.addChromosome(drk3)


// stopping condition
        StoppingCondition stopCond = new FixedGenerationCount(20);

// run the algorithm
        Population finalPopulation = ga.evolve(initialPop, stopCond);

// best chromosome from the final population
        Chromosome bestFinal = finalPopulation.getFittestChromosome();

    }


}

public class DummyRandomKey extends RandomKey<String> {

    public DummyRandomKey(List<Double> representation) {
        super(representation);
    }

    public DummyRandomKey(Double[] representation) {
        super(representation);
    }

    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
        return new DummyRandomKey(chromosomeRepresentation);
    }

    public double fitness() {
        // unimportant
        return 0;
    }

}

public class DummyBinaryChromosome extends BinaryChromosome{

    DummyBinaryChromosome(List<Integer> representation) throws InvalidRepresentationException {
        super(representation)
    }

    DummyBinaryChromosome(Integer[] representation) throws InvalidRepresentationException {
        super(representation)
    }

    @Override
    AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> chromosomeRepresentation) {
        return new DummyBinaryChromosome(chromosomeRepresentation)
    }

    @Override
    double fitness() {
        int sum=0;
        this.representation.each {
            sum+=it
        }
        return sum
    }
}


/** Model **/

/**
 * Basic interface for all indexed genes. An indexed gene is backed by an indexed structure
 * @param < T >
 */
public interface BinaryIndexedGene<T>{
    public T get(int i);
}

public abstract class BinaryGeneBase implements BinaryIndexedGene<Double>{

    private Double[] sequence;

    BinaryGeneBase(Double[] sequence) {
        this.sequence = evaluate(sequence)
    }

    @Override
    Double get(int i) {
        return sequence[i]
    }

    protected abstract Double[] evaluate(Double[] sequence);
}

public class ERGene extends BinaryGeneBase{
    private int periods=10
    ERGene(Double[] sequence) {
        super(sequence)
    }

    @Override
    protected Double[] evaluate(Double[] sequence) {
        double[] result=ER.er(sequence,periods);
        return result
    }
}

public class KamaGene extends BinaryGeneBase{
    private int periods=10
    KamaGene(Double[] sequence) {
        super(sequence)
    }

    @Override
    protected Double[] evaluate(Double[] sequence) {
        def eRP=30;//Efficiency Ratio periods
        def f5=5;// fastest constant 5 periods
        def f2=2;// fastest constant 2 periods
        def slow=30;// slowest constant 30 periods
        def regression20=20;// regression periods 20
        def regression5=5;// regression periods 5
        def threshold=0.1;//neutral trend boundaries
        def ikama5Trend = KAMA.trend(ArrayUtil.reverse(sequence),eRP,f5,slow,regression20,threshold)

        def ikama2Trend = KAMA.trend(ArrayUtil.reverse(sequence),eRP,f2,slow,regression5,threshold)

        double[] ikamaConvergence=MathAnalysis.convergence(ikama5Trend,ikama2Trend)

        return ikamaConvergence
    }
}

public class SooGene extends BinaryGeneBase{
    def periods=14
    def smooth=3
    def overBThreshold=80
    def overSThreshold=20
    SooGene(Double[] sequence) {
        super(sequence)
    }

    @Override
    protected Double[] evaluate(Double[] sequence) {

        double[][] oscillator = SOO.stochasticOscillator(0, sequence, periods, smooth)
        double[] signal = SOO.signal(oscillator[3], overBThreshold, overSThreshold)

        return signal

    }
}

public class MacdGene extends BinaryGeneBase{
    private int fastEma=12
    private int slowEma=26
    MacdGene(Double[] sequence) {
        super(sequence)
    }

    @Override
    protected Double[] evaluate(Double[] sequence) {

        def macd = MA.macd(0, sequence.length, sequence, fastEma, slowEma)
        return macd[7]

    }
}

public class AroonGene extends BinaryGeneBase{
    private int periods=25
    private int bullish=50
    private int bearish=-50

    AroonGene(Double[] sequence) {
        super(sequence)
    }

    @Override
    protected Double[] evaluate(Double[] sequence) {

        def compoundSignal = AROON.aroonCompoundSignal(sequence, periods, bullish, bearish)
        return compoundSignal

    }
}

public class RetracementGene extends BinaryGeneBase{
    private int zigzag=7
    private int retracementThrashold=1

    RetracementGene(Double[] sequence) {
        super(sequence)
    }

    @Override
    protected Double[] evaluate(Double[] sequence) {

        RETRACEMENT retr = new RETRACEMENT();
        def retracementSignal = retr.fibonacciSignal(ArrayUtil.reverse(sequence), zigzag,retracementThrashold)
        return retracementSignal

    }
}

