perfeito :: Int -> Bool
perfeito a = a == (somatorio (fatores a))

somatorio :: [Int] -> Int
somatorio [] = 0
somatorio (x:xs) = x + somatorio xs

fatores :: Int -> [Int]
fatores 0 = []
fatores n = [i | i <- [1..(n-1)], n `mod` i == 0]
