package main.java;

import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import com.company.Module;

public class ModuleTest {
    @Test
    public void testGetId() {
        Module m = new Module(31415, new ArrayList<Module>());
        assertEquals(31415, m.getId());
    }
    @Test
    public void testGetDeps() {
        Module m2 = new Module(2, new ArrayList<Module>());
        Module m1 = new Module(1, new ArrayList<Module>(List.of(m2)));
        assertEquals(1, m1.getDeps().size());
        assertEquals(2, m1.getDeps().get(0).getId());
    }
}