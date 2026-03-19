package org.reinforcement.bandits.algorithms;

import org.reinforcement.bandits.algorithms.selectors.SoftmaxSelector;

import java.util.Arrays;
import java.util.Random;

/**
 * Gradient Bandit Algorithm that learns action preferences.
 * Uses a softmax distribution over preferences and updates using gradient ascent.
 * This is a special case that doesn't fit the standard Q-learning pattern.
 */
public class GradientBanditAlgorithm {
    private final double stepSize;
    private final Random random;
    private final double[] preferences;
    private final double[] qValues;  // Track average rewards per action
    private final int[] actionCounts;
    private double averageReward;
    private int totalSteps;

    /**
     * Create a gradient bandit algorithm.
     *
     * @param numActions number of actions
     * @param stepSize learning rate (alpha)
     * @param random random number generator
     */
    public GradientBanditAlgorithm(int numActions, double stepSize, Random random) {
        if (numActions <= 0) {
            throw new IllegalArgumentException("Number of actions must be positive");
        }
        if (stepSize <= 0) {
            throw new IllegalArgumentException("Step size must be positive");
        }

        this.stepSize = stepSize;
        this.random = random;
        this.preferences = new double[numActions];
        this.qValues = new double[numActions];
        this.actionCounts = new int[numActions];
        this.averageReward = 0.0;
        this.totalSteps = 0;

        Arrays.fill(preferences, 0.0);
        Arrays.fill(qValues, 0.0);
        Arrays.fill(actionCounts, 0);
    }

    /**
     * Select an action using softmax over preferences.
     *
     * @return the selected action index
     */
    public int selectAction() {
        SoftmaxSelector selector = new SoftmaxSelector(preferences, random);
        ActionSelectionContext context = new ActionSelectionContext(
            qValues, actionCounts, totalSteps
        );
        return selector.selectAction(context);
    }

    /**
     * Update preferences using gradient ascent.
     *
     * @param action the action that was taken
     * @param reward the reward that was received
     */
    public void update(int action, double reward) {
        actionCounts[action]++;
        totalSteps++;

        // Update average reward (used as baseline)
        averageReward = averageReward + (1.0 / totalSteps) * (reward - averageReward);

        // Update action Q-values (for tracking purposes)
        qValues[action] = qValues[action] + (1.0 / actionCounts[action]) * (reward - qValues[action]);

        // Calculate softmax probabilities
        double[] probabilities = calculateSoftmaxProbabilities();

        // Update preferences using gradient ascent
        for (int i = 0; i < preferences.length; i++) {
            if (i == action) {
                preferences[i] += stepSize * (reward - averageReward) * (1 - probabilities[i]);
            } else {
                preferences[i] -= stepSize * (reward - averageReward) * probabilities[i];
            }
        }
    }

    private double[] calculateSoftmaxProbabilities() {
        int n = preferences.length;
        double[] probs = new double[n];
        double sumExp = 0.0;

        for (int i = 0; i < n; i++) {
            probs[i] = Math.exp(preferences[i]);
            sumExp += probs[i];
        }

        for (int i = 0; i < n; i++) {
            probs[i] /= sumExp;
        }

        return probs;
    }

    /**
     * Get a copy of the current Q-values (average rewards).
     *
     * @return array of Q-values
     */
    public double[] getQValues() {
        return qValues.clone();
    }

    /**
     * Get a copy of the current preferences.
     *
     * @return array of preferences
     */
    public double[] getPreferences() {
        return preferences.clone();
    }

    /**
     * Get the current average reward (baseline).
     *
     * @return average reward
     */
    public double getAverageReward() {
        return averageReward;
    }
}