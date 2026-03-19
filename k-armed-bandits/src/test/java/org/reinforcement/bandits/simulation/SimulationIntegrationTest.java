package org.reinforcement.bandits.simulation;

import org.junit.Test;
import org.reinforcement.bandits.observers.OptimalActionTracker;
import org.reinforcement.bandits.observers.RewardTracker;

import static org.junit.Assert.*;

public class SimulationIntegrationTest {

    @Test
    public void testEpsilonGreedySimulation() {
        Object simObj = new SimulationBuilder()
            .withSeed(42)
            .withNumBandits(10)
            .withStationaryBandits()
            .withEpsilonGreedy(0.1)
            .withSampleAverageUpdate()
            .withInitialQ(0.0)
            .build();

        assertTrue(simObj instanceof BanditSimulation);
        BanditSimulation simulation = (BanditSimulation) simObj;

        OptimalActionTracker tracker = new OptimalActionTracker(
            simulation.getModel(), 1000
        );
        simulation.addObserver(tracker);

        SimulationResult result = simulation.run(1000);

        assertNotNull(result);
        assertEquals(10, result.getQValues().length);
        assertTrue(tracker.getOptimalActionPercentage() > 0.5); // Should learn to choose optimal > 50% of time
    }

    @Test
    public void testUCBSimulation() {
        Object simObj = new SimulationBuilder()
            .withSeed(123)
            .withNumBandits(5)
            .withStationaryBandits()
            .withUCB(2.0)
            .withSampleAverageUpdate()
            .build();

        assertTrue(simObj instanceof BanditSimulation);
        BanditSimulation simulation = (BanditSimulation) simObj;

        RewardTracker rewardTracker = new RewardTracker(500);
        simulation.addObserver(rewardTracker);

        simulation.run(500);

        assertTrue(rewardTracker.getTotalReward() != 0);
        assertTrue(rewardTracker.getAverageReward() > -10); // Sanity check
    }

    @Test
    public void testGradientBanditSimulation() {
        Object simObj = new SimulationBuilder()
            .withSeed(999)
            .withNumBandits(10)
            .withStationaryBandits()
            .withGradientBandit(0.1)
            .build();

        assertTrue(simObj instanceof GradientBanditSimulation);
        GradientBanditSimulation simulation = (GradientBanditSimulation) simObj;

        OptimalActionTracker tracker = new OptimalActionTracker(
            simulation.getModel(), 2000
        );
        simulation.addObserver(tracker);

        SimulationResult result = simulation.run(2000);

        assertNotNull(result);
        assertTrue(tracker.getOptimalActionPercentage() > 0.3); // Should learn somewhat
    }

    @Test
    public void testNonStationarySimulation() {
        Object simObj = new SimulationBuilder()
            .withSeed(456)
            .withNumBandits(10)
            .withNonStationaryBandits(0.01) // Small drift
            .withEpsilonGreedy(0.1)
            .withConstantStepUpdate(0.1) // Important for non-stationary
            .build();

        assertTrue(simObj instanceof BanditSimulation);
        BanditSimulation simulation = (BanditSimulation) simObj;

        SimulationResult result = simulation.run(1000);

        assertNotNull(result);
        assertEquals(10, result.getQValues().length);
    }

    @Test
    public void testSimulationWithMultipleObservers() {
        BanditSimulation simulation = (BanditSimulation) new SimulationBuilder()
            .withSeed(789)
            .withNumBandits(5)
            .withStationaryBandits()
            .withEpsilonGreedy(0.1)
            .withSampleAverageUpdate()
            .build();

        OptimalActionTracker optimalTracker = new OptimalActionTracker(
            simulation.getModel(), 500
        );
        RewardTracker rewardTracker = new RewardTracker(500);

        simulation.addObserver(optimalTracker);
        simulation.addObserver(rewardTracker);

        simulation.run(500);

        assertNotNull(optimalTracker.getWasOptimal());
        assertTrue(rewardTracker.getTotalReward() != 0);
    }
}