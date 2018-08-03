remover(X, [X], []).
remover(X, [Y], [Y]):- X \== Y.
remover(X, [X|T], NT) :- remover(X,T,NT).
remover(X, [Y|T], [Y|NT]) :- X \== Y, remover(X, T, NT).
