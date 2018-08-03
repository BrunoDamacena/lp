intercalar :: [Int] -> [Int] -> [Int]
intercalar x [] = x
intercalar [] x = x
intercalar (x:xs) (y:ys)
    | x < y = [x] ++ intercalar xs ([y] ++ ys)
    | otherwise = [y] ++ intercalar ([x] ++ xs) ys
