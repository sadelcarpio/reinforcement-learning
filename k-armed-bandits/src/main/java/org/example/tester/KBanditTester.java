package org.example.tester;

import org.example.algorithms.KBanditMethod;
import org.example.model.Bandit;
import org.example.utils.ArrayUtils;

import java.util.Arrays;

public class KBanditTester {
    public int optimalAction;
    public boolean[] wasOptimal;

    public void init(Bandit[] bandits, int steps) {
        int argMax = 0;
        for (int i = 0; i < bandits.length; i++) {
            if (bandits[i].expected_reward > bandits[argMax].expected_reward) {
                argMax = i;
            }
        }
        this.optimalAction = argMax;
        this.wasOptimal = new boolean[steps];
    }

    public void isOptimal(int a, int i) {
        wasOptimal[i] = a == optimalAction;
    }

    public double[] fullTest(KBanditMethod method, int steps, double initialQ, int nTrials) {
        boolean[][] wasOptimalTrials = new boolean[steps][nTrials];
        for (int i = 0; i < nTrials; i++) {
            method.run(steps, initialQ, this);
            for (int j = 0; j < steps; j++) {
                wasOptimalTrials[j][i] = this.wasOptimal[j];
            }
        }
        return Arrays.stream(wasOptimalTrials).mapToDouble(ArrayUtils::trueRatio).toArray();
    }
}
