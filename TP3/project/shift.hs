shift :: Int -> [Int] -> [Int]
shift n (x:xs)
    | n == 0 = (x:xs)
    | otherwise = shift (n-1) (xs ++ [x])
