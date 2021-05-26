package main.java;

import org.junit.Test;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;
import com.company.Module;

public class ParallelCompilationOrdererTest {
    @Test
    public void simpleTest() throws Exception {
        /**
         *         1
         *        / \
         *       2   3
         *      / \   \
         *     5  6    4
         */

        Module m6 = new Module(6, new ArrayList<Module>());
        Module m5 = new Module(5, new ArrayList<Module>());
        Module m4 = new Module(4, new ArrayList<Module>());
        Module m3 = new Module(3, List.of(m4));
        Module m2 = new Module(2, List.of(m5, m6));
        Module root = new Module(1, List.of(m2, m3));

        ArrayList<ArrayList<Integer>> order = ParallelCompilationOrderer.get_parallel_compilation_order(root);
        assertEquals (3, order.size());

        ArrayList<Integer> group = new ArrayList<>();
        group.addAll(order.get(0));
        Collections.sort(group);
        assertArrayEquals(new Integer[]{4, 5, 6}, group.toArray());

        group = new ArrayList<>();
        group.addAll(order.get(1));
        Collections.sort(group);
        assertArrayEquals(new Integer[]{2, 3}, group.toArray());

        group = new ArrayList<>();
        group.addAll(order.get(2));
        Collections.sort(group);
        assertArrayEquals(new Integer[]{1}, group.toArray());
    }

    @Test
    public void dupesTest() throws Exception {
        /**
         *         1
         *        / \
         *       2   3
         *        \ /
         *         4
         */

        Module m4 = new Module(4, new ArrayList<Module>());
        Module m3 = new Module(3, List.of(m4));
        Module m2 = new Module(2, List.of(m4));
        Module root = new Module(1, List.of(m2, m3));

        ArrayList<ArrayList<Integer>> order = ParallelCompilationOrderer.get_parallel_compilation_order(root);
        assertEquals (3, order.size());

        ArrayList<Integer> group = new ArrayList<>();
        group.addAll(order.get(0));
        Collections.sort(group);
        assertArrayEquals(new Integer[]{4}, group.toArray());

        group = new ArrayList<>();
        group.addAll(order.get(1));
        Collections.sort(group);
        assertArrayEquals(new Integer[]{2, 3}, group.toArray());

        group = new ArrayList<>();
        group.addAll(order.get(2));
        Collections.sort(group);
        assertArrayEquals(new Integer[]{1}, group.toArray());
    }

    @Test
    public void circularDepthsTest() throws Exception {
        /**
         *         1
         *        /
         *       2
         *        \
         *         1
         */

        Module m1 = new Module(1, new ArrayList<Module>());
        Module m2 = new Module(2, List.of(m1));
        Module root = new Module(1, List.of(m2));

        Exception exception = assertThrows(Exception.class, () -> {
            ParallelCompilationOrderer.get_parallel_compilation_order(root);
        });

        assertTrue(exception.getMessage().contains("Circular dependency!"));
    }
}