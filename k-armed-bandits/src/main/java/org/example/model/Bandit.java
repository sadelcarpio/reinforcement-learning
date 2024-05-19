package org.example.model;

import java.util.Random;

public class Bandit {
    protected static final Random rand = new Random();
    public double expected_reward;

    public Bandit() {
        this.expected_reward = rand.nextGaussian();
    }

    public double getReward() {
        return rand.nextGaussian(this.expected_reward, 1);
    }

    @Override
    public String toString() {
        return String.format("%.2f", this.expected_reward);
    }
}
