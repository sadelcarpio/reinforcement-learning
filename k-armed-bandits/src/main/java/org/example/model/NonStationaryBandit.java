package org.example.model;

public class NonStationaryBandit extends Bandit{
    @Override
    public double getReward() {
        expected_reward = rand.nextGaussian(expected_reward, 1);
        return expected_reward;
    }
}
