package org.example.algorithms.ucb;

import org.example.algorithms.QOptimizer;
import org.example.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class UpperConfidenceBound extends QOptimizer {
    private double[] ucb;
    private final double epsilon, c;

    public UpperConfidenceBound(double epsilon, double c) {
        this.epsilon = epsilon;
        this.c = c;
    }

    @Override
    public void update(int a, double reward) {
        super.update(a, reward);
        for (int i = 0; i < k; i++) {
            if (n[i] != 0) ucb[i] = q[i] + c * Math.sqrt(Math.log(n[i]) / n[i]);
        }
    }

    @Override
    public void init(double initialQ, int k) {
        super.init(initialQ, k);
        this.ucb = new double[k];
        Arrays.fill(ucb, initialQ);
    }

    @Override
    public int selectAction() {
        if (rand.nextDouble() < epsilon) {
            return rand.nextInt(0, k);
        } else {
            ArrayList<Integer> argmaxA = ArrayUtils.getArgmax(ucb);
            return argmaxA.get((rand.nextInt(0, argmaxA.size())));
        }
    }
}
