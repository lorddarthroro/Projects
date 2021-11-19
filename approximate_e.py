degree = int(input("Enter a degree of specificity (n): "))
sum_e = 0
factorial = 1
for i in range(degree+1):
    factorial *= i
    if factorial == 0:
        factorial = 1;
    sum_e += 1/factorial
print(sum_e)
