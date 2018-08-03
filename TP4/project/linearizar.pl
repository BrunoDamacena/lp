linearizar([X],X).
linearizar([X|L1],LF):-concatenar(X,L2,LF), linearizar(L1,L2).

concatenar([], L, L).
concatenar([X|L1], L2, [X|L3]) :- concatenar(L1, L2, L3).

