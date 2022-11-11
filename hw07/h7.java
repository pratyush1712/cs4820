import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Main {
    static int colors; // k
    static int values; // m
    static int friends; // n
    static int numNodes; // number of nodes in graph
    static int srcIndex; // index of s
    static int sinkIndex; // index of t
    static int[][][] cards;
    static Integer[][] dests_final; // dests[i] is the destinations of the edges that leave vertex i
    static Integer[][] ids_final; // ids[i] is the edge ids of the edges that leave edge i

    static int[] backtrack2; // populated by DFS, used by FF
    static int[] minima; // populated by DFS, used by FF

    static int[] cap; // cap[j] is the capacity of edge with id j; backwards edges are added at graph
                      // initialization directly after forwards edges. not modified after init
    static int[] rescap; // rescap[j] is the residual capacity of edge with id j. use for debugging
    // static File file = new File("D:\\cs4820\\hw07\\test1.txt");

    public static void main(String[] args) {

        // sample code to read inputs
        try {
            // BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String[] dim = reader.readLine().split(" ");
            friends = Integer.parseInt(dim[0]);
            values = Integer.parseInt(dim[1]);
            colors = Integer.parseInt(dim[2]);
            cards = new int[friends][values][colors];
            for (int friend = 0; friend < friends; friend++) {
                String[] friendCards = reader.readLine().split(" ");
                for (int i = 0; i < friendCards.length; i += 2) {
                    int value = Integer.parseInt(friendCards[i]);
                    int color = Integer.parseInt(friendCards[i + 1]);
                    // everything 1-indexed
                    cards[friend][value - 1][color - 1]++;
                }
            }
            // for (int person = 0; person < friends; person++) {
            // for (int value = 0; value < values; value++) {
            // for (int color = 0; color < colors; color++) {
            // System.out.println(person + " " + value + " " + color + ": " +
            // cards[person][value][color]);
            // }
            // }
            // }
        } catch (IOException e) {
            System.err.printf(e.toString());
            System.err.printf("Got an IO exception\n");
        }
        int numEdges = 0;
        ArrayList<Integer> edge_heads_new = new ArrayList<>();
        ArrayList<Integer> edge_tails_new = new ArrayList<>();
        ArrayList<Integer> edge_caps_new = new ArrayList<>();
        for (int person = 0; person < friends; person++) {
            for (int value = 0; value < values; value++) {
                for (int color = 0; color < colors; color++) {
                    if (cards[person][value][color] != 0) {
                        if (value == 0) {
                            edge_heads_new.add(0);
                            edge_tails_new.add(find_index(person, value, color, 0)); // from source to this node
                            edge_caps_new.add(Integer.MAX_VALUE); // edge of capacity infinty between different
                            // nodes
                            numEdges++;
                        } else if (value == values - 1) {
                            edge_heads_new.add(find_index(person, value, color, 1));
                            edge_tails_new.add(find_index(friends - 1, values - 1, colors - 1, 1) + 1); // from this
                                                                                                        // node to sink
                            edge_caps_new.add(Integer.MAX_VALUE); // edge of capacity infinty between different
                            // nodes
                            numEdges++;
                            // System.out.println(person + " " + value + " " + color);
                        }
                        int[] persons_to_check = (((person + 1) % (friends)) != person)
                                ? new int[] { person, ((person + 1) % (friends)) }
                                : new int[] { person };
                        // for (int person21 = person; person21 <= person + 1; person21++) {
                        // int person2 = person21;
                        // if (person21 == friends) {
                        // person2 = 0;
                        // }
                        for (int person2 : persons_to_check) {
                            int value2 = value;
                            for (int color2 = 0; color2 < colors; color2++) {
                                if (cards[person2][value2][color2] != 0) { // it means there exist at least one of
                                                                           // this
                                                                           // valid card
                                    if (!(color2 == color && person2 == person)) { // we will handle same color
                                                                                   // differently, so chill out for
                                                                                   // now
                                                                                   // System.out.println(edge_heads_new.toString());
                                        edge_heads_new.add(find_index(person, value, color, 1));
                                        edge_tails_new.add(find_index(person2, value2, color2, 0)); // from 1 to 0
                                        edge_caps_new.add(Integer.MAX_VALUE); // edge of capacity infinty between
                                                                              // different
                                                                              // nodes
                                    } else { // if same card then
                                        edge_heads_new.add(find_index(person, value, color, 0));
                                        edge_tails_new.add(find_index(person2, value2, color2, 1)); // from 0 to 1
                                        edge_caps_new.add(cards[person][value][color]); // edge of capacity equal to
                                                                                        // number of duplicates
                                    }
                                    numEdges++;
                                }
                            }
                            // now adding edges between nodes of card value + 1, and same card color
                            value2++;
                            // System.out.println(value + " " + value2);
                            if (value2 != values) {
                                if (cards[person2][value2][color] != 0) {
                                    edge_heads_new.add(find_index(person, value, color, 1));
                                    edge_tails_new.add(find_index(person2, value2, color, 0)); // from 1 to 0
                                    edge_caps_new.add(Integer.MAX_VALUE); // edge of capacity infinty between different
                                    numEdges++;
                                }
                            }
                        }
                    }
                }
            }
        }
        // System.out.println(edge_heads_new);
        // System.out.println(edge_tails_new);
        // System.out.println(edge_caps_new);
        // System.out.println(numEdges);
        // sample code to make a graph in expected format
        int num_vertices = 2 * friends * values * colors + 2;// 4;
        srcIndex = 0;
        sinkIndex = (friends - 1) * (values * colors * 2) + (values - 1) * (colors * 2) + (colors - 1) * 2 + 2 + 1;
        int num_edges = numEdges;
        // these are hard-coded into arrays only because it's easier to read off the
        // original graph.
        // you'll probably want to generate the arraylists on the fly in your
        // implementation without intermediate arrays.
        int[] edge_heads = edge_heads_new.stream().mapToInt(Integer::intValue).toArray();
        int[] edge_tails = edge_tails_new.stream().mapToInt(Integer::intValue).toArray();
        int[] edge_caps = edge_caps_new.stream().mapToInt(Integer::intValue).toArray();
        // initialize using ArrayLists for dynamic resizing
        ArrayList<Integer> capacities_rough = new ArrayList<>();
        ArrayList<ArrayList<Integer>> dests_rough = new ArrayList<>();
        ArrayList<ArrayList<Integer>> ids_rough = new ArrayList<>();
        for (int i = 0; i < num_vertices; i++) {
            dests_rough.add(new ArrayList<Integer>());
            ids_rough.add(new ArrayList<Integer>());
        }

        int edge_index = 0;

        for (int i = 0; i < num_edges; i++) {
            int u = edge_heads[i];
            int v = edge_tails[i];
            int c = edge_caps[i];

            // note that forwards edge is directly followed by backwards edge
            dests_rough.get(u).add(v);
            dests_rough.get(v).add(u);
            ids_rough.get(u).add(edge_index++);
            ids_rough.get(v).add(edge_index++);
            capacities_rough.add(c);
            capacities_rough.add(0);
        }

        // backtrack should have length 2 * n
        backtrack2 = new int[ids_rough.size() * 2];

        // convert ArrayLists to arrays. instead of doing this you could replace the
        // arry indexing in dfs, F-F with calls to get and set, but initial experiments
        // suggest that this is faster for large graphs
        dests_final = dests_rough.stream().map(u -> u.toArray(new Integer[0])).toArray(Integer[][]::new);
        ids_final = ids_rough.stream().map(u -> u.toArray(new Integer[0])).toArray(Integer[][]::new);
        rescap = capacities_rough.stream().mapToInt(Integer::intValue).toArray();
        cap = capacities_rough.stream().mapToInt(Integer::intValue).toArray();
        System.out.println(fordFulkerson());
        // also populates rescap, if you need it for debugging!
    }

    /**
     * Runs Ford-Fulkerson on the card network (using Edmonds-Karp) (Pseudocode that
     * inspired this from CLSR book)
     *
     * @return the maximum flow
     */
    public static int fordFulkerson() {
        int maxFlow = 0; // no flow at beginning
        while (bfs()) { // exists an augmenting path
            int currentCap = minima[sinkIndex];
            int edgeEnd = sinkIndex;
            while (edgeEnd != srcIndex) {
                int edge = backtrack2[2 * edgeEnd + 1];
                int rev_edge = (edge % 2 == 1) ? edge - 1 : edge + 1;
                rescap[edge] -= currentCap;
                rescap[rev_edge] += currentCap;
                edgeEnd = backtrack2[2 * edgeEnd];
            }
            maxFlow += currentCap;
        }
        return maxFlow;
    }

    /**
     * Runs DFS on the residual graph
     *
     * @return Whether there exists an s-t path on the residual graph
     * @postcondition backtrack contains the chosen parents of the nodes
     */
    public static boolean bfs() {
        boolean[] visited = new boolean[ids_final.length];
        minima = new int[ids_final.length];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(srcIndex);
        backtrack2[2 * srcIndex] = Integer.MIN_VALUE;
        backtrack2[2 * srcIndex + 1] = -1;
        visited[srcIndex] = true;
        minima[srcIndex] = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            int currMin = minima[currentNode];
            for (int i = 0; i < dests_final[currentNode].length; i++) {
                int neighbor = dests_final[currentNode][i];
                int edge = ids_final[currentNode][i];
                if (!visited[neighbor]
                        && rescap[edge] > 0) { // exists an edge that can allow more flow
                    int newMin = Math.min(currMin, rescap[edge]);
                    backtrack2[2 * neighbor] = currentNode;
                    backtrack2[2 * neighbor + 1] = edge;
                    minima[neighbor] = newMin;
                    if (neighbor == sinkIndex) {
                        return true; // s-t path found
                    }
                    queue.add(neighbor);
                    visited[neighbor] = true; // ensure no cycles
                }
            }
        }
        return false; // no s-t path found
    }

    public static int find_index(int friend_index, int value, int color, int node_index) {
        return friend_index * (values * colors * 2) + value * (colors * 2) + color * (2) + node_index + 1;
    }
}