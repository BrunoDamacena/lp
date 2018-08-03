nelementos([], 0).
nelementos([_|L1], Y) :- nelementos(L1, A), Y is A+1.
