primo :: Int -> Bool
primo 0 = False
primo 1 = False
primo n = checaPrimo n (n `div` 2)

checaPrimo :: Int -> Int -> Bool
checaPrimo _ 1 = True
checaPrimo num x
    | (num `mod` x == 0) = False
    | otherwise = checaPrimo num (x-1)
