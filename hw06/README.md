# Problem Description (Max Flow - Stacking Cards)

### Key part of the problem is to figure out an accurate reduction to a Max Flow Instance.

A kindergarten teacher bought a big grid-shaped mold to use for making chocolate. He believes raisins are very healthy, so he insists on using raisins in the chocolate, and wants to divide the chocolate between his students so every kid gets a piece with raisins. Luckily, he kept track of which squares in the chocolate mold have raisins. He needs to figure out how to break the bar down into pieces such that each piece contains at least $k$ squares with raisins.

The only way one can cleanly break a rectangle of chocolate of $m$ by $n$ squares is by snapping it either horizontally or vertically after the ith row or column, leaving two pieces of size $m\times i$ and $m\times (n-i)$, or $i\times n$ and $(m-i)\times n$, respectively, where all dimensions of the resulting pieces are at least 1. One is unable to break pieces diagonally or into fractions.

The goal is to find the _maximum_ number of pieces in which one can break the chocolate so
that each piece has at least $k$ squares with raisins.

## Run Time Requirements:

The algorithm should run in $O(n^3 m^3 k^5)$ time where $n, m, k$ are the number of friends, number of cards of each color, and number of colors, respectively.

## Space Time Requirements:

The algorithm should consume less than $O(2*n m k)$ amount of memory. The given algorithm consumes $O(nmk)$ amount of memory (**_better than the course staff's solutions!!!_**)
