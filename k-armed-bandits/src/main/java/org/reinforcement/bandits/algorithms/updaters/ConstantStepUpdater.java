package org.reinforcement.bandits.algorithms.updaters;

import org.reinforcement.bandits.algorithms.IQValueUpdater;

/**
 * Updates Q-values using a constant step size: Q(a) = Q(a) + α * (R - Q(a))
 * This gives more weight to recent rewards, suitable for non-stationary problems.
 */
public class ConstantStepUpdater implements IQValueUpdater {
    private final double stepSize;

    /**
     * Create an updater with the specified step size.
     *
     * @param stepSize the learning rate (alpha), typically between 0 and 1
     */
    public ConstantStepUpdater(double stepSize) {
        if (stepSize <= 0 || stepSize > 1) {
            throw new IllegalArgumentException("Step size must be in (0, 1]");
        }
        this.stepSize = stepSize;
    }

    @Override
    public void updateQValue(int action, double reward, double[] qValues, int[] actionCounts) {
        qValues[action] = qValues[action] + stepSize * (reward - qValues[action]);
    }
}