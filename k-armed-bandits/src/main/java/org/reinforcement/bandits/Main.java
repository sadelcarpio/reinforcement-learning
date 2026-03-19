package org.reinforcement.bandits;

import org.reinforcement.bandits.observers.OptimalActionTracker;
import org.reinforcement.bandits.observers.RewardTracker;
import org.reinforcement.bandits.simulation.*;

import java.util.Arrays;

/**
 * Main class demonstrating the refactored k-armed bandits simulation.
 * Shows examples of different algorithms and configurations.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== K-Armed Bandits Simulation (Refactored) ===\n");

        // Example 1: Epsilon-Greedy with stationary bandits
        runEpsilonGreedyExample();

        // Example 2: UCB with stationary bandits
        runUCBExample();

        // Example 3: Gradient Bandit
        runGradientBanditExample();

        // Example 4: Non-stationary bandits with constant step size
        runNonStationaryExample();

        // Example 5: Comparison experiment across multiple trials
        runComparisonExperiment();
    }

    private static void runEpsilonGreedyExample() {
        System.out.println("--- Example 1: Epsilon-Greedy (ε=0.1) ---");

        BanditSimulation simulation = (BanditSimulation) new SimulationBuilder()
            .withSeed(42)
            .withNumBandits(10)
            .withStationaryBandits()
            .withEpsilonGreedy(0.1)
            .withSampleAverageUpdate()
            .withInitialQ(0.0)
            .build();

        OptimalActionTracker tracker = new OptimalActionTracker(simulation.getModel(), 2000);
        RewardTracker rewardTracker = new RewardTracker(2000);
        simulation.addObserver(tracker);
        simulation.addObserver(rewardTracker);

        SimulationResult result = simulation.run(2000);

        System.out.println("Optimal action: " + result.getOptimalActionIndex());
        System.out.println("Best learned action: " + result.getBestActionIndex());
        System.out.println("Q-values: " + Arrays.toString(formatArray(result.getQValues())));
        System.out.println("Optimal action chosen: " +
            String.format("%.2f%%", tracker.getOptimalActionPercentage() * 100));
        System.out.println("Average reward: " + String.format("%.3f", rewardTracker.getAverageReward()));
        System.out.println();
    }

    private static void runUCBExample() {
        System.out.println("--- Example 2: Upper Confidence Bound (c=2.0) ---");

        BanditSimulation simulation = (BanditSimulation) new SimulationBuilder()
            .withSeed(123)
            .withNumBandits(10)
            .withStationaryBandits()
            .withUCB(2.0)
            .withSampleAverageUpdate()
            .build();

        OptimalActionTracker tracker = new OptimalActionTracker(simulation.getModel(), 2000);
        RewardTracker rewardTracker = new RewardTracker(2000);
        simulation.addObserver(tracker);
        simulation.addObserver(rewardTracker);

        SimulationResult result = simulation.run(2000);

        System.out.println("Optimal action: " + result.getOptimalActionIndex());
        System.out.println("Best learned action: " + result.getBestActionIndex());
        System.out.println("Q-values: " + Arrays.toString(formatArray(result.getQValues())));
        System.out.println("Optimal action chosen: " +
            String.format("%.2f%%", tracker.getOptimalActionPercentage() * 100));
        System.out.println("Average reward: " + String.format("%.3f", rewardTracker.getAverageReward()));
        System.out.println();
    }

    private static void runGradientBanditExample() {
        System.out.println("--- Example 3: Gradient Bandit (α=0.1) ---");

        GradientBanditSimulation simulation = (GradientBanditSimulation) new SimulationBuilder()
            .withSeed(456)
            .withNumBandits(10)
            .withStationaryBandits()
            .withGradientBandit(0.1)
            .build();

        OptimalActionTracker tracker = new OptimalActionTracker(simulation.getModel(), 2000);
        RewardTracker rewardTracker = new RewardTracker(2000);
        simulation.addObserver(tracker);
        simulation.addObserver(rewardTracker);

        SimulationResult result = simulation.run(2000);

        System.out.println("Optimal action: " + result.getOptimalActionIndex());
        System.out.println("Best learned action: " + result.getBestActionIndex());
        System.out.println("Q-values: " + Arrays.toString(formatArray(result.getQValues())));
        System.out.println("Preferences: " +
            Arrays.toString(formatArray(simulation.getAlgorithm().getPreferences())));
        System.out.println("Optimal action chosen: " +
            String.format("%.2f%%", tracker.getOptimalActionPercentage() * 100));
        System.out.println("Average reward: " + String.format("%.3f", rewardTracker.getAverageReward()));
        System.out.println();
    }

    private static void runNonStationaryExample() {
        System.out.println("--- Example 4: Non-Stationary Bandits (ε=0.1, α=0.1) ---");

        BanditSimulation simulation = (BanditSimulation) new SimulationBuilder()
            .withSeed(789)
            .withNumBandits(10)
            .withNonStationaryBandits(0.01)  // Small drift rate
            .withEpsilonGreedy(0.1)
            .withConstantStepUpdate(0.1)  // Constant step size for non-stationary
            .build();

        OptimalActionTracker tracker = new OptimalActionTracker(simulation.getModel(), 2000);
        RewardTracker rewardTracker = new RewardTracker(2000);
        simulation.addObserver(tracker);
        simulation.addObserver(rewardTracker);

        SimulationResult result = simulation.run(2000);

        System.out.println("Initial optimal action: " + result.getOptimalActionIndex());
        System.out.println("Best learned action: " + result.getBestActionIndex());
        System.out.println("Q-values: " + Arrays.toString(formatArray(result.getQValues())));
        System.out.println("Optimal action chosen: " +
            String.format("%.2f%%", tracker.getOptimalActionPercentage() * 100));
        System.out.println("Average reward: " + String.format("%.3f", rewardTracker.getAverageReward()));
        System.out.println("Note: Optimal action changes over time due to drift");
        System.out.println();
    }

    private static void runComparisonExperiment() {
        System.out.println("--- Example 5: Multi-Trial Comparison ---");
        System.out.println("Running 100 trials of 1000 steps each...");

        SimulationBuilder builder = new SimulationBuilder()
            .withNumBandits(10)
            .withStationaryBandits()
            .withEpsilonGreedy(0.1)
            .withSampleAverageUpdate();

        ExperimentRunner.AggregatedResults results =
            ExperimentRunner.runExperiment(builder, 100, 1000, 1000);

        System.out.println("Final average optimal action rate: " +
            String.format("%.2f%%", results.getFinalOptimalActionRate() * 100));
        System.out.println("Final average reward: " +
            String.format("%.3f", results.getFinalAvgReward()));
        System.out.println("Number of trials: " + results.getNumTrials());
        System.out.println();

        System.out.println("These aggregated results can be plotted to create learning curves!");
    }

    /**
     * Format array values to 2 decimal places.
     */
    private static String[] formatArray(double[] arr) {
        String[] formatted = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            formatted[i] = String.format("%.2f", arr[i]);
        }
        return formatted;
    }
}