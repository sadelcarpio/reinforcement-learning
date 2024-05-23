package org.example;

import org.example.algorithms.KBanditMethod;
import org.example.algorithms.epsilongreedy.EpsilonGreedy;
import org.example.algorithms.epsilongreedy.NonStationaryEpsilonGreedy;
import org.example.algorithms.gradientbandit.GradientBandit;
import org.example.algorithms.ucb.UpperConfidenceBound;
import org.example.model.KArmedBanditsModel;
import org.example.tester.KBanditTester;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        int k = 10;
        KArmedBanditsModel model = new KArmedBanditsModel(k, false);
        UpperConfidenceBound ucbOptimizer = new UpperConfidenceBound(0.1, 1);
        EpsilonGreedy epsilonGreedyOptimizer = new EpsilonGreedy(0.1);
        NonStationaryEpsilonGreedy nonStatOptimizer = new NonStationaryEpsilonGreedy(0.1, 0.1);
        GradientBandit gradientBanditOptimizer = new GradientBandit(0.1);
        KBanditMethod method = new KBanditMethod(model, nonStatOptimizer);
        double[] q = method.run(2000, 0.0);
        System.out.println(Arrays.toString(model.bandits));
        System.out.println(Arrays.toString(q));
        System.out.println("Testing optimal action taken");
        KArmedBanditsModel testModel = new KArmedBanditsModel(k, true);
        EpsilonGreedy testOptimizer = new EpsilonGreedy(0.0);
        KBanditMethod testMethod = new KBanditMethod(testModel, testOptimizer);
        KBanditTester tester = new KBanditTester();
        double[] testQ = testMethod.run(100, 0.0, tester);
        System.out.println(Arrays.toString(testModel.bandits));
        System.out.println(Arrays.toString(testQ));
        System.out.println(Arrays.toString(tester.wasOptimal));
    }
}
