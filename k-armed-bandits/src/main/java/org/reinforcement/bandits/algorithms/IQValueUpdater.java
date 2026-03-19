package org.reinforcement.bandits.algorithms;

/**
 * Strategy for updating Q-value estimates after receiving a reward.
 */
public interface IQValueUpdater {
    /**
     * Update the Q-value estimate for the given action.
     *
     * @param action the action that was taken
     * @param reward the reward that was received
     * @param qValues the current Q-value array (will be modified)
     * @param actionCounts the current action count array
     */
    void updateQValue(int action, double reward, double[] qValues, int[] actionCounts);
}