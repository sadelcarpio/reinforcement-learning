package org.example.algorithms.epsilongreedy;

import org.example.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class NonStationaryEpsilonGreedy extends EpsilonGreedy{
    private final double stepSize;
    private double[] qNonStat;

    public NonStationaryEpsilonGreedy(double epsilon, double stepSize) {
        super(epsilon);
        this.stepSize = stepSize;
    }

    @Override
    public void init(double initialQ, int k) {
        super.init(initialQ, k);
        this.qNonStat = new double[k];
        Arrays.fill(qNonStat, initialQ);
    }

    @Override
    public void update(int a, double reward) {
        super.update(a, reward);
        qNonStat[a] = qNonStat[a] + stepSize * (reward - qNonStat[a]);
    }

    @Override
    public int selectAction() {
        if (rand.nextDouble() < epsilon) {
            return rand.nextInt(0, k);
        } else {
            ArrayList<Integer> argmaxA = ArrayUtils.getArgmax(qNonStat);
            return argmaxA.get((rand.nextInt(0, argmaxA.size())));
        }
    }
}
