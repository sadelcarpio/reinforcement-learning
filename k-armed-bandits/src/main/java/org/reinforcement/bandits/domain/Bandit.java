package org.reinforcement.bandits.domain;

import java.util.Random;

/**
 * A stationary bandit with a fixed expected reward.
 * Rewards are sampled from a Gaussian distribution with the expected reward as mean.
 */
public class Bandit implements IBandit {
    private final double expectedReward;
    private final double variance;
    private final Random random;

    /**
     * Create a bandit with specified expected reward.
     *
     * @param expectedReward the true expected reward
     * @param variance the variance of the reward distribution
     * @param random the random number generator
     */
    public Bandit(double expectedReward, double variance, Random random) {
        this.expectedReward = expectedReward;
        this.variance = variance;
        this.random = random;
    }

    @Override
    public double getReward() {
        return random.nextGaussian(expectedReward, Math.sqrt(variance));
    }

    @Override
    public double getExpectedReward() {
        return expectedReward;
    }

    @Override
    public String toString() {
        return String.format("Bandit(μ=%.2f, σ²=%.2f)", expectedReward, variance);
    }
}