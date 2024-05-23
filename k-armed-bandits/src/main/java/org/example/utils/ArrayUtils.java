package org.example.utils;

import java.util.ArrayList;
import java.util.Random;

public class ArrayUtils {
    public static ArrayList<Integer> getArgmax(double[] q) {
        ArrayList<Integer> argmax_a = new ArrayList<>();
        int k = q.length;
        double max = q[0];
        for (int j = 0; j < k; j++) {
            double q_a = q[j];
            if (q_a > max) {
                max = q_a;
                argmax_a.clear();
                argmax_a.add(j);
            } else if (Double.compare(q_a, max) == 0) {
                argmax_a.add(j);
            }
        }
        return argmax_a;
    }

    public static int randomIndexWithWeightedProb(double[] p, Random rand) {
        int n = p.length;
        double[] cumulativeProbs = new double[n];
        cumulativeProbs[0] = p[0];
        for (int i = 1; i < n; i++) {
            cumulativeProbs[i] = cumulativeProbs[i - 1] + p[i];
        }
        double randomNumber = rand.nextDouble() * cumulativeProbs[n - 1];
        for (int i = 0; i < n; i++) {
            if (randomNumber <= cumulativeProbs[i]) return i;
        }
        return -1;  // basically an error
    }

    public static double trueRatio(boolean[] arr) {
        int trues = 0;
        for (boolean b : arr) {
            if (b) trues++;
        }
        return (double) trues / arr.length;
    }
}
