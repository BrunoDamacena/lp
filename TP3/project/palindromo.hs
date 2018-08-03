palindromo :: [Int] -> Bool
palindromo [] = False
palindromo x = x == reverter x

reverter :: [Int] -> [Int]
reverter [] = []
reverter (x:xs) = reverter xs ++ [x]
