removerFim :: Int -> [Int] -> [Int]
removerFim n x = inverte (removerInicio n (inverte x))

removerInicio :: Int -> [Int] -> [Int]
removerInicio 0 x = x
removerInicio n (x:xs) = removerInicio (n-1) xs

inverte :: [Int] -> [Int]
inverte [] = []
inverte (x:xs) = inverte xs ++ [x]
