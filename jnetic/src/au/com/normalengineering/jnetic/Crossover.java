package au.com.normalengineering.jnetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Crossover {

    public static ArrayList<Genome> twoPoint(ArrayList<Genome> genomes, double crossoverRate) {
        ArrayList<Genome> newGenomes = new ArrayList<Genome>();
        Collections.shuffle(genomes, Random.getInstance());
        Iterator<Genome> iterator = genomes.iterator();
        while (iterator.hasNext()) {
            Genome parent1 = iterator.next();
            if (iterator.hasNext()) {
                Genome parent2 = iterator.next();
                if (Random.getInstance().nextDouble() <= crossoverRate) {
                    int firstBit = Random.getInstance().nextInt(parent1.getNumBases());
                    int numBits = Random.getInstance().nextInt(parent1.getNumBases());
                    newGenomes.add(parent1.getCrossedOverInstance(parent2, firstBit, numBits));
                    newGenomes.add(parent2.getCrossedOverInstance(parent1, firstBit, numBits));
                } else {
                    newGenomes.add(parent1);
                    newGenomes.add(parent2);
                }
            } else {
                newGenomes.add(parent1);
            }
        }

        return newGenomes;
    }

    public static ArrayList<Genome> random(ArrayList<Genome> genomes, double crossoverRate) {
        ArrayList<Genome> newGenomes = new ArrayList<Genome>();
        Collections.shuffle(genomes, Random.getInstance());
        Iterator<Genome> iterator = genomes.iterator();
        while (iterator.hasNext()) {
            Genome parent1 = iterator.next();
            if (iterator.hasNext()) {
                Genome parent2 = iterator.next();
                if (Random.getInstance().nextDouble() <= crossoverRate) {
                    ArrayList<Gene> child1Genes = new ArrayList<Gene>();
                    ArrayList<Gene> child2Genes = new ArrayList<Gene>();
                    for (int i = 0; i < parent1.getGenes().size(); i++) {
                        if (Random.getInstance().nextDouble() < 0.5) {
                            child1Genes.add(parent1.getGenes().get(i));
                            child2Genes.add(parent2.getGenes().get(i));
                        } else {
                            child1Genes.add(parent2.getGenes().get(i));
                            child2Genes.add(parent1.getGenes().get(i));
                        }
                    }
                    newGenomes.add(parent1.getInstance(child1Genes));
                    newGenomes.add(parent2.getInstance(child2Genes));
                } else {
                    newGenomes.add(parent1);
                    newGenomes.add(parent2);
                }
            } else {
                newGenomes.add(parent1);
            }
        }

        return newGenomes;
    }
}
