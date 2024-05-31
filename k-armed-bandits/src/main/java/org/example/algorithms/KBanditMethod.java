package org.example.algorithms;

import org.example.model.KArmedBanditsModel;
import org.example.tester.KBanditTester;

/**
 * General K Bandit Method,
 */
public class KBanditMethod {
    private final KArmedBanditsModel model;
    private final QOptimizer optimizer;

    /**
     * Initializes a method
     * @param model The K Bandits model, each bandit with a given distribution of rewards
     * @param optimizer The specific optimizer whose objective is to determine the best option from the model
     */
    public KBanditMethod(KArmedBanditsModel model, QOptimizer optimizer) {
        this.model = model;
        this.optimizer = optimizer;
    }

    /**
     * Runs the method for a number of time steps.
     * @param steps number of time steps. The more time steps the more possibility of getting to choose the best option
     *              everytime (ideally).
     * @param initialQ Initial assumption for the Q estimate.
     * @return The value of the q estimate for each action.
     */
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

    /**
     * Runs the method with a tester that logs the choices made.
     * @param steps number of time steps. The more time steps the more possibility of getting to choose the best option
     *              everytime (ideally).
     * @param initialQ Initial assumption for the Q estimate.
     * @param tester Tester that logs each choice at each step.
     * @return The value of the q estimate for each action.
     */
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
