package main.java;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.company.Module;

public class ParallelCompilationOrderer {
    /**
     * ParallelCompilationOrderer class obtains the compilation order for a module dependency graph.
     * The goal is to split all modules into a sequence of groups
     *    - groups in the beginning of the list should be compiled first
     *    - modules in each group can be compiled in parallel (on a multiple CPU cores or build machines)
     * @version 1.0
     * @author Vlad Lyapunov
     */

    // to detect circular dependencies
    private ArrayList<Integer> dependencyChain;
    // needed for parallel compilation ordering
    private HashMap<Integer, Integer> depthById;
    // this makes the life easier when creating compilation group list
    private Integer graphDepth;

    private ParallelCompilationOrderer() {
        this.dependencyChain = new ArrayList<Integer>();
        this.depthById = new HashMap<Integer, Integer>();
        this.graphDepth = 0;
    }

    public static ArrayList<ArrayList<Integer>> get_parallel_compilation_order(Module root) throws Exception {
         /**
          * get_parallel_compilation_order groups module ids into groups by dependency depth.
          *
          * All-in-all it seems to be a good balance of efficiency and simplicity,
          * although there are cases when this approach provides non-ideal answer.
          *
          * E.g.
          *   A
          *  / \
          * B   C
          *      \
          *       D
          * This approach will return [[D], [B, C], [A]],
          * the ideal solution would be [[D, B], [C], [A]].
           */

         // traverse dependency graph, storing the dependency depth for each module
         // we cannot reliably compose ids_by_depth mapping yet:
         // some modules can present multiple times in the graph at different depths
         ParallelCompilationOrderer co = new ParallelCompilationOrderer();
         co.processModule(root, 0);

         // compose the list of groups
         // each group can be compiled in parallel
         // highest depth group should be compiled first
         ArrayList<ArrayList<Integer>> parallelCompilationGroups = new ArrayList<ArrayList<Integer>>();
         for (Integer i=0; i <= co.graphDepth; i++) {
             parallelCompilationGroups.add(new ArrayList<Integer>());
         }
         for (Map.Entry<Integer, Integer> entry: co.depthById.entrySet()){
            Integer id = entry.getKey();
            Integer depth = entry.getValue();
            parallelCompilationGroups.get(co.graphDepth-depth).add(id);
         }

         return parallelCompilationGroups;
    }

    private void processModule(Module node, Integer depth) throws Exception {
        // throw if circular dependency is detected
        // checking if node.id presents in the chain can be optimized by storing ids in a hash set
        // the list is still good to have though for proper exception message
        if (this.dependencyChain.contains(node.getId())) {
            this.dependencyChain.add(node.getId());

            String str = "";
            StringBuilder builder = new StringBuilder();
            for(Integer item : this.dependencyChain)
                builder.append(item);
            throw new Exception("Circular dependency! " + builder.toString());
        }

        // update graph depth
        if (this.graphDepth < depth) {
            this.graphDepth = depth;
        }

        // temporary add the node.id into the dependency chain,
        this.dependencyChain.add(node.getId());
        // process the node's dependencies
        for (Module dependency : node.getDeps()) {
            this.processModule(dependency, depth + 1);
        }
        // pop node.id from the chain
        this.dependencyChain.remove(this.dependencyChain.size()-1);

        // if multiple nodes depend on this node, pick maximum depth
        // This will guarantee that the dependency module is compiled early enough for all cases
        Integer max_depth = depth;
        if (this.depthById.containsKey(node.getId()) && this.depthById.get(node.getId()) > max_depth) {
            max_depth = this.depthById.get(node.getId());
        }
        this.depthById.put(node.getId(), max_depth);
    }
}
