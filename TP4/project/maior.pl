maior([X],X).
maior([X|XS], M) :- maior(XS, M1), X > M1, M is X.
maior([X|XS], M) :- maior(XS, M1), X =< M1, M is M1.
