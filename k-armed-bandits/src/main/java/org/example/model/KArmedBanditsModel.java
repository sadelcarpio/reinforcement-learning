package org.example.model;

import java.util.ArrayList;

public class KArmedBanditsModel {
    public final int k;
    public final ArrayList<Bandit> bandits;

    /**
     * @param k number of bandits
     */
    public KArmedBanditsModel(int k) {
        this.k = k;
        bandits = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            bandits.add(i, new Bandit());
        }
    }

    public double pull_lever(int i) {
        return bandits.get(i).pull_lever();
    }
}
