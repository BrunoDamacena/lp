adjacente(X,Y,[X,Y|_]).
adjacente(X,Y,[_,D|L1]) :- concatenar([D], L1, L2), adjacente(X,Y, L2).

concatenar([], L, L).
concatenar([X|L1], L2, [X|L3]) :- concatenar(L1, L2, L3).
