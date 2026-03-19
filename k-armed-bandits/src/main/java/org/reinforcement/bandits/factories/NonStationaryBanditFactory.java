package org.reinforcement.bandits.factories;

import org.reinforcement.bandits.domain.IBandit;
import org.reinforcement.bandits.domain.NonStationaryBandit;

import java.util.Random;

/**
 * Factory for creating non-stationary bandits whose expected rewards drift over time.
 */
public class NonStationaryBanditFactory implements IBanditFactory {
    private final Random random;
    private final double variance;
    private final double driftRate;
    private final double expectedRewardMean;
    private final double expectedRewardStdDev;

    /**
     * Create a factory with custom parameters.
     *
     * @param expectedRewardMean mean of the distribution for initial expected rewards
     * @param expectedRewardStdDev standard deviation for initial expected rewards
     * @param variance variance of the reward distribution for each bandit
     * @param driftRate standard deviation of the random walk drift per step
     * @param random random number generator
     */
    public NonStationaryBanditFactory(double expectedRewardMean,
                                     double expectedRewardStdDev,
                                     double variance,
                                     double driftRate,
                                     Random random) {
        this.expectedRewardMean = expectedRewardMean;
        this.expectedRewardStdDev = expectedRewardStdDev;
        this.variance = variance;
        this.driftRate = driftRate;
        this.random = random;
    }

    /**
     * Create a factory with default parameters (mean=0, stddev=1, variance=1, drift=1).
     *
     * @param driftRate standard deviation of the random walk drift per step
     * @param random random number generator
     */
    public NonStationaryBanditFactory(double driftRate, Random random) {
        this(0.0, 1.0, 1.0, driftRate, random);
    }

    @Override
    public IBandit createBandit() {
        double initialExpectedReward = random.nextGaussian(expectedRewardMean, expectedRewardStdDev);
        return new NonStationaryBandit(initialExpectedReward, variance, driftRate, random);
    }
}