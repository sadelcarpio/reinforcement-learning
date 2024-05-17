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

    private void updateQ(int a, double reward, double step_size) {
        q[a] = q[a] + step_size * (reward - q[a]);
    }

    private void initQ(double initial_q) {
        for (int i = 0; i < model.k; i++) {
            q[i] = initial_q;
        }
    }

    private int selectAction() {
        if (rand.nextDouble() < epsilon) {
                return rand.nextInt(0, model.k);
            } else {
                ArrayList<Integer> argmax_a = getArgmax(q);
                return argmax_a.get((rand.nextInt(0, argmax_a.size())));
            }
    }

    public double[] run(int steps, double initial_q) {
        initQ(initial_q);
        int[] n = new int[model.k];
        for (int j = 0; j < model.k; j++) {
            n[j] = 0;
        }
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = selectAction();
            reward = model.pull_lever(a);
            n[a] += 1;
            updateQ(a, reward, 1. / n[a]);
        }
        return q;
    }

    public double[] run(int steps, double initial_q, double step_size) {
        initQ(initial_q);
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = selectAction();
            reward = model.pull_lever(a);
            updateQ(a, reward, step_size);
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
