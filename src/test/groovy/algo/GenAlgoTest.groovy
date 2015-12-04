package algo

import groovy.util.logging.Log
import groovy.util.logging.Slf4j
import org.apache.commons.math3.genetics.AbstractListChromosome
import org.apache.commons.math3.genetics.BinaryChromosome
import org.apache.commons.math3.genetics.Chromosome
import org.apache.commons.math3.genetics.ElitisticListPopulation
import org.apache.commons.math3.genetics.FixedGenerationCount
import org.apache.commons.math3.genetics.GeneticAlgorithm
import org.apache.commons.math3.genetics.InvalidRepresentationException
import org.apache.commons.math3.genetics.ListPopulation
import org.apache.commons.math3.genetics.OnePointCrossover
import org.apache.commons.math3.genetics.Population
import org.apache.commons.math3.genetics.RandomKey
import org.apache.commons.math3.genetics.RandomKeyMutation
import org.apache.commons.math3.genetics.StoppingCondition
import org.apache.commons.math3.genetics.TournamentSelection
import org.junit.Test

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
        BinaryChromosome chrome1 = new DummyBinaryChromosome([0, 0, 0, 0, 0,0,1]);
        BinaryChromosome chrome2 = new DummyBinaryChromosome([1, 0, 0, 0, 0,0,1]);



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
