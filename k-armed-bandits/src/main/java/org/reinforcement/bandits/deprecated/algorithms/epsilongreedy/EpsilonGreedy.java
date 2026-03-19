package org.reinforcement.bandits.deprecated.algorithms.epsilongreedy;

import org.reinforcement.bandits.deprecated.algorithms.QOptimizer;
import org.reinforcement.bandits.deprecated.utils.ArrayUtils;

import java.util.ArrayList;

public class EpsilonGreedy extends QOptimizer {
    protected final double epsilon;

    public EpsilonGreedy(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public void init(double initialQ, int k) {
        super.init(initialQ, k);
    }

    @Override
    public void update(int a, double reward) {
        super.update(a, reward);
    }

    @Override
    public int selectAction() {
        if (rand.nextDouble() < epsilon) {
            return rand.nextInt(0, k);
        } else {
            ArrayList<Integer> argmaxA = ArrayUtils.getArgmax(q);
            return argmaxA.get((rand.nextInt(0, argmaxA.size())));
        }
    }
}
