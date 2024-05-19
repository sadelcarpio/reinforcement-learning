package org.example.algorithms;

import java.util.Arrays;
import java.util.Random;

public abstract class QOptimizer {
    public static final Random rand = new Random();
    protected int[] n;
    protected int k;
    public double[] q;

    public void update(int a, double reward) {
        n[a] += 1;
        q[a] = q[a] + (1. / n[a]) * (reward - q[a]);
    }

    public void init(double initialQ, int k) {
        this.k = k;
        this.q = new double[k];
        this.n = new int[k];
        Arrays.fill(n, 0);
        Arrays.fill(q, initialQ);
    }

    public abstract int selectAction();
}
