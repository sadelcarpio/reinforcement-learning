package org.reinforcement.bandits.simulation;

import org.reinforcement.bandits.algorithms.BanditAlgorithm;
import org.reinforcement.bandits.algorithms.GradientBanditAlgorithm;
import org.reinforcement.bandits.algorithms.IActionSelector;
import org.reinforcement.bandits.algorithms.IQValueUpdater;
import org.reinforcement.bandits.algorithms.selectors.EpsilonGreedySelector;
import org.reinforcement.bandits.algorithms.selectors.UCBSelector;
import org.reinforcement.bandits.algorithms.updaters.ConstantStepUpdater;
import org.reinforcement.bandits.algorithms.updaters.SampleAverageUpdater;
import org.reinforcement.bandits.domain.IBanditModel;
import org.reinforcement.bandits.factories.IBanditFactory;
import org.reinforcement.bandits.factories.ModelFactory;
import org.reinforcement.bandits.factories.NonStationaryBanditFactory;
import org.reinforcement.bandits.factories.StationaryBanditFactory;

import java.util.Random;

/**
 * Fluent builder for creating and configuring bandit simulations.
 * Provides a convenient API for setting up experiments.
 */
public class SimulationBuilder {
    // Model configuration
    private int numBandits = 10;
    private IBanditFactory banditFactory;
    private IBanditModel model;

    // Algorithm configuration
    private double initialQ = 0.0;
    private IActionSelector actionSelector;
    private IQValueUpdater qValueUpdater;

    // Special algorithms
    private boolean useGradientBandit = false;
    private double gradientStepSize = 0.1;

    // Random number generator
    private Random random = new Random();
    private Long seed;

    /**
     * Set the number of bandits (arms) in the problem.
     *
     * @param numBandits number of bandits
     * @return this builder
     */
    public SimulationBuilder withNumBandits(int numBandits) {
        this.numBandits = numBandits;
        return this;
    }

    /**
     * Use stationary bandits with default parameters.
     *
     * @return this builder
     */
    public SimulationBuilder withStationaryBandits() {
        this.banditFactory = new StationaryBanditFactory(getRandom());
        return this;
    }

    /**
     * Use non-stationary bandits with specified drift rate.
     *
     * @param driftRate standard deviation of the random walk drift
     * @return this builder
     */
    public SimulationBuilder withNonStationaryBandits(double driftRate) {
        this.banditFactory = new NonStationaryBanditFactory(driftRate, getRandom());
        return this;
    }

    /**
     * Use a custom bandit factory.
     *
     * @param factory the factory to use
     * @return this builder
     */
    public SimulationBuilder withBanditFactory(IBanditFactory factory) {
        this.banditFactory = factory;
        return this;
    }

    /**
     * Use a pre-configured model.
     *
     * @param model the model to use
     * @return this builder
     */
    public SimulationBuilder withModel(IBanditModel model) {
        this.model = model;
        return this;
    }

    /**
     * Set the initial Q-value estimate for all actions.
     *
     * @param initialQ initial Q-value
     * @return this builder
     */
    public SimulationBuilder withInitialQ(double initialQ) {
        this.initialQ = initialQ;
        return this;
    }

    /**
     * Use epsilon-greedy action selection.
     *
     * @param epsilon exploration probability [0, 1]
     * @return this builder
     */
    public SimulationBuilder withEpsilonGreedy(double epsilon) {
        this.actionSelector = new EpsilonGreedySelector(epsilon, getRandom());
        this.useGradientBandit = false;
        return this;
    }

    /**
     * Use UCB action selection.
     *
     * @param c exploration parameter
     * @return this builder
     */
    public SimulationBuilder withUCB(double c) {
        this.actionSelector = new UCBSelector(c, getRandom());
        this.useGradientBandit = false;
        return this;
    }

    /**
     * Use a custom action selector.
     *
     * @param selector the selector to use
     * @return this builder
     */
    public SimulationBuilder withActionSelector(IActionSelector selector) {
        this.actionSelector = selector;
        this.useGradientBandit = false;
        return this;
    }

    /**
     * Use sample-average Q-value updates (for stationary problems).
     *
     * @return this builder
     */
    public SimulationBuilder withSampleAverageUpdate() {
        this.qValueUpdater = new SampleAverageUpdater();
        this.useGradientBandit = false;
        return this;
    }

    /**
     * Use constant step-size Q-value updates (for non-stationary problems).
     *
     * @param stepSize the learning rate (alpha)
     * @return this builder
     */
    public SimulationBuilder withConstantStepUpdate(double stepSize) {
        this.qValueUpdater = new ConstantStepUpdater(stepSize);
        this.useGradientBandit = false;
        return this;
    }

    /**
     * Use a custom Q-value updater.
     *
     * @param updater the updater to use
     * @return this builder
     */
    public SimulationBuilder withQValueUpdater(IQValueUpdater updater) {
        this.qValueUpdater = updater;
        this.useGradientBandit = false;
        return this;
    }

    /**
     * Use gradient bandit algorithm with softmax action selection.
     *
     * @param stepSize the learning rate
     * @return this builder
     */
    public SimulationBuilder withGradientBandit(double stepSize) {
        this.useGradientBandit = true;
        this.gradientStepSize = stepSize;
        return this;
    }

    /**
     * Set the random seed for reproducibility.
     *
     * @param seed the random seed
     * @return this builder
     */
    public SimulationBuilder withSeed(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        return this;
    }

    /**
     * Set a custom random number generator.
     *
     * @param random the random number generator
     * @return this builder
     */
    public SimulationBuilder withRandom(Random random) {
        this.random = random;
        this.seed = null;
        return this;
    }

    /**
     * Build the simulation with the configured parameters.
     *
     * @return a new simulation instance
     * @throws IllegalStateException if configuration is invalid
     */
    public Object build() {
        validateConfiguration();

        // Create model if not provided
        IBanditModel finalModel = model;
        if (finalModel == null) {
            if (banditFactory == null) {
                banditFactory = new StationaryBanditFactory(getRandom());
            }
            finalModel = ModelFactory.createModel(numBandits, banditFactory);
        }

        int numActions = finalModel.getNumberOfBandits();

        // Build appropriate simulation type
        if (useGradientBandit) {
            GradientBanditAlgorithm algorithm = new GradientBanditAlgorithm(
                numActions, gradientStepSize, getRandom()
            );
            return new GradientBanditSimulation(finalModel, algorithm);
        } else {
            // Use defaults if not specified
            if (actionSelector == null) {
                actionSelector = new EpsilonGreedySelector(0.1, getRandom());
            }
            if (qValueUpdater == null) {
                qValueUpdater = new SampleAverageUpdater();
            }

            BanditAlgorithm algorithm = new BanditAlgorithm(
                numActions, initialQ, actionSelector, qValueUpdater
            );
            return new BanditSimulation(finalModel, algorithm);
        }
    }

    private void validateConfiguration() {
        if (model == null && numBandits <= 0) {
            throw new IllegalStateException("Number of bandits must be positive");
        }
    }

    private Random getRandom() {
        return random;
    }
}