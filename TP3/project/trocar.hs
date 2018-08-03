trocar :: Int -> [Int]
trocar x
    | x >= 100 = trocar (x-100) ++ [100]
    | x >= 50 = trocar (x-50) ++ [50]
    | x >= 10 = trocar (x-10) ++ [10]
    | x >= 5 = trocar (x-5) ++ [5]
    | x >= 1 = trocar (x-1) ++ [1]
    | otherwise = []
