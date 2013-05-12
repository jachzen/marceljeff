package au.com.normalengineering.jnetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Selection {

    public static ArrayList<Genome> rouletteWheel(ArrayList<Genome> genomes) {
        double totalFitness = 0.0;
        for (Genome genome : genomes) {
            totalFitness += genome.getFitness();
        }
        ArrayList<Double> randomCumulativeFitnesses = new ArrayList<Double>(genomes.size());
        for (int i = 0; i < genomes.size(); i++) {
            randomCumulativeFitnesses.add(Random.getInstance().nextDouble() * totalFitness);
        }
        Collections.sort(randomCumulativeFitnesses);
        return rouletteWheelSelectionHelper(genomes, randomCumulativeFitnesses);
    }

    public static ArrayList<Genome> stochasticUniversalSampling(ArrayList<Genome> genomes) {
        double totalFitness = 0.0;
        for (Genome genome : genomes) {
            totalFitness += genome.getFitness();
        }
        double meanFitness = totalFitness / genomes.size();
        double start = Random.getInstance().nextDouble() * meanFitness;
        ArrayList<Double> regularCumulativeFitnesses = new ArrayList<Double>(genomes.size());
        for (int i = 0; i < genomes.size(); i++) {
            regularCumulativeFitnesses.add(start + i * meanFitness);
        }
        return rouletteWheelSelectionHelper(genomes, regularCumulativeFitnesses);
    }

    /* cumulativeFitnesses must be sorted */
    private static ArrayList<Genome> rouletteWheelSelectionHelper(ArrayList<Genome> genomes,
        ArrayList<Double> cumulativeFitnesses) {
        Collections.sort(genomes);
        ArrayList<Genome> newGenomes = new ArrayList<Genome>();
        Iterator<Genome> iterator = genomes.iterator();
        Genome genome = iterator.next();
        double totalFitness = genome.getFitness();
        for (Double cumulativeFitness : cumulativeFitnesses) {
            while (totalFitness < cumulativeFitness) {
                genome = iterator.next();
                totalFitness += genome.getFitness();
            }
            newGenomes.add(genome);
        }
        return newGenomes;
    }
}
