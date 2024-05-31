package org.example.tester;

import org.example.algorithms.KBanditMethod;
import org.example.model.Bandit;
import org.example.utils.ArrayUtils;

import java.util.Arrays;

/**
 * Tests different KBandits methods over a number of time steps, a given number of trials.
 */
public class KBanditTester {
    public int optimalAction;
    public boolean[] wasOptimal;

    /**
     * Initialize Tester inside KBandit Method. Gets the optimal choice to compare on every time step.
     * @param bandits bandits which one of them contains the optimal choice
     * @param steps number of time steps the method is using
     */
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

    /**
     * Based on the action chosen determine if it was the optimal action.
     * @param a action chosen by the method
     * @param i time step
     */
    public void isOptimal(int a, int i) {
        wasOptimal[i] = a == optimalAction;
    }

    /**
     * Test a given Method.
     * @param method Method to use
     * @param steps number of time steps for each trial
     * @param initialQ Initial assumption of Q estimate
     * @param nTrials number of times the experiment will repeat
     * @return Array of probabilities of choosing the optimal action at each time step, based on the number of trials.
     */
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
