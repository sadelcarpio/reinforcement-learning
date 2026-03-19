package org.reinforcement.bandits.observers;

/**
 * Tracks the distribution of actions selected during the simulation.
 */
public class ActionDistributionTracker implements ISimulationObserver {
    private final int[] actionSelectionCounts;
    private int totalSelections;

    /**
     * Create an action distribution tracker.
     *
     * @param numActions the number of actions
     */
    public ActionDistributionTracker(int numActions) {
        this.actionSelectionCounts = new int[numActions];
        this.totalSelections = 0;
    }

    @Override
    public void onStepComplete(int step, int selectedAction, double reward) {
        actionSelectionCounts[selectedAction]++;
        totalSelections++;
    }

    @Override
    public void onSimulationComplete() {
        // Optional: could log distribution here
    }

    /**
     * Get the number of times each action was selected.
     *
     * @return array of selection counts
     */
    public int[] getActionSelectionCounts() {
        return actionSelectionCounts.clone();
    }

    /**
     * Get the proportion of times each action was selected.
     *
     * @return array of proportions in [0, 1]
     */
    public double[] getActionProportions() {
        double[] proportions = new double[actionSelectionCounts.length];
        if (totalSelections > 0) {
            for (int i = 0; i < actionSelectionCounts.length; i++) {
                proportions[i] = (double) actionSelectionCounts[i] / totalSelections;
            }
        }
        return proportions;
    }
}