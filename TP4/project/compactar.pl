compactar([],[]).
compactar([X],[[1,X]]).
compactar([X,X|L1], [[N,X] | LF]):- compactar([X | L1], [ [V,X] | LF ]), N is V+1.
compactar([X,Y|L1],[[1,X] |LF]):- X \== Y, compactar([Y| L1], LF).

concatenar([], L, L).
concatenar([X|L1], L2, [X|L3]) :- concatenar(L1, L2, L3).
