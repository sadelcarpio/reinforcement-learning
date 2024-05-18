package org.example.algorithms;

public interface KBanditMethod {
    double[] run(int steps, double initialQ);
    double[] run(int steps, double initialQ, double stepSize);
}
