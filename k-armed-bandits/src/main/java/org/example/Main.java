package org.example;

import org.example.epsilongreedy.EpsilonGreedy;
import org.example.model.KArmedBanditsModel;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        int k = 10;
        KArmedBanditsModel model = new KArmedBanditsModel(k);
        EpsilonGreedy eGreedy = new EpsilonGreedy(model, 0.1);
        // Note: using a fixed step size does not consider Q as the mean reward per action anymore. It gives more
        // importance to most recent rewards and thus may be higher than the actual q
        double[] q = eGreedy.run(2000, 5, 0.01);
        System.out.println(Arrays.toString(model.bandits));
        System.out.println(Arrays.toString(q));
    }
}
