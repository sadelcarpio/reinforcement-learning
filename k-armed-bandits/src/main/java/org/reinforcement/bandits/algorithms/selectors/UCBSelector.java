package org.reinforcement.bandits.algorithms.selectors;

import org.reinforcement.bandits.algorithms.ActionSelectionContext;
import org.reinforcement.bandits.algorithms.IActionSelector;

import java.util.Random;

/**
 * Upper Confidence Bound (UCB) action selection strategy.
 * Selects actions based on: UCB(a) = Q(a) + c * sqrt(ln(t) / N(a))
 * Balances exploration and exploitation by favoring less-tried actions.
 */
public class UCBSelector implements IActionSelector {
    private final double c;
    private final Random random;

    /**
     * Create a UCB selector.
     *
     * @param c the exploration parameter (typically ~2)
     * @param random random number generator for tie-breaking
     */
    public UCBSelector(double c, Random random) {
        if (c < 0) {
            throw new IllegalArgumentException("Exploration parameter c must be non-negative");
        }
        this.c = c;
        this.random = random;
    }

    @Override
    public int selectAction(ActionSelectionContext context) {
        int numActions = context.getNumActions();
        double[] ucbValues = new double[numActions];

        // First, select any action that has never been tried
        for (int i = 0; i < numActions; i++) {
            if (context.getActionCount(i) == 0) {
                return i;
            }
        }

        // Calculate UCB values
        int totalSteps = context.getTotalSteps();
        for (int i = 0; i < numActions; i++) {
            int count = context.getActionCount(i);
            double uncertainty = c * Math.sqrt(Math.log(totalSteps) / count);
            ucbValues[i] = context.getQValue(i) + uncertainty;
        }

        // Select action with highest UCB value (break ties randomly)
        return selectMaxIndex(ucbValues);
    }

    private int selectMaxIndex(double[] values) {
        double maxValue = values[0];
        int countMax = 1;
        int firstMaxIndex = 0;

        for (int i = 1; i < values.length; i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
                countMax = 1;
                firstMaxIndex = i;
            } else if (values[i] == maxValue) {
                countMax++;
            }
        }

        if (countMax == 1) {
            return firstMaxIndex;
        }

        // Randomly select among tied actions
        int[] maxIndices = new int[countMax];
        int idx = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == maxValue) {
                maxIndices[idx++] = i;
            }
        }

        return maxIndices[random.nextInt(countMax)];
    }
}