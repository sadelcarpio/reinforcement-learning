package org.reinforcement.bandits.factories;

import org.reinforcement.bandits.domain.Bandit;
import org.reinforcement.bandits.domain.IBandit;

import java.util.Random;

/**
 * Factory for creating stationary bandits with normally distributed expected rewards.
 */
public class StationaryBanditFactory implements IBanditFactory {
    private final Random random;
    private final double variance;
    private final double expectedRewardMean;
    private final double expectedRewardStdDev;

    /**
     * Create a factory with custom parameters.
     *
     * @param expectedRewardMean mean of the distribution for initial expected rewards
     * @param expectedRewardStdDev standard deviation for initial expected rewards
     * @param variance variance of the reward distribution for each bandit
     * @param random random number generator
     */
    public StationaryBanditFactory(double expectedRewardMean,
                                  double expectedRewardStdDev,
                                  double variance,
                                  Random random) {
        this.expectedRewardMean = expectedRewardMean;
        this.expectedRewardStdDev = expectedRewardStdDev;
        this.variance = variance;
        this.random = random;
    }

    /**
     * Create a factory with default parameters (mean=0, stddev=1, variance=1).
     *
     * @param random random number generator
     */
    public StationaryBanditFactory(Random random) {
        this(0.0, 1.0, 1.0, random);
    }

    @Override
    public IBandit createBandit() {
        double expectedReward = random.nextGaussian(expectedRewardMean, expectedRewardStdDev);
        return new Bandit(expectedReward, variance, random);
    }
}