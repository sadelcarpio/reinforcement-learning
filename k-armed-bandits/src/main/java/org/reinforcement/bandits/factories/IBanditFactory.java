package org.reinforcement.bandits.factories;

import org.reinforcement.bandits.domain.IBandit;

/**
 * Factory interface for creating bandit instances.
 * Allows different bandit types to be created without modifying client code.
 */
public interface IBanditFactory {
    /**
     * Create a new bandit instance.
     *
     * @return a new bandit
     */
    IBandit createBandit();
}