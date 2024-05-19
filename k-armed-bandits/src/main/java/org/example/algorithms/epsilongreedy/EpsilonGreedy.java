package org.example.algorithms.epsilongreedy;

import org.example.algorithms.QOptimizer;
import org.example.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class EpsilonGreedy extends QOptimizer {
    private int[] n;
    private final double epsilon;

    public EpsilonGreedy(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public void init(double initialQ, int k) {
        this.k = k;
        this.q = new double[k];
        this.n = new int[k];
        Arrays.fill(n, 0);
        Arrays.fill(q, initialQ);
    }

    @Override
    public void update(int a, double reward) {
        n[a] += 1;
        q[a] = q[a] + (1. / n[a]) * (reward - q[a]);
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
