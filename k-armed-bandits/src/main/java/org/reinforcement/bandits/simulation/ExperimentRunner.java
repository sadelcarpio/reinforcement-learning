package org.reinforcement.bandits.simulation;

import org.reinforcement.bandits.observers.OptimalActionTracker;

/**
 * Utility for running multiple trials of an experiment and aggregating results.
 * Useful for creating learning curves and comparing algorithms.
 */
public class ExperimentRunner {

    /**
     * Run multiple independent trials of a simulation configuration.
     *
     * @param builderTemplate builder with base configuration (will be cloned per trial)
     * @param numTrials number of independent trials to run
     * @param numSteps number of steps per trial
     * @param baseSeed base random seed (each trial gets baseSeed + trialIndex)
     * @return aggregated results
     */
    public static AggregatedResults runExperiment(
            SimulationBuilder builderTemplate,
            int numTrials,
            int numSteps,
            long baseSeed) {

        double[][] optimalActionsByStep = new double[numSteps][numTrials];
        double[][] rewardsByStep = new double[numSteps][numTrials];

        for (int trial = 0; trial < numTrials; trial++) {
            // Create simulation with unique seed
            Object simObj = builderTemplate
                .withSeed(baseSeed + trial)
                .build();

            // Run simulation based on type
            if (simObj instanceof BanditSimulation) {
                runBanditSimulation((BanditSimulation) simObj, trial, numSteps,
                                  optimalActionsByStep, rewardsByStep);
            } else if (simObj instanceof GradientBanditSimulation) {
                runGradientSimulation((GradientBanditSimulation) simObj, trial, numSteps,
                                    optimalActionsByStep, rewardsByStep);
            }
        }

        // Calculate averages across trials for each step
        double[] avgOptimalActionRate = new double[numSteps];
        double[] avgReward = new double[numSteps];

        for (int step = 0; step < numSteps; step++) {
            double sumOptimal = 0.0;
            double sumReward = 0.0;
            for (int trial = 0; trial < numTrials; trial++) {
                sumOptimal += optimalActionsByStep[step][trial];
                sumReward += rewardsByStep[step][trial];
            }
            avgOptimalActionRate[step] = sumOptimal / numTrials;
            avgReward[step] = sumReward / numTrials;
        }

        return new AggregatedResults(avgOptimalActionRate, avgReward, numTrials);
    }

    private static void runBanditSimulation(BanditSimulation simulation, int trial,
                                           int numSteps,
                                           double[][] optimalActionsByStep,
                                           double[][] rewardsByStep) {
        OptimalActionTracker tracker = new OptimalActionTracker(
            simulation.getModel(), numSteps
        );
        simulation.addObserver(tracker);
        simulation.addObserver(new RewardCollector(trial, rewardsByStep));

        simulation.run(numSteps);

        boolean[] wasOptimal = tracker.getWasOptimal();
        for (int step = 0; step < numSteps; step++) {
            optimalActionsByStep[step][trial] = wasOptimal[step] ? 1.0 : 0.0;
        }
    }

    private static void runGradientSimulation(GradientBanditSimulation simulation, int trial,
                                             int numSteps,
                                             double[][] optimalActionsByStep,
                                             double[][] rewardsByStep) {
        OptimalActionTracker tracker = new OptimalActionTracker(
            simulation.getModel(), numSteps
        );
        simulation.addObserver(tracker);
        simulation.addObserver(new RewardCollector(trial, rewardsByStep));

        simulation.run(numSteps);

        boolean[] wasOptimal = tracker.getWasOptimal();
        for (int step = 0; step < numSteps; step++) {
            optimalActionsByStep[step][trial] = wasOptimal[step] ? 1.0 : 0.0;
        }
    }

    /**
     * Helper observer for collecting rewards during a trial.
     */
    private static class RewardCollector implements org.reinforcement.bandits.observers.ISimulationObserver {
        private final int trialIndex;
        private final double[][] rewardsByStep;

        RewardCollector(int trialIndex, double[][] rewardsByStep) {
            this.trialIndex = trialIndex;
            this.rewardsByStep = rewardsByStep;
        }

        @Override
        public void onStepComplete(int step, int selectedAction, double reward) {
            rewardsByStep[step][trialIndex] = reward;
        }

        @Override
        public void onSimulationComplete() {
        }
    }

    /**
     * Results aggregated across multiple trials.
     */
    public static class AggregatedResults {
        private final double[] avgOptimalActionRate;
        private final double[] avgReward;
        private final int numTrials;

        AggregatedResults(double[] avgOptimalActionRate, double[] avgReward, int numTrials) {
            this.avgOptimalActionRate = avgOptimalActionRate;
            this.avgReward = avgReward;
            this.numTrials = numTrials;
        }

        public double[] getAvgOptimalActionRate() {
            return avgOptimalActionRate.clone();
        }

        public double[] getAvgReward() {
            return avgReward.clone();
        }

        public int getNumTrials() {
            return numTrials;
        }

        /**
         * Get the final average optimal action rate (last step).
         */
        public double getFinalOptimalActionRate() {
            return avgOptimalActionRate[avgOptimalActionRate.length - 1];
        }

        /**
         * Get the final average reward (last step).
         */
        public double getFinalAvgReward() {
            return avgReward[avgReward.length - 1];
        }
    }
}