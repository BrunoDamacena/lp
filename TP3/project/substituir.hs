substituir :: Int -> Int -> [Int] -> [Int]
substituir _ _ [] = []
substituir a b (x:xs)
    | a == x = b : substituir a b xs
    | otherwise = x : substituir a b xs
