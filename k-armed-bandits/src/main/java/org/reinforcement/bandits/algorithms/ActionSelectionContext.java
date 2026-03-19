package org.reinforcement.bandits.algorithms;

/**
 * Context object providing state information for action selection.
 * Immutable to prevent accidental modifications.
 */
public class ActionSelectionContext {
    private final double[] qValues;
    private final int[] actionCounts;
    private final int totalSteps;

    public ActionSelectionContext(double[] qValues, int[] actionCounts, int totalSteps) {
        this.qValues = qValues.clone(); // defensive copy
        this.actionCounts = actionCounts.clone();
        this.totalSteps = totalSteps;
    }

    public int getNumActions() {
        return qValues.length;
    }

    public double getQValue(int action) {
        return qValues[action];
    }

    public int getActionCount(int action) {
        return actionCounts[action];
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Get the greedy action (highest Q-value).
     * Breaks ties randomly.
     *
     * @return index of the greedy action
     */
    public int getGreedyAction() {
        double maxQ = qValues[0];
        int countMax = 1;
        int firstMaxIndex = 0;

        for (int i = 1; i < qValues.length; i++) {
            if (qValues[i] > maxQ) {
                maxQ = qValues[i];
                countMax = 1;
                firstMaxIndex = i;
            } else if (qValues[i] == maxQ) {
                countMax++;
            }
        }

        // If only one max, return it
        if (countMax == 1) {
            return firstMaxIndex;
        }

        // Otherwise, randomly select among tied actions
        int[] maxIndices = new int[countMax];
        int idx = 0;
        for (int i = 0; i < qValues.length; i++) {
            if (qValues[i] == maxQ) {
                maxIndices[idx++] = i;
            }
        }

        return maxIndices[(int) (Math.random() * countMax)];
    }

    public double[] getQValues() {
        return qValues.clone();
    }

    public int[] getActionCounts() {
        return actionCounts.clone();
    }
}