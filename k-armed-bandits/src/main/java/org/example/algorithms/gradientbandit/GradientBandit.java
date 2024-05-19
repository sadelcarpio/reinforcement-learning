package org.example.algorithms.gradientbandit;

import org.example.algorithms.QOptimizer;
import org.example.utils.ArrayUtils;

import java.util.Arrays;

public class GradientBandit extends QOptimizer {
    private final double stepSize;
    private double[] h;
    private double[] pi;

    public GradientBandit(double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public void update(int a, double reward) {
        n[a] += 1;
        for (int i = 0; i < k; i++) {
            pi[i] = Math.exp(h[i]) / (Arrays.stream(h).map(Math::exp).sum());
            if (i == a) h[i] = h[i] + stepSize * (reward - q[i]) * (1 - pi[i]);
            else h[i] = h[i] - stepSize * (reward - q[i]) * pi[i];
        }
        q[a] = q[a] + (1. / n[a]) * (reward - q[a]);  // q is mean reward over time
    }

    @Override
    public void init(double initialQ, int k) {
        super.init(initialQ, k);
        this.h = new double[k];
        this.pi = new double[k];
        Arrays.fill(h, 0.0);
        Arrays.fill(pi, 1. / k);
    }

    @Override
    public int selectAction() {
        return ArrayUtils.randomIndexWithWeightedProb(pi, rand);
    }
}
