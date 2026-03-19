package org.reinforcement.bandits.simulation;

import java.util.Arrays;

/**
 * Immutable result object containing the outcome of a simulation run.
 */
public class SimulationResult {
    private final double[] qValues;
    private final int[] actionCounts;
    private final int optimalActionIndex;

    /**
     * Create a simulation result.
     *
     * @param qValues the final Q-values
     * @param actionCounts the action selection counts
     * @param optimalActionIndex the index of the optimal action
     */
    public SimulationResult(double[] qValues, int[] actionCounts, int optimalActionIndex) {
        this.qValues = qValues.clone();
        this.actionCounts = actionCounts.clone();
        this.optimalActionIndex = optimalActionIndex;
    }

    /**
     * Get a copy of the final Q-values.
     *
     * @return array of Q-values
     */
    public double[] getQValues() {
        return qValues.clone();
    }

    /**
     * Get a copy of the action counts.
     *
     * @return array of action counts
     */
    public int[] getActionCounts() {
        return actionCounts.clone();
    }

    /**
     * Get the index of the optimal action.
     *
     * @return optimal action index
     */
    public int getOptimalActionIndex() {
        return optimalActionIndex;
    }

    /**
     * Get the index of the action with the highest Q-value.
     *
     * @return index of best estimated action
     */
    public int getBestActionIndex() {
        int bestIndex = 0;
        double maxQ = qValues[0];
        for (int i = 1; i < qValues.length; i++) {
            if (qValues[i] > maxQ) {
                maxQ = qValues[i];
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    /**
     * Check if the algorithm identified the optimal action.
     *
     * @return true if the best Q-value corresponds to the optimal action
     */
    public boolean identifiedOptimalAction() {
        return getBestActionIndex() == optimalActionIndex;
    }

    @Override
    public String toString() {
        return String.format(
            "SimulationResult{qValues=%s, actionCounts=%s, optimal=%d, identified=%b}",
            Arrays.toString(qValues),
            Arrays.toString(actionCounts),
            optimalActionIndex,
            identifiedOptimalAction()
        );
    }
}