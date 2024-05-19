package org.example.model;

public class KArmedBanditsModel {
    public final int k;
    public final Bandit[] bandits;

    public KArmedBanditsModel(int k) {
        this.k = k;
        bandits = new Bandit[k];
        for (int i = 0; i < k; i++) {
            bandits[i] = new Bandit();
        }
    }

    public KArmedBanditsModel(int k, boolean stationary) {
        this.k = k;
        bandits = new Bandit[k];
        for (int i = 0; i < k; i++) {
            if (stationary) bandits[i] = new Bandit();
            else bandits[i] = new NonStationaryBandit();
        }
    }

    public double getReward(int i) {
        return bandits[i].getReward();
    }
}
