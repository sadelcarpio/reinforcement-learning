package org.reinforcement.bandits.simulation;

import org.reinforcement.bandits.algorithms.GradientBanditAlgorithm;
import org.reinforcement.bandits.domain.IBanditModel;
import org.reinforcement.bandits.observers.ISimulationObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized simulation for gradient bandit algorithms.
 * Similar to BanditSimulation but works with GradientBanditAlgorithm.
 */
public class GradientBanditSimulation {
    private final IBanditModel model;
    private final GradientBanditAlgorithm algorithm;
    private final List<ISimulationObserver> observers;

    /**
     * Create a gradient bandit simulation.
     *
     * @param model the bandit model
     * @param algorithm the gradient bandit algorithm
     */
    public GradientBanditSimulation(IBanditModel model, GradientBanditAlgorithm algorithm) {
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
            int selectedAction = algorithm.selectAction();
            double reward = model.getReward(selectedAction);
            algorithm.update(selectedAction, reward);
            notifyObservers(step, selectedAction, reward);
        }

        for (ISimulationObserver observer : observers) {
            observer.onSimulationComplete();
        }

        return new SimulationResult(
            algorithm.getQValues(),
            new int[0], // Gradient bandit doesn't track action counts the same way
            model.getOptimalBanditIndex()
        );
    }

    private void notifyObservers(int step, int selectedAction, double reward) {
        for (ISimulationObserver observer : observers) {
            observer.onStepComplete(step, selectedAction, reward);
        }
    }

    public IBanditModel getModel() {
        return model;
    }

    public GradientBanditAlgorithm getAlgorithm() {
        return algorithm;
    }
}