package algo

import org.apache.commons.math3.genetics.AbstractListChromosome
import org.apache.commons.math3.genetics.Chromosome
import org.apache.commons.math3.genetics.ElitisticListPopulation
import org.apache.commons.math3.genetics.FixedGenerationCount
import org.apache.commons.math3.genetics.GeneticAlgorithm
import org.apache.commons.math3.genetics.ListPopulation
import org.apache.commons.math3.genetics.OnePointCrossover
import org.apache.commons.math3.genetics.Population
import org.apache.commons.math3.genetics.RandomKey
import org.apache.commons.math3.genetics.RandomKeyMutation
import org.apache.commons.math3.genetics.StoppingCondition
import org.apache.commons.math3.genetics.TournamentSelection
import org.junit.Test

class GenAlgoTest {

    private static int counter = 0;

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
