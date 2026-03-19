package org.reinforcement.bandits.examples;

import org.reinforcement.bandits.domain.IBandit;
import org.reinforcement.bandits.domain.IBanditModel;
import org.reinforcement.bandits.factories.ModelFactory;
import org.reinforcement.bandits.factories.StationaryBanditFactory;

import java.util.Random;

/**
 * Quick verification that bandits are initialized with different expected rewards
 * sampled from N(0, 1).
 */
public class VerifyBanditInitialization {
    public static void main(String[] args) {
        System.out.println("=== Verifying Bandit Initialization ===\n");

        // Create model with 10 bandits
        Random random = new Random(42);
        StationaryBanditFactory factory = new StationaryBanditFactory(random);
        IBanditModel model = ModelFactory.createModel(10, factory);

        System.out.println("Expected rewards for each bandit (should be different, ~N(0,1)):");
        System.out.println("Bandit | Expected Reward");
        System.out.println("-------|----------------");

        double sum = 0;
        double sumSquared = 0;

        for (int i = 0; i < model.getNumberOfBandits(); i++) {
            IBandit bandit = model.getBandit(i);
            double expectedReward = bandit.getExpectedReward();
            sum += expectedReward;
            sumSquared += expectedReward * expectedReward;
            System.out.printf("  %2d   | %+7.4f\n", i, expectedReward);
        }

        double mean = sum / model.getNumberOfBandits();
        double variance = (sumSquared / model.getNumberOfBandits()) - (mean * mean);
        double stddev = Math.sqrt(variance);

        System.out.println("\nStatistics of expected rewards:");
        System.out.printf("Mean:   %.4f (should be ~0.0)\n", mean);
        System.out.printf("StdDev: %.4f (should be ~1.0)\n", stddev);
        System.out.printf("Min:    %.4f\n",
            getMinExpectedReward(model));
        System.out.printf("Max:    %.4f\n",
            getMaxExpectedReward(model));

        System.out.println("\n✓ Verification complete!");
        System.out.println("If bandits show different values ~N(0,1), initialization is correct.");
    }

    private static double getMinExpectedReward(IBanditModel model) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < model.getNumberOfBandits(); i++) {
            double expected = model.getBandit(i).getExpectedReward();
            if (expected < min) min = expected;
        }
        return min;
    }

    private static double getMaxExpectedReward(IBanditModel model) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < model.getNumberOfBandits(); i++) {
            double expected = model.getBandit(i).getExpectedReward();
            if (expected > max) max = expected;
        }
        return max;
    }
}