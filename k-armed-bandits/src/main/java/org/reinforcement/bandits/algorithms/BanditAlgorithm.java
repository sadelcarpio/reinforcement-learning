package org.reinforcement.bandits.algorithms;

import java.util.Arrays;

/**
 * A bandit algorithm that combines action selection and Q-value update strategies.
 * Follows the Strategy pattern for flexible composition.
 */
public class BanditAlgorithm {
    private final IActionSelector actionSelector;
    private final IQValueUpdater qValueUpdater;
    private final double[] qValues;
    private final int[] actionCounts;
    private int totalSteps;

    /**
     * Create a bandit algorithm with specified components.
     *
     * @param numActions number of actions (bandits)
     * @param initialQ initial Q-value estimate for all actions
     * @param actionSelector strategy for selecting actions
     * @param qValueUpdater strategy for updating Q-values
     */
    public BanditAlgorithm(int numActions, double initialQ,
                          IActionSelector actionSelector,
                          IQValueUpdater qValueUpdater) {
        if (numActions <= 0) {
            throw new IllegalArgumentException("Number of actions must be positive");
        }
        if (actionSelector == null || qValueUpdater == null) {
            throw new IllegalArgumentException("Selector and updater must not be null");
        }

        this.actionSelector = actionSelector;
        this.qValueUpdater = qValueUpdater;
        this.qValues = new double[numActions];
        this.actionCounts = new int[numActions];
        this.totalSteps = 0;

        Arrays.fill(qValues, initialQ);
        Arrays.fill(actionCounts, 0);
    }

    /**
     * Select the next action to take.
     *
     * @return the index of the selected action
     */
    public int selectAction() {
        ActionSelectionContext context = new ActionSelectionContext(
            qValues, actionCounts, totalSteps
        );
        return actionSelector.selectAction(context);
    }

    /**
     * Update the algorithm after receiving a reward.
     *
     * @param action the action that was taken
     * @param reward the reward that was received
     */
    public void update(int action, double reward) {
        actionCounts[action]++;
        totalSteps++;
        qValueUpdater.updateQValue(action, reward, qValues, actionCounts);
    }

    /**
     * Get a copy of the current Q-values.
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
     * Get the total number of steps taken.
     *
     * @return total steps
     */
    public int getTotalSteps() {
        return totalSteps;
    }
}