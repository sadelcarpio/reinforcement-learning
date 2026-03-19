package org.reinforcement.bandits.observers;

import org.reinforcement.bandits.domain.IBanditModel;

/**
 * Tracks whether the optimal action was selected at each step.
 * Decouples testing logic from simulation execution.
 */
public class OptimalActionTracker implements ISimulationObserver {
    private final int optimalAction;
    private final boolean[] wasOptimal;
    private int optimalCount;

    /**
     * Create a tracker for the given model.
     *
     * @param model the bandit model to track
     * @param numSteps the number of steps in the simulation
     */
    public OptimalActionTracker(IBanditModel model, int numSteps) {
        this.optimalAction = model.getOptimalBanditIndex();
        this.wasOptimal = new boolean[numSteps];
        this.optimalCount = 0;
    }

    @Override
    public void onStepComplete(int step, int selectedAction, double reward) {
        boolean isOptimal = (selectedAction == optimalAction);
        wasOptimal[step] = isOptimal;
        if (isOptimal) {
            optimalCount++;
        }
    }

    @Override
    public void onSimulationComplete() {
        // Optional: could log statistics here
    }

    /**
     * Get the percentage of times the optimal action was chosen.
     *
     * @return percentage in [0, 1]
     */
    public double getOptimalActionPercentage() {
        return wasOptimal.length > 0 ? (double) optimalCount / wasOptimal.length : 0.0;
    }

    /**
     * Get a copy of the optimal action choices for each step.
     *
     * @return array of boolean values
     */
    public boolean[] getWasOptimal() {
        return wasOptimal.clone();
    }

    /**
     * Get the index of the optimal action.
     *
     * @return optimal action index
     */
    public int getOptimalAction() {
        return optimalAction;
    }
}