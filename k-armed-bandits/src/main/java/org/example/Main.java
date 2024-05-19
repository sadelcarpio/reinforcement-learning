package org.example;

import org.example.algorithms.KBanditMethod;
import org.example.algorithms.epsilongreedy.EpsilonGreedy;
import org.example.algorithms.gradientbandit.GradientBandit;
import org.example.algorithms.ucb.UpperConfidenceBound;
import org.example.model.KArmedBanditsModel;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        int k = 10;
        KArmedBanditsModel model = new KArmedBanditsModel(k);
        UpperConfidenceBound ucbOptimizer = new UpperConfidenceBound(0.1, 1);
        EpsilonGreedy epsilonGreedyOptimizer = new EpsilonGreedy(0.1);
        GradientBandit gradientBanditOptimizer = new GradientBandit(0.1);
        KBanditMethod method = new KBanditMethod(model, gradientBanditOptimizer);
        double[] q = method.run(2000, 0.0);
        System.out.println(Arrays.toString(model.bandits));
        System.out.println(Arrays.toString(q));
    }
}
