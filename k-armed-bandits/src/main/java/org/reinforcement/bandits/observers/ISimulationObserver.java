package org.reinforcement.bandits.observers;

/**
 * Observer interface for monitoring simulation progress.
 * Implements the Observer pattern to decouple simulation tracking from execution.
 */
public interface ISimulationObserver {
    /**
     * Called when a simulation step is completed.
     *
     * @param step the current step number (0-based)
     * @param selectedAction the action that was selected
     * @param reward the reward that was received
     */
    void onStepComplete(int step, int selectedAction, double reward);

    /**
     * Called when the entire simulation is complete.
     */
    void onSimulationComplete();
}