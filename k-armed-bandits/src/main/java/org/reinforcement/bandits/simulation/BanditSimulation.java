package org.reinforcement.bandits.simulation;

import org.reinforcement.bandits.algorithms.BanditAlgorithm;
import org.reinforcement.bandits.domain.IBanditModel;
import org.reinforcement.bandits.observers.ISimulationObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates a bandit simulation by coordinating the model, algorithm, and observers.
 * Implements the core simulation loop following clean architecture principles.
 */
public class BanditSimulation {
    private final IBanditModel model;
    private final BanditAlgorithm algorithm;
    private final List<ISimulationObserver> observers;

    /**
     * Create a simulation with the specified model and algorithm.
     *
     * @param model the bandit model
     * @param algorithm the learning algorithm
     */
    public BanditSimulation(IBanditModel model, BanditAlgorithm algorithm) {
        if (model == null || algorithm == null) {
            throw new IllegalArgumentException("Model and algorithm must not be null");
        }
        this.model = model;
        this.algorithm = algorithm;
        this.observers = new ArrayList<>();
    }

    /**
     * Add an observer to track simulation progress.
     *
     * @param observer the observer to add
     */
    public void addObserver(ISimulationObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    /**
     * Run the simulation for the specified number of steps.
     *
     * @param numSteps number of steps to run
     * @return the simulation result
     */
    public SimulationResult run(int numSteps) {
        if (numSteps <= 0) {
            throw new IllegalArgumentException("Number of steps must be positive");
        }

        for (int step = 0; step < numSteps; step++) {
            // Select action using the algorithm
            int selectedAction = algorithm.selectAction();

            // Get reward from the model
            double reward = model.getReward(selectedAction);

            // Update the algorithm
            algorithm.update(selectedAction, reward);

            // Notify observers
            notifyObservers(step, selectedAction, reward);
        }

        // Notify observers of completion
        for (ISimulationObserver observer : observers) {
            observer.onSimulationComplete();
        }

        // Return results
        return new SimulationResult(
            algorithm.getQValues(),
            algorithm.getActionCounts(),
            model.getOptimalBanditIndex()
        );
    }

    private void notifyObservers(int step, int selectedAction, double reward) {
        for (ISimulationObserver observer : observers) {
            observer.onStepComplete(step, selectedAction, reward);
        }
    }

    /**
     * Get the underlying model.
     *
     * @return the bandit model
     */
    public IBanditModel getModel() {
        return model;
    }

    /**
     * Get the underlying algorithm.
     *
     * @return the bandit algorithm
     */
    public BanditAlgorithm getAlgorithm() {
        return algorithm;
    }
}