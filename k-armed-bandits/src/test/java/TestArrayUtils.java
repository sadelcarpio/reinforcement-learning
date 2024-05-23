import org.example.utils.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestArrayUtils {

    @Test
    public void testGetArgMax() {
        double[] testList = new double[]{1., 2., 3., 4.};
        double[] testList2 = new double[]{1., 3., 1., 3., 4., 2., 4.};
        List<Integer> argmax = ArrayUtils.getArgmax(testList);
        List<Integer> argmax2 = ArrayUtils.getArgmax(testList2);
        assertEquals(List.of(3), argmax);
        assertEquals(Arrays.asList(4, 6), argmax2);
    }

    @Test
    public void testTrueRatio() {
        boolean[][] testArrays = new boolean[][] {{true, true, false},
                {false, false, true, false},
                {false, false, false, false},
                {true, true, true, true, true}};
        double[] expectedRatios = new double[]{2./3, 0.25, 0., 1.};
        for (int i = 0; i < 4; i++) {
            double trueRatio = ArrayUtils.trueRatio(testArrays[i]);
            assertEquals(expectedRatios[i], trueRatio, 0.001);
        }
    }
}