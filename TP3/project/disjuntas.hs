disjuntas :: [Int] -> [Int] -> Bool
disjuntas [] _ = True
disjuntas _ [] = True
disjuntas (x:xs) (y:ys)
    | x == y = False
    | otherwise = (disjuntas (x:xs) ys) && (disjuntas (y:ys) xs)
