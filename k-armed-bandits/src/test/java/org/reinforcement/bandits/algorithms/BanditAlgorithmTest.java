package org.reinforcement.bandits.algorithms;

import org.junit.Test;
import org.reinforcement.bandits.algorithms.selectors.EpsilonGreedySelector;
import org.reinforcement.bandits.algorithms.updaters.SampleAverageUpdater;

import java.util.Random;
import static org.junit.Assert.*;

public class BanditAlgorithmTest {

    @Test
    public void testAlgorithmInitialization() {
        Random random = new Random();
        BanditAlgorithm algorithm = new BanditAlgorithm(
            5, 0.0,
            new EpsilonGreedySelector(0.1, random),
            new SampleAverageUpdater()
        );

        double[] qValues = algorithm.getQValues();
        assertEquals(5, qValues.length);
        for (double q : qValues) {
            assertEquals(0.0, q, 0.0001);
        }
    }

    @Test
    public void testQValueUpdate() {
        Random random = new Random(42);
        BanditAlgorithm algorithm = new BanditAlgorithm(
            3, 0.0,
            new EpsilonGreedySelector(0.0, random), // Pure greedy
            new SampleAverageUpdater()
        );

        // Update action 1 with reward 5.0
        algorithm.update(1, 5.0);

        double[] qValues = algorithm.getQValues();
        assertEquals(0.0, qValues[0], 0.0001);
        assertEquals(5.0, qValues[1], 0.0001);
        assertEquals(0.0, qValues[2], 0.0001);
    }

    @Test
    public void testGreedyActionSelection() {
        Random random = new Random(42);
        BanditAlgorithm algorithm = new BanditAlgorithm(
            3, 0.0,
            new EpsilonGreedySelector(0.0, random), // Pure greedy
            new SampleAverageUpdater()
        );

        // Make action 2 have highest Q-value
        algorithm.update(2, 10.0);
        algorithm.update(0, 1.0);
        algorithm.update(1, 2.0);

        // Greedy selection should choose action 2
        for (int i = 0; i < 10; i++) {
            assertEquals(2, algorithm.selectAction());
        }
    }

    @Test
    public void testActionCounts() {
        Random random = new Random();
        BanditAlgorithm algorithm = new BanditAlgorithm(
            3, 0.0,
            new EpsilonGreedySelector(0.1, random),
            new SampleAverageUpdater()
        );

        algorithm.update(0, 1.0);
        algorithm.update(1, 2.0);
        algorithm.update(0, 3.0);

        int[] counts = algorithm.getActionCounts();
        assertEquals(2, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);
    }
}