binario :: Int -> [Int]
binario 0 = [0]
binario 1 = [1]
binario n = binario (n `div` 2) ++ [(n `mod` 2)]
