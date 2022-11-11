import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Main {

  // declaring nodes and edges
  static int nodes;
  static int numEdges;

  // each component: parent, size
  static ArrayList<int[]> componentsArray = new ArrayList<int[]>();

  // edges
  static ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();

  // selected edges
  static ArrayList<ArrayList<Integer>> selectedEdges = new ArrayList<ArrayList<Integer>>();

  static int[] find(int nodeIndex) {
    if (componentsArray.get(nodeIndex)[0] != -1) {
      return find(componentsArray.get(nodeIndex)[0]);
    }
    return new int[] { nodeIndex, componentsArray.get(nodeIndex)[1] };
  }

  static File file = new File("D:\\cs4820\\hw02\\test6.txt");

  public static void main(String[] args) throws NumberFormatException, IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));

    nodes = Integer.parseInt(br.readLine());
    numEdges = Integer.parseInt(br.readLine());

    // reading in number of nodes
    for (int i = 0; i < nodes; i++) {
      int[] node = new int[] { -1, 1 }; // initially parent: -1, size: 1
      componentsArray.add(node);
    }

    // reading in edges
    for (int i = 0; i < numEdges; i++) {
      String[] line = br.readLine().split(" ");
      ArrayList<Integer> edge = new ArrayList<Integer>();
      edge.add(Integer.parseInt(line[0]));
      edge.add(Integer.parseInt(line[1]));
      edge.add(Integer.parseInt(line[2]));
      edges.add(edge);
    }

    // sort edges by their weights
    edges.sort((a, b) -> {
      return a.get(2) - b.get(2);
    });

    br.close();
    for (int i = 0; i < numEdges; i++) {
      if (selectedEdges.size() < nodes - 3) {
        int[] firstParent = find(edges.get(i).get(0));
        int[] secondParent = find(edges.get(i).get(1));
        if (firstParent[0] != secondParent[0]) {
          if (firstParent[1] > secondParent[1]) {
            componentsArray.get(secondParent[0])[0] = (firstParent[0]);
            componentsArray.get(firstParent[0])[1] += secondParent[1];
            componentsArray.get(secondParent[0])[1] += secondParent[1];
          } else {
            componentsArray.get(firstParent[0])[0] = (secondParent[0]);
            componentsArray.get(secondParent[0])[1] += (firstParent[1]);
            componentsArray.get(firstParent[0])[1] += firstParent[1];
          }
          selectedEdges.add(edges.get(i));
        }
      }
    }
    ArrayList<Integer> finalResp = new ArrayList<Integer>();
    for (int i = 0; i < nodes; i++) {
      if (componentsArray.get(i)[0] == -1) {
        finalResp.add(componentsArray.get(i)[1]);
      }
    }
    finalResp.sort((c1, c2) -> {
      return c1 - c2;
    });
    for (int l : finalResp) {
      System.out.println(l);
    }
  }
}