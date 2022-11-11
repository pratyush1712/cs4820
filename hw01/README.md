# Problem Description (Gale-Shapley Algorithm with Forbidden Pairs)

Consider the stable matching problem, with n hospitals, and n residents. We wish
to implement the Gale-Shapley algorithm with the hospitals proposing.

We consider the stable matching problem with forbidden pairs, in which some pairs
are not allowed. In particular, the input to the problem has an additional set $F\subseteq H\times R$ (where H is the set of hospitals and R the set of residents) of forbidden pairs.

A matching M is a stable matching with forbidden pairs (SMWFP) for this instance if:

- $M\cap F=\Phi$ (M has no forbidden pairs), and
- there is no pair $(h,\;r)$ such that
  - $(h,\;r)\not\in F$ and,
  - $h$ is unmatched or prefers $r$ to its current match in $M$ and,
  - $r$ is unmatched or prefers $h$ to its current match in $M$

## Run time requirements:

The algorithm should run in $O(n^2)$ time where $n$ is the number of residences/hospitals.
