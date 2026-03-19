package org.reinforcement.bandits.observers;

/**
 * Tracks rewards received at each step of the simulation.
 */
public class RewardTracker implements ISimulationObserver {
    private final double[] rewards;
    private double totalReward;
    private double averageReward;
    private int stepCount;

    /**
     * Create a reward tracker.
     *
     * @param numSteps the number of steps in the simulation
     */
    public RewardTracker(int numSteps) {
        this.rewards = new double[numSteps];
        this.totalReward = 0.0;
        this.averageReward = 0.0;
        this.stepCount = 0;
    }

    @Override
    public void onStepComplete(int step, int selectedAction, double reward) {
        rewards[step] = reward;
        totalReward += reward;
        stepCount++;
        averageReward = totalReward / stepCount;
    }

    @Override
    public void onSimulationComplete() {
        // Optional: could log final statistics here
    }

    /**
     * Get a copy of all rewards received.
     *
     * @return array of rewards
     */
    public double[] getRewards() {
        return rewards.clone();
    }

    /**
     * Get the total reward accumulated.
     *
     * @return total reward
     */
    public double getTotalReward() {
        return totalReward;
    }

    /**
     * Get the average reward per step.
     *
     * @return average reward
     */
    public double getAverageReward() {
        return averageReward;
    }
}