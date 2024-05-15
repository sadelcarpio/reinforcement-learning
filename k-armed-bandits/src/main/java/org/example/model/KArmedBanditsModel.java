package org.example.model;

public class KArmedBanditsModel {
    public final int k;
    public final Bandit[] bandits;

    /**
     * @param k number of bandits
     */
    public KArmedBanditsModel(int k) {
        this.k = k;
        bandits = new Bandit[k];
        for (int i = 0; i < k; i++) {
            bandits[i] = new Bandit();
        }
    }

    public double pull_lever(int i) {
        return bandits[i].pull_lever();
    }
}
