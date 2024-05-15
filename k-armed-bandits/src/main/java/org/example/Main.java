package org.example;

import org.example.epsilongreedy.EpsilonGreedy;
import org.example.model.KArmedBanditsModel;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        int k = 10;
        KArmedBanditsModel model = new KArmedBanditsModel(k);
        EpsilonGreedy eGreedy = new EpsilonGreedy(model, 0.1);
        ArrayList<Double> q = eGreedy.run(10000);
        System.out.println(model.bandits);
        System.out.println(q);
    }
}
