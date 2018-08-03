medio(L,M):-soma(L,S),nelementos(L,N), M is S/N.

nelementos([],0).
nelementos([_|R],S):-nelementos(R,S1), S is S1 + 1.

soma([], 0).
soma([X | L], S) :- soma(L, S1), S is X+S1.
