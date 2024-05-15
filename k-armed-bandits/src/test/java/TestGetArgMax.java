import org.example.epsilongreedy.EpsilonGreedy;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestGetArgMax {

    @Test
    public void testAdd() {
        double[] testList = new double[]{1., 2., 3., 4.};
        double[] testList2 = new double[]{1., 3., 1., 3., 4., 2., 4.};
        List<Integer> argmax = EpsilonGreedy.getArgmax(testList);
        List<Integer> argmax2 = EpsilonGreedy.getArgmax(testList2);
        assertEquals(argmax, List.of(3));
        assertEquals(argmax2, Arrays.asList(4, 6));
    }
}