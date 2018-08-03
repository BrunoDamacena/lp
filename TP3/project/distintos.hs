distintos :: [Int] -> Bool
distintos [] = True
distintos [_] = True
distintos (x:y:xs)
    | x == y = False
    | otherwise = (distintos (x:xs)) && (distintos (y:xs))
