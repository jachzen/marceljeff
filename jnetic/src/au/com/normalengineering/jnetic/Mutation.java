package au.com.normalengineering.jnetic;

import java.util.ArrayList;

public class Mutation {

    public static ArrayList<Genome> bitInversion(ArrayList<Genome> genomes, double mutationRate) {
        ArrayList<Genome> newGenomes = new ArrayList<Genome>();
        for (Genome genome : genomes) {
            for (int bit = 0; bit < genome.getNumBases(); bit++) {
                if (Random.getInstance().nextDouble() <= mutationRate) {
                    genome = genome.getMutatedInstance(bit);
                }
            }
            newGenomes.add(genome);
        }
        return newGenomes;
    }
}
