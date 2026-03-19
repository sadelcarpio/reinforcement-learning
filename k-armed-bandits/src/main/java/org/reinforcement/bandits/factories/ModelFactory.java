package org.reinforcement.bandits.factories;

import org.reinforcement.bandits.domain.IBandit;
import org.reinforcement.bandits.domain.IBanditModel;
import org.reinforcement.bandits.domain.KArmedBanditsModel;

/**
 * Utility class for creating bandit models with a specified factory.
 */
public class ModelFactory {
    /**
     * Create a k-armed bandit model using the specified factory.
     *
     * @param k number of bandits to create
     * @param banditFactory factory for creating individual bandits
     * @return a new bandit model
     */
    public static IBanditModel createModel(int k, IBanditFactory banditFactory) {
        if (k <= 0) {
            throw new IllegalArgumentException("Number of bandits must be positive");
        }
        if (banditFactory == null) {
            throw new IllegalArgumentException("Bandit factory must not be null");
        }

        IBandit[] bandits = new IBandit[k];
        for (int i = 0; i < k; i++) {
            bandits[i] = banditFactory.createBandit();
        }

        return new KArmedBanditsModel(bandits);
    }
}