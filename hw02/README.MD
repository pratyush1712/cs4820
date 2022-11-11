# Problem Description (Kruskal's Algorithm with exactly 3 MST)

In this problem we consider the classical Kruskal’s minimum-cost spanning tree algorithm. Given $n$ nodes $V$ , and $m$ possible edges $E$, each $e\in E$ with its cost $c_e>0$, we will **implement the algorithm until there are 3 components left**. The output of this algorithm will be a set of $n-3$ edges that give a graph on $V$ with $3$ components. The algorithm will output the sizes of the three component in non-decreasing order.

$F\subseteq H\times R$

(where H is the set of hospitals and R the set of residents) of forbidden pairs.

A matching M is a stable matching with forbidden pairs (SMWFP) for this instance if:

- $M\cap F=\Phi$ (M has no forbidden pairs), and
- there is no pair $(h,\;r)$ such that
  - $(h,\;r)\not\in F$ and,
  - $h$ is unmatched or prefers $r$ to its current match in $M$ and,
  - $r$ is unmatched or prefers $h$ to its current match in $M$

## Run Time Requirements:

The algorithm should run in $O(m\log n)$ time where $n$ is the number of residences/hospitals.

## Data Structures Used:

**Union-Find**:

Time complecities:

- **Find**: $O(\log n)$
- **Make**: $O(n)$
