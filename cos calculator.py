def factorial_func(x):
    hold = 1
    for j in range(1, x + 1):
        hold *= j
    return hold


number = float(input("Enter a number to find the cos of (radians as a decimal): "))
sum_cos = 0
factorial = 1
for i in range(50):
    if i != 0:
        factorial = factorial_func(2 * i)
    sum_cos += (pow(-1, i) * pow(number, 2 * i)) / factorial
print(sum_cos)
