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
        assertEquals(argmax, List.of(3));
        assertEquals(argmax2, Arrays.asList(4, 6));
    }
}