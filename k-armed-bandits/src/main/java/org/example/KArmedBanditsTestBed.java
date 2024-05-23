package org.example;

import org.example.algorithms.KBanditMethod;
import org.example.algorithms.epsilongreedy.EpsilonGreedy;
import org.example.algorithms.gradientbandit.GradientBandit;
import org.example.algorithms.ucb.UpperConfidenceBound;
import org.example.model.KArmedBanditsModel;
import org.example.tester.KBanditTester;

import java.util.Arrays;

public class KArmedBanditsTestBed {
    public static void main(String[] args) {
        int k = 10;
        KBanditTester tester = new KBanditTester();
        KArmedBanditsModel model = new KArmedBanditsModel(k);
        GradientBandit optimizer = new GradientBandit(0.1);
        KBanditMethod method = new KBanditMethod(model, optimizer);
        System.out.println(Arrays.toString(model.bandits));
        double[] optimalChoiceProb = tester.fullTest(method, 1000, 0.0, 2000);
        System.out.println(Arrays.toString(optimalChoiceProb));
    }
}
