package org.reinforcement.bandits.domain;

/**
 * Represents a single bandit (arm) in a multi-armed bandit problem.
 * A bandit can be pulled to receive a reward sampled from its reward distribution.
 */
public interface IBandit {
    /**
     * Pull this bandit's arm and receive a reward.
     * The reward is sampled from the bandit's reward distribution.
     *
     * @return the reward value
     */
    double getReward();

    /**
     * Get the true expected reward of this bandit.
     * This is typically unknown in real scenarios but useful for evaluation.
     *
     * @return the expected reward value
     */
    double getExpectedReward();
}