package org.example.algorithms.ucb;

import org.example.algorithms.KBanditMethod;
import org.example.model.KArmedBanditsModel;
import org.example.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Random;

public class UpperConfidenceBound implements KBanditMethod {
    private static final Random rand = new Random();
    private final double[] q, ucb;
    private final int[] n;
    private final double epsilon, c;
    private final KArmedBanditsModel model;

    public UpperConfidenceBound(KArmedBanditsModel model, double epsilon, double c) {
        q = new double[model.k];
        ucb = new double[model.k];
        this.n = new int[model.k];
        this.model = model;
        this.epsilon = epsilon;
        this.c = c;
        for (int j = 0; j < model.k; j++) {
            n[j] = 0;
        }
    }

    private void updateQ(int a, double reward, double stepSize) {
        q[a] = q[a] + stepSize * (reward - q[a]);
    }

    private void initQ(double initialQ) {
        for (int a = 0; a < model.k; a++) {
            q[a] = initialQ;
        }
    }

    private void initUcb() {
        System.arraycopy(q, 0, ucb, 0, model.k);
    }

    private int selectAction() {
        if (rand.nextDouble() < epsilon) {
            return rand.nextInt(0, model.k);
        } else {
            ArrayList<Integer> argmaxA = ArrayUtils.getArgmax(ucb);
            return argmaxA.get((rand.nextInt(0, argmaxA.size())));
        }
    }

    private void updateUcb() {
        for (int i = 0; i < model.k; i++) {
            ucb[i] = q[i] + c * Math.sqrt(Math.log(n[i]) / n[i]);
        }
    }

    @Override
    public double[] run(int steps, double initialQ) {
        initQ(initialQ);
        initUcb();
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = selectAction();
            n[a] += 1;
            updateUcb();
            reward = model.getReward(a);
            updateQ(a, reward, 1. / n[a]);
        }
        return q;
    }

    @Override
    public double[] run(int steps, double initialQ, double stepSize) {
        initQ(initialQ);
        initUcb();
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = selectAction();
            n[a] += 1;
            updateUcb();
            reward = model.getReward(a);
            updateQ(a, reward, stepSize);
        }
        return q;
    }
}
