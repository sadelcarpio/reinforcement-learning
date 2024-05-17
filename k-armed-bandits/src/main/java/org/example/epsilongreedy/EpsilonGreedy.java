package org.example.epsilongreedy;

import org.example.model.KArmedBanditsModel;

import java.util.ArrayList;
import java.util.Random;

public class EpsilonGreedy {
    private static final Random rand = new Random();
    private final double[] q;
    private final double epsilon;
    private final KArmedBanditsModel model;


    public EpsilonGreedy(KArmedBanditsModel model, double epsilon) {
        q = new double[model.k];
        this.model = model;
        this.epsilon = epsilon;
    }

    private void updateQ(int a, double reward, double stepSize) {
        q[a] = q[a] + stepSize * (reward - q[a]);
    }

    private void initQ(double initialQ) {
        for (int i = 0; i < model.k; i++) {
            q[i] = initialQ;
        }
    }

    private int selectAction() {
        if (rand.nextDouble() < epsilon) {
                return rand.nextInt(0, model.k);
            } else {
                ArrayList<Integer> argmaxA = getArgmax(q);
                return argmaxA.get((rand.nextInt(0, argmaxA.size())));
            }
    }

    public double[] run(int steps, double initialQ) {
        initQ(initialQ);
        int[] n = new int[model.k];
        for (int j = 0; j < model.k; j++) {
            n[j] = 0;
        }
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = selectAction();
            reward = model.getReward(a);
            n[a] += 1;
            updateQ(a, reward, 1. / n[a]);
        }
        return q;
    }

    public double[] run(int steps, double initialQ, double stepSize) {
        initQ(initialQ);
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = selectAction();
            reward = model.getReward(a);
            updateQ(a, reward, stepSize);
        }
        return q;
    }

    public static ArrayList<Integer> getArgmax(double[] q) {
        ArrayList<Integer> argmax_a = new ArrayList<>();
        int k = q.length;
        double max = q[0];
        for (int j = 0; j < k; j++) {
            double q_a = q[j];
            if (q_a > max) {
                max = q_a;
                argmax_a.clear();
                argmax_a.add(j);
            } else if (Double.compare(q_a, max) == 0) {
                argmax_a.add(j);
            }
        }
        return argmax_a;
    }
}
