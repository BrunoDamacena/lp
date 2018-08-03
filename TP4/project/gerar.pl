gerar(Y, Y, [Y]).
gerar(X, Y, [X|L1]) :- A is X+1, gerar(A, Y, L1).
