package org.reinforcement.bandits.algorithms;

/**
 * Strategy for selecting which action (bandit) to choose next.
 */
public interface IActionSelector {
    /**
     * Select an action based on the current context.
     *
     * @param context the current state including Q-values and action counts
     * @return the index of the selected action
     */
    int selectAction(ActionSelectionContext context);
}