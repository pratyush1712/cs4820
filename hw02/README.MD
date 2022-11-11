# Problem Description (Kruskal's Algorithm with exactly 3 MST)

In this problem we consider the classical Kruskal’s minimum-cost spanning tree algorithm. Given $n$ nodes $V$ , and $m$ possible edges $E$, each $e\in E$ with its cost $c_e>0$, we will **implement the algorithm until there are 3 components left**. The output of this algorithm will be a set of $n-3$ edges that give a graph on $V$ with $3$ components. The algorithm will output the sizes of the three component in non-decreasing order.

## Run Time Requirements:

The algorithm should run in $O(m\log n)$ time where $n$ is the number of nodes and $m$ is the number of edges.

## Data Structures Used:

**Union-Find**: A data structure used to create disjoint sets, find the set of an element, and combine two sets.

Time complexities:

- **Find**: $O(\log n)$
- **Make**: $O(n)$
