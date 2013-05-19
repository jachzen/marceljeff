import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.apache.log4j.Logger;

import au.com.normalengineering.jnetic.Crossover;
import au.com.normalengineering.jnetic.Genome;
import au.com.normalengineering.jnetic.Mutation;
import au.com.normalengineering.jnetic.Selection;

/**
 * Runs the genetic optimization.
 */
public class Optimize {
    private final static Logger logger = Logger.getLogger(Engine.class.getName());

    public static void run() throws Exception {
        logger.info("Starting optimization");

        /* Read in the historical database for which the genome is to be
         * optimized. */
        Database database = new Database();
        for (String file : IConfiguration.DATABASES) {
            logger.info(String.format("Loading database from: \"%s\"...", file));
            int lineCount = database.addDatabaseFile(new File(file));
            logger.info(String.format("...done (%d lines)", lineCount));
        }

        /* Create the population using the genome type which is to be optimized. */
        logger.info(String.format("Creating %d genomes", IConfiguration.POPULATION_SIZE));
        ArrayList<Genome> genomes = new ArrayList<Genome>();
        for (int i = 0; i < IConfiguration.POPULATION_SIZE; i++) {
            genomes.add((Genome) IConfiguration.ALGORITHM_CLASS.getConstructor().newInstance());
        }

        /* Tell the algorithm what exchange to use. */
        logger.info(String.format("Using exchange %s", IConfiguration.EXCHANGE_CLASS.getSimpleName()));
        Method setExchangeFactory = IConfiguration.ALGORITHM_CLASS.getMethod("setExchangeFactory",
            java.lang.Class.class, Database.class);
        setExchangeFactory.invoke(null, IConfiguration.EXCHANGE_CLASS, database);

        /* Run the optimization loop, printing out the fittest genome each
         * generation. */
        logger.info("Starting optimization");
        // mutate 50% of genomes
        final double MUTATION_RATE = Math.pow(0.5, genomes.get(0).getNumBases());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < IConfiguration.MAX_ALLOWED_EVOLUTIONS; i++) {
            Collections.sort(genomes);
            genomes.remove(genomes.size() - 1);
            Genome fittestGenome = genomes.get(0);
            logger.info(String.format("%05d %.0f %s", i, fittestGenome.getFitness(), fittestGenome.toString()));
            genomes = Selection.stochasticUniversalSampling(genomes);
            genomes = Crossover.random(genomes, IConfiguration.CROSSOVER_RATE);
            genomes = Mutation.bitInversion(genomes, MUTATION_RATE);
            genomes.add(fittestGenome);
        }
        long endTime = System.currentTimeMillis();
        logger.info("Total optimization time: " + (endTime - startTime) + " ms");

        /* Store the fittest genome. */
        Collections.sort(genomes);
        Genome fittestGenome = genomes.get(0);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(IConfiguration.FITTEST_GENOME));
        out.writeObject(fittestGenome);
        out.close();
        logger.info("Fittest genome: " + fittestGenome.toString());

        logger.info("done");
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
