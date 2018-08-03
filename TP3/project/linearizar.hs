linearizar :: [[Int]] -> [Int]
linearizar [] = []
linearizar [x] = x
linearizar (x:xs) = linearizar [x] ++ linearizar xs
