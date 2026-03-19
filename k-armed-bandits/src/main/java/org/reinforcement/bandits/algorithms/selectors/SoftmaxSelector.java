package org.reinforcement.bandits.algorithms.selectors;

import org.reinforcement.bandits.algorithms.ActionSelectionContext;
import org.reinforcement.bandits.algorithms.IActionSelector;

import java.util.Random;

/**
 * Softmax (Boltzmann) action selection using preference values.
 * Used by gradient bandit algorithms. Selects actions with probability
 * proportional to exp(H(a)) where H(a) is the preference.
 */
public class SoftmaxSelector implements IActionSelector {
    private final Random random;
    private final double[] preferences;

    /**
     * Create a softmax selector with preference array.
     *
     * @param preferences the preference values for each action
     * @param random random number generator
     */
    public SoftmaxSelector(double[] preferences, Random random) {
        this.preferences = preferences;
        this.random = random;
    }

    @Override
    public int selectAction(ActionSelectionContext context) {
        return selectWithSoftmax(preferences);
    }

    private int selectWithSoftmax(double[] h) {
        int n = h.length;
        double[] probabilities = new double[n];

        // Calculate softmax probabilities
        double sumExp = 0.0;
        for (int i = 0; i < n; i++) {
            probabilities[i] = Math.exp(h[i]);
            sumExp += probabilities[i];
        }

        for (int i = 0; i < n; i++) {
            probabilities[i] /= sumExp;
        }

        // Sample from the probability distribution
        double[] cumulativeProbs = new double[n];
        cumulativeProbs[0] = probabilities[0];
        for (int i = 1; i < n; i++) {
            cumulativeProbs[i] = cumulativeProbs[i - 1] + probabilities[i];
        }

        double randomValue = random.nextDouble();
        for (int i = 0; i < n; i++) {
            if (randomValue <= cumulativeProbs[i]) {
                return i;
            }
        }

        return n - 1; // fallback (shouldn't reach here)
    }
}