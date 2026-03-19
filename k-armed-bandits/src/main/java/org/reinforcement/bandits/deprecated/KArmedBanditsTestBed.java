package org.reinforcement.bandits.deprecated;

import org.reinforcement.bandits.deprecated.algorithms.KBanditMethod;
import org.reinforcement.bandits.deprecated.algorithms.gradientbandit.GradientBandit;
import org.reinforcement.bandits.deprecated.model.KArmedBanditsModel;
import org.reinforcement.bandits.deprecated.tester.KBanditTester;

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
