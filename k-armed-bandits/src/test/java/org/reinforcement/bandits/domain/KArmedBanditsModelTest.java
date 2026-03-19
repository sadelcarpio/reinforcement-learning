package org.reinforcement.bandits.domain;

import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

public class KArmedBanditsModelTest {

    @Test
    public void testModelCreation() {
        Random random = new Random(42);
        IBandit[] bandits = new IBandit[5];
        for (int i = 0; i < bandits.length; i++) {
            bandits[i] = new Bandit(i * 0.5, 1.0, random);
        }

        KArmedBanditsModel model = new KArmedBanditsModel(bandits);

        assertEquals(5, model.getNumberOfBandits());
    }

    @Test
    public void testGetReward() {
        Random random = new Random(42);
        IBandit[] bandits = new IBandit[3];
        bandits[0] = new Bandit(1.0, 0.1, random);
        bandits[1] = new Bandit(2.0, 0.1, random);
        bandits[2] = new Bandit(3.0, 0.1, random);

        KArmedBanditsModel model = new KArmedBanditsModel(bandits);

        // Getting reward should not throw exception
        double reward = model.getReward(1);
        assertTrue(reward > 0);
    }

    @Test
    public void testGetOptimalBanditIndex() {
        Random random = new Random();
        IBandit[] bandits = new IBandit[4];
        bandits[0] = new Bandit(1.0, 1.0, random);
        bandits[1] = new Bandit(3.5, 1.0, random); // Optimal
        bandits[2] = new Bandit(2.0, 1.0, random);
        bandits[3] = new Bandit(-1.0, 1.0, random);

        KArmedBanditsModel model = new KArmedBanditsModel(bandits);

        assertEquals(1, model.getOptimalBanditIndex());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidBanditIndex() {
        Random random = new Random();
        IBandit[] bandits = new IBandit[3];
        for (int i = 0; i < bandits.length; i++) {
            bandits[i] = new Bandit(i, 1.0, random);
        }

        KArmedBanditsModel model = new KArmedBanditsModel(bandits);
        model.getReward(5); // Should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBanditsArray() {
        new KArmedBanditsModel(new IBandit[0]);
    }
}