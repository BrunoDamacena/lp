rotacionarn(0,L1,L1).
rotacionarn(1,[X|L1],L2):-concatenar(L1,[X],L2).
rotacionarn(N,[X|L1],L3):-N1 is N-1, concatenar(L1,[X],L2),rotacionarn(N1,L2,L3).

concatenar([], L, L).
concatenar([X|L1], L2, [X|L3]) :- concatenar(L1, L2, L3).
