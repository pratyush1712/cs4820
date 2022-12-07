; This program checks if the input is of form 0^k1^(k^2)

;initial state 0: reject if the string starts with a 1
0 0 0 l 1
0 1 1 r halt-reject
0 _ _ r halt-accept

;state 1: convert string to one way tape
1 _ d r s0

; state s0: reading all the 0s at start
s0 0 0 r s0
s0 1 1 r s1

; state s1: reading all 1s now
s1 1 1 r s1
s1 _ a l s2
s1 0 0 r halt-reject

; state s2: go back to start
s2 Y Y l s2
s2 1 1 l s2
s2 0 0 l s2
s2 X X l s2
s2 d d r s3

; state s3: marking all 0s
s3 0 X * s4
s3 X X r s3

; state s4: go back to start
s4 X X l s4
s4 d d r s5

; state s5: marking a 1 for all x and 0
s5 0 o r s6
s5 X x r s6
s5 Y Y l s8

;state s6: find first 1 in input
s6 X X r s6
s6 0 0 r s6
s6 Y Y r s6
s6 1 Y * s7
s6 a a * halt-reject

;state s7: 
s7 1 1 l s7
s7 0 0 l s7
s7 Y Y l s7
s7 o 0 r s5
s7 x X r s5
s7 X X l s7

;state s8: 
s8 0 0 * s2
s8 X X * s9
s8 Y Y * s2

; state s9: evaluating the final
s9 Y Y r s9
s9 X X r s9
s9 1 1 r halt-reject
s9 0 0 r halt-reject
s9 a a * halt-accept