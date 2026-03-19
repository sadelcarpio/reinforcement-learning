package org.reinforcement.bandits.domain;

/**
 * A k-armed bandit problem model containing k bandits.
 * Uses composition and delegation instead of direct array manipulation.
 */
public class KArmedBanditsModel implements IBanditModel {
    private final IBandit[] bandits;

    /**
     * Create a model with the specified bandits.
     *
     * @param bandits the array of bandits
     * @throws IllegalArgumentException if bandits array is null or empty
     */
    public KArmedBanditsModel(IBandit[] bandits) {
        if (bandits == null || bandits.length == 0) {
            throw new IllegalArgumentException("Bandits array must not be null or empty");
        }
        this.bandits = bandits.clone(); // defensive copy
    }

    @Override
    public int getNumberOfBandits() {
        return bandits.length;
    }

    @Override
    public double getReward(int banditIndex) {
        validateBanditIndex(banditIndex);
        return bandits[banditIndex].getReward();
    }

    @Override
    public IBandit getBandit(int banditIndex) {
        validateBanditIndex(banditIndex);
        return bandits[banditIndex];
    }

    @Override
    public int getOptimalBanditIndex() {
        int optimalIndex = 0;
        double maxExpectedReward = bandits[0].getExpectedReward();

        for (int i = 1; i < bandits.length; i++) {
            double expectedReward = bandits[i].getExpectedReward();
            if (expectedReward > maxExpectedReward) {
                maxExpectedReward = expectedReward;
                optimalIndex = i;
            }
        }

        return optimalIndex;
    }

    private void validateBanditIndex(int banditIndex) {
        if (banditIndex < 0 || banditIndex >= bandits.length) {
            throw new IndexOutOfBoundsException(
                String.format("Bandit index %d out of bounds [0, %d)",
                            banditIndex, bandits.length)
            );
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("KArmedBanditsModel[\n");
        for (int i = 0; i < bandits.length; i++) {
            sb.append(String.format("  %d: %s\n", i, bandits[i]));
        }
        sb.append("]");
        return sb.toString();
    }
}