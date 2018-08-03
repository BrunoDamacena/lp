ultimo([L], L).
ultimo([X|L1], L) :- ultimo(L1, L).
