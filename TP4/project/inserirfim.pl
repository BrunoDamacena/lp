inserirfim(X, [], [X]).
inserirfim(X, [Y|L1], [Y|L2]) :- inserirfim(X, L1, L2).
