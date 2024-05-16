package org.example;

import org.example.epsilongreedy.EpsilonGreedy;
import org.example.model.KArmedBanditsModel;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        int k = 10;
        KArmedBanditsModel model = new KArmedBanditsModel(k);
        EpsilonGreedy eGreedy = new EpsilonGreedy(model, 0.01);
        double[] q = eGreedy.run(2000);
        System.out.println(Arrays.toString(model.bandits));
        System.out.println(Arrays.toString(q));
    }
}
