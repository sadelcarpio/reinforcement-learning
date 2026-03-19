package org.reinforcement.bandits.algorithms.updaters;

import org.reinforcement.bandits.algorithms.IQValueUpdater;

/**
 * Updates Q-values using sample averaging: Q(a) = Q(a) + (1/n) * (R - Q(a))
 * This is the standard incremental update rule for stationary problems.
 */
public class SampleAverageUpdater implements IQValueUpdater {
    @Override
    public void updateQValue(int action, double reward, double[] qValues, int[] actionCounts) {
        int n = actionCounts[action];
        if (n > 0) {
            qValues[action] = qValues[action] + (1.0 / n) * (reward - qValues[action]);
        } else {
            qValues[action] = reward;
        }
    }
}