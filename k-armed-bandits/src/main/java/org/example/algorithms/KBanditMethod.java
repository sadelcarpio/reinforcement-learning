package org.example.algorithms;

import org.example.model.KArmedBanditsModel;
import org.example.tester.KBanditTester;

public class KBanditMethod {
    private final KArmedBanditsModel model;
    private final QOptimizer optimizer;

    public KBanditMethod(KArmedBanditsModel model, QOptimizer optimizer) {
        this.model = model;
        this.optimizer = optimizer;
    }

    public double[] run(int steps, double initialQ) {
        optimizer.init(initialQ, model.k);
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = optimizer.selectAction();
            reward = model.getReward(a);
            optimizer.update(a, reward);
        }
        return optimizer.q;
    }

    public double[] run(int steps, double initialQ, KBanditTester tester) {
        optimizer.init(initialQ, model.k);
        tester.init(model.bandits, steps);
        double reward;
        for (int i = 0; i < steps; i++) {
            int a = optimizer.selectAction();
            tester.isOptimal(a, i);
            reward = model.getReward(a);
            optimizer.update(a, reward);
        }
        return optimizer.q;
    }
}
