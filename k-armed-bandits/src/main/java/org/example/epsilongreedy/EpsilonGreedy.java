package org.example.epsilongreedy;

import org.example.model.KArmedBanditsModel;

import java.util.ArrayList;
import java.util.Random;

public record EpsilonGreedy(KArmedBanditsModel model, double epsilon) {
    private static final Random rand = new Random();

    public double[] run(int steps) {
        double[] q = new double[model.k];
        int[] n = new int[model.k];
        for (int i = 0; i < model.k; i++) {
            q[i] = 0.0;
        }
        for (int j = 0; j < model.k; j++) {
            n[j] = 0;
        }
        double reward;
        for (int i = 0; i < steps; i++) {
            int a;
            if (rand.nextDouble() < epsilon) {
                a = rand.nextInt(0, model.k);
            } else {
                ArrayList<Integer> argmax_a = getArgmax(q);
                a = argmax_a.get((rand.nextInt(0, argmax_a.size())));
            }
            reward = model.pull_lever(a);
            n[a] += 1;
            q[a] = q[a] + (1. / n[a]) * (reward - q[a]);
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
