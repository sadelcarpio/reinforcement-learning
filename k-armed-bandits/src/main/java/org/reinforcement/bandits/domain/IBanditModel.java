package org.reinforcement.bandits.domain;

/**
 * Represents a k-armed bandit problem model containing multiple bandits.
 */
public interface IBanditModel {
    /**
     * Get the number of bandits (arms) in this model.
     *
     * @return the number of bandits
     */
    int getNumberOfBandits();

    /**
     * Pull the specified bandit's arm and receive a reward.
     *
     * @param banditIndex the index of the bandit to pull (0-based)
     * @return the reward value
     * @throws IndexOutOfBoundsException if banditIndex is invalid
     */
    double getReward(int banditIndex);

    /**
     * Get the bandit at the specified index.
     *
     * @param banditIndex the index of the bandit (0-based)
     * @return the bandit instance
     * @throws IndexOutOfBoundsException if banditIndex is invalid
     */
    IBandit getBandit(int banditIndex);

    /**
     * Find the index of the optimal bandit (highest expected reward).
     *
     * @return the index of the optimal bandit
     */
    int getOptimalBanditIndex();
}