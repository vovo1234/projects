package com.company;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Module {
    /**
     * Module class stores module id and a list of module's dependencies.
     * Each dependency is supposed to be a Node instance.
     * @version 1.0
     * @author Vlad Lyapunov
     */

    private int id;
    private ArrayList<Module> deps;   // direct dependencies, in any order

    public Module(int id, List<Module> deps) {
        this.id = id;
        this.deps = new ArrayList<Module>();
        this.deps.addAll(deps);
    }

    public int getId() {
        return this.id;
    }

    public List<Module> getDeps() {
        return Collections.unmodifiableList(this.deps);
    }
}
