package org.reinforcement.bandits.algorithms.selectors;

import org.reinforcement.bandits.algorithms.ActionSelectionContext;
import org.reinforcement.bandits.algorithms.IActionSelector;

import java.util.Random;

/**
 * Epsilon-greedy action selection strategy.
 * With probability epsilon, selects a random action (exploration).
 * With probability 1-epsilon, selects the greedy action (exploitation).
 */
public class EpsilonGreedySelector implements IActionSelector {
    private final double epsilon;
    private final Random random;

    /**
     * Create an epsilon-greedy selector.
     *
     * @param epsilon the exploration probability, must be in [0, 1]
     * @param random the random number generator
     */
    public EpsilonGreedySelector(double epsilon, Random random) {
        if (epsilon < 0 || epsilon > 1) {
            throw new IllegalArgumentException("Epsilon must be in [0, 1]");
        }
        this.epsilon = epsilon;
        this.random = random;
    }

    @Override
    public int selectAction(ActionSelectionContext context) {
        if (random.nextDouble() < epsilon) {
            // Explore: select random action
            return random.nextInt(context.getNumActions());
        } else {
            // Exploit: select greedy action
            return context.getGreedyAction();
        }
    }
}