def factorial_func(x):
    hold = 1
    for j in range(1, x+1):
        hold *= j
    return hold


number = float(input("Enter a number to find the sin of (radians as a decimal): "))
sum_sin = 0
factorial = 1
for i in range(50):
    if i != 0:
        factorial = factorial_func(2*i+1)
    sum_sin += (pow(-1, i) * pow(number, 2 * i + 1)) / factorial
print(sum_sin)