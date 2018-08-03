somaParciais :: [Int] -> [Int]
somaParciais [x] = [x]
somaParciais x = somaParcialReverso (reverter x)

reverter :: [Int] -> [Int]
reverter [] = []
reverter (x:xs) = reverter xs ++ [x]

somaParcialReverso :: [Int] -> [Int]
somaParcialReverso [x] = [x]
somaParcialReverso (x:xs) = somaParcialReverso xs ++ [x + somas xs]

somas :: [Int] -> Int
somas [x] = x
somas (x:xs) = x + somas xs

