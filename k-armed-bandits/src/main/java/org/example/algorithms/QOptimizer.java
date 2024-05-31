package org.example.algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 * Base Class for KBandit Optimizers
 */
public abstract class QOptimizer {
    public static final Random rand = new Random();
    protected int[] n;
    protected int k;
    public double[] q;

    /**
     * Update q values given the action taken
     * @param a index of the action taken ("a"th bandit)
     * @param reward The reward yielded by choosing that action
     */
    public void update(int a, double reward) {
        n[a] += 1;
        q[a] = q[a] + (1. / n[a]) * (reward - q[a]);
    }

    /**
     * Initialize the optimizer
     * @param initialQ Initial assumption of the q estimate.
     * @param k Number of bandits of the model
     */
    public void init(double initialQ, int k) {
        this.k = k;
        this.q = new double[k];
        this.n = new int[k];
        Arrays.fill(n, 0);
        Arrays.fill(q, initialQ);
    }

    public abstract int selectAction();
}
