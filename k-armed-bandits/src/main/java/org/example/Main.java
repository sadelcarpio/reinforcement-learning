package org.example;

import org.example.algorithms.epsilongreedy.EpsilonGreedy;
import org.example.algorithms.ucb.UpperConfidenceBound;
import org.example.model.KArmedBanditsModel;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        int k = 10;
        KArmedBanditsModel model = new KArmedBanditsModel(k);
        UpperConfidenceBound ucbMethod = new UpperConfidenceBound(model, 0.1, 1);
        double[] q = ucbMethod.run(2000, 0.0);
        System.out.println(Arrays.toString(model.bandits));
        System.out.println(Arrays.toString(q));
    }
}
