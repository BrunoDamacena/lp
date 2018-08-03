potencia :: Int -> Int -> Int
potencia _ 0 = 1
potencia b e = b * potencia b (e-1)
