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
    public void update(int a, double reward) {
        n[a] += 1;
        q[a] = q[a] + stepSize * (reward - q[a]);
    }
}
