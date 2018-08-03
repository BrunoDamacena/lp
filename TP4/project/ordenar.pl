ordenar([],[]).
ordenar([X|R],Ord):-menores(X,R,A),maiores(X,R,S), ordenar(A,AO),ordenar(S,SO), concatenar(AO,[X],AX),concatenar(AX,SO,Ord).

menores(X,[],[]).
menores(X,[Y | R],M):-X<Y,menores(X,R,M).
menores(X,[Y | R],C):-X>=Y,menores(X,R,M),concatenar([Y],M,C).

maiores(X,[],[]).
maiores(X,[ Y | R ],M):-X>=Y,maiores(X,R,M).
maiores(X,[ Y | R ],C):-X<Y,maiores(X,R,M),concatenar([Y],M,C).

concatenar([], L, L).
concatenar([X|L1], L2, [X|L3]) :- concatenar(L1, L2, L3).
