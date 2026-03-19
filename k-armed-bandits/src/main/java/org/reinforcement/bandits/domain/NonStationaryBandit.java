package org.reinforcement.bandits.domain;

import java.util.Random;

/**
 * A non-stationary bandit whose expected reward drifts over time.
 * Each time the bandit is pulled, its expected reward changes by a random walk.
 */
public class NonStationaryBandit implements IBandit {
    private double currentExpectedReward;
    private final double variance;
    private final double driftRate;
    private final Random random;

    /**
     * Create a non-stationary bandit with specified initial expected reward.
     *
     * @param initialExpectedReward the initial expected reward
     * @param variance the variance of the reward distribution
     * @param driftRate the standard deviation of the random walk drift
     * @param random the random number generator
     */
    public NonStationaryBandit(double initialExpectedReward, double variance,
                               double driftRate, Random random) {
        this.currentExpectedReward = initialExpectedReward;
        this.variance = variance;
        this.driftRate = driftRate;
        this.random = random;
    }

    @Override
    public double getReward() {
        // Drift the expected reward (random walk)
        currentExpectedReward += random.nextGaussian(0, driftRate);
        // Return a reward sampled around the new expected value
        return random.nextGaussian(currentExpectedReward, Math.sqrt(variance));
    }

    @Override
    public double getExpectedReward() {
        return currentExpectedReward;
    }

    @Override
    public String toString() {
        return String.format("NonStationaryBandit(μ=%.2f, σ²=%.2f, drift=%.2f)",
                           currentExpectedReward, variance, driftRate);
    }
}