package org.reinforcement.bandits.visualization;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.reinforcement.bandits.simulation.ExperimentRunner;
import org.reinforcement.bandits.simulation.SimulationBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstration of visualization capabilities for bandit simulations.
 * Creates comparison plots between different algorithms.
 */
public class VisualizationDemo {

    public static void main(String[] args) {
        System.out.println("=== Bandit Simulation Visualization Demo ===\n");

        // Run experiments for different algorithms
        System.out.println("Running experiments... (this may take a minute)");

        int numTrials = 1000;
        int numSteps = 1000;
        long baseSeed = 42;

        // Epsilon-Greedy (ε=0.0 - pure greedy)
        System.out.println("  Running greedy...");
        ExperimentRunner.AggregatedResults greedyResults =
            ExperimentRunner.runExperiment(
                new SimulationBuilder()
                    .withNumBandits(10)
                    .withStationaryBandits()
                    .withEpsilonGreedy(0.0)
                    .withSampleAverageUpdate(),
                numTrials, numSteps, baseSeed
            );

        // Epsilon-Greedy (ε=0.01)
        System.out.println("  Running ε-greedy (0.01)...");
        ExperimentRunner.AggregatedResults epsilonSmallResults =
            ExperimentRunner.runExperiment(
                new SimulationBuilder()
                    .withNumBandits(10)
                    .withStationaryBandits()
                    .withConstantStepUpdate(0.1)
                    .withEpsilonGreedy(0.01),
                numTrials, numSteps, baseSeed + 100
            );

        // Epsilon-Greedy Optimistic (ε=0.0 - pure greedy)
        System.out.println("  Running greedy...");
        ExperimentRunner.AggregatedResults optimisticResults =
                ExperimentRunner.runExperiment(
                        new SimulationBuilder()
                                .withNumBandits(10)
                                .withStationaryBandits()
                                .withEpsilonGreedy(0.0)
                                .withConstantStepUpdate(0.1)
                                .withInitialQ(5.0),
                        numTrials, numSteps, baseSeed
                );
        // Epsilon-Greedy (ε=0.1)
        System.out.println("  Running ε-greedy (0.1)...");
        ExperimentRunner.AggregatedResults epsilonResults =
            ExperimentRunner.runExperiment(
                new SimulationBuilder()
                    .withNumBandits(10)
                    .withStationaryBandits()
                    .withEpsilonGreedy(0.1)
                    .withSampleAverageUpdate(),
                numTrials, numSteps, baseSeed + 200
            );

        // UCB
        System.out.println("  Running UCB...");
        ExperimentRunner.AggregatedResults ucbResults =
            ExperimentRunner.runExperiment(
                new SimulationBuilder()
                    .withNumBandits(10)
                    .withStationaryBandits()
                    .withUCB(2.0)
                    .withSampleAverageUpdate(),
                numTrials, numSteps, baseSeed + 300
            );

        // Gradient Bandit
        System.out.println("  Running Gradient Bandit...");
        ExperimentRunner.AggregatedResults gradientResults =
            ExperimentRunner.runExperiment(
                new SimulationBuilder()
                    .withNumBandits(10)
                    .withStationaryBandits()
                    .withGradientBandit(0.1),
                numTrials, numSteps, baseSeed + 400
            );

        System.out.println("\nCreating visualizations...\n");

        // Create comparison charts
        double[] steps = ChartBuilder.generateStepData(numSteps);

        // Average reward comparison
        Map<String, double[]> rewardComparison = new HashMap<>();
        rewardComparison.put("Greedy (ε=0)", greedyResults.getAvgReward());
        rewardComparison.put("Optimistic Greedy (ε=0)", optimisticResults.getAvgReward());
        rewardComparison.put("ε-greedy (0.01)", epsilonSmallResults.getAvgReward());
        rewardComparison.put("ε-greedy (0.1)", epsilonResults.getAvgReward());
        rewardComparison.put("UCB (c=2)", ucbResults.getAvgReward());
        rewardComparison.put("Gradient Bandit", gradientResults.getAvgReward());

        XYChart rewardChart = ChartBuilder.createComparisonChart(
            "Algorithm Comparison - Average Reward",
            steps,
            rewardComparison
        );

        // Optimal action comparison
        Map<String, double[]> optimalComparison = new HashMap<>();
        optimalComparison.put("Greedy (ε=0)", greedyResults.getAvgOptimalActionRate());
        optimalComparison.put("Optimistic Greedy (ε=0)", optimisticResults.getAvgOptimalActionRate());
        optimalComparison.put("ε-greedy (0.01)", epsilonSmallResults.getAvgOptimalActionRate());
        optimalComparison.put("ε-greedy (0.1)", epsilonResults.getAvgOptimalActionRate());
        optimalComparison.put("UCB (c=2)", ucbResults.getAvgOptimalActionRate());
        optimalComparison.put("Gradient Bandit", gradientResults.getAvgOptimalActionRate());

        XYChart optimalChart = ChartBuilder.createOptimalActionChart(
            "Algorithm Comparison - Optimal Action Selection",
            steps,
            optimalComparison.get("ε-greedy (0.1)"),
            "ε-greedy (0.1)"
        );

        // Add other series manually
        for (Map.Entry<String, double[]> entry : optimalComparison.entrySet()) {
            if (!entry.getKey().equals("ε-greedy (0.1)")) {
                double[] percentageData = new double[entry.getValue().length];
                for (int i = 0; i < entry.getValue().length; i++) {
                    percentageData[i] = entry.getValue()[i] * 100.0;
                }
                optimalChart.addSeries(entry.getKey(), steps, percentageData);
            }
        }

        // Display charts
        ChartBuilder.displayChart(rewardChart);
        ChartBuilder.displayChart(optimalChart);

        // Save charts to files
        try {
            ChartBuilder.saveChart(rewardChart, "./bandit_rewards_comparison",
                BitmapEncoder.BitmapFormat.PNG);
            ChartBuilder.saveChart(optimalChart, "./bandit_optimal_comparison",
                BitmapEncoder.BitmapFormat.PNG);
            System.out.println("Charts saved to:");
            System.out.println("  - ./bandit_rewards_comparison.png");
            System.out.println("  - ./bandit_optimal_comparison.png");
        } catch (IOException e) {
            System.err.println("Failed to save charts: " + e.getMessage());
        }

        // Print summary statistics
        System.out.println("\n=== Final Performance (after " + numSteps + " steps, " +
                          numTrials + " trials) ===");
        System.out.printf("%-20s | Avg Reward | Optimal %% %n", "Algorithm");
        System.out.println("---------------------------------------------------");
        System.out.printf("%-20s | %10.3f | %9.1f%% %n", "Greedy",
            greedyResults.getFinalAvgReward(),
            greedyResults.getFinalOptimalActionRate() * 100);
        System.out.printf("%-20s | %10.3f | %9.1f%% %n", "ε-greedy (0.01)",
            epsilonSmallResults.getFinalAvgReward(),
            epsilonSmallResults.getFinalOptimalActionRate() * 100);
        System.out.printf("%-20s | %10.3f | %9.1f%% %n", "ε-greedy (0.1)",
            epsilonResults.getFinalAvgReward(),
            epsilonResults.getFinalOptimalActionRate() * 100);
        System.out.printf("%-20s | %10.3f | %9.1f%% %n", "UCB",
            ucbResults.getFinalAvgReward(),
            ucbResults.getFinalOptimalActionRate() * 100);
        System.out.printf("%-20s | %10.3f | %9.1f%% %n", "Gradient Bandit",
            gradientResults.getFinalAvgReward(),
            gradientResults.getFinalOptimalActionRate() * 100);

        System.out.println("\n✓ Visualization complete!");
    }
}