package org.example.algorithms;

import java.util.Random;

public abstract class QOptimizer {
    public static final Random rand = new Random();
    protected int k;
    public double[] q;

    public abstract void update(int a, double reward);

    public abstract void init(double initialQ, int k);

    public abstract int selectAction();
}
