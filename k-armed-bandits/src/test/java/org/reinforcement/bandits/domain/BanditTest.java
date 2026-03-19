package org.reinforcement.bandits.domain;

import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

public class BanditTest {

    @Test
    public void testBanditReturnsRewardsAroundExpectedValue() {
        Random random = new Random(42);
        double expectedReward = 1.5;
        double variance = 1.0;
        Bandit bandit = new Bandit(expectedReward, variance, random);

        // Pull the bandit many times and check average
        int numPulls = 10000;
        double sum = 0.0;
        for (int i = 0; i < numPulls; i++) {
            sum += bandit.getReward();
        }
        double average = sum / numPulls;

        // Average should be close to expected reward (within margin of error)
        assertEquals(expectedReward, average, 0.1);
    }

    @Test
    public void testGetExpectedReward() {
        Random random = new Random();
        double expectedReward = 2.5;
        Bandit bandit = new Bandit(expectedReward, 1.0, random);

        assertEquals(expectedReward, bandit.getExpectedReward(), 0.0001);
    }

    @Test
    public void testNonStationaryBanditDrifts() {
        Random random = new Random(42);
        double initialExpectedReward = 0.0;
        double variance = 1.0;
        double driftRate = 1.0;
        NonStationaryBandit bandit = new NonStationaryBandit(
            initialExpectedReward, variance, driftRate, random
        );

        double initialExpected = bandit.getExpectedReward();

        // Pull the bandit many times
        for (int i = 0; i < 100; i++) {
            bandit.getReward();
        }

        double finalExpected = bandit.getExpectedReward();

        // Expected reward should have drifted from initial value
        assertNotEquals(initialExpected, finalExpected, 0.0001);
    }
}