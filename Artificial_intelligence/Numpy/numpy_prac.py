# Import NumPy library
import numpy as np

# Step 1: Create sample arrays
array1 = np.array([1, 2, 3, 4])
array2 = np.array([5, 6, 7, 8])

# Step 2: Perform arithmetic operations
addition = array1 + array2
subtraction = array1 - array2
multiplication = array1 * array2
division = array1 / array2

# Step 3: Perform logical operations
logical_and = np.logical_and(array1 > 2, array2 > 6)
logical_or = np.logical_or(array1 > 2, array2 > 6)
logical_not = np.logical_not(array1 > 2)

# Step 4: Comparison operation
comparison = array1 > array2

# Display outputs
print("Addition:", addition)
print("Subtraction:", subtraction)
print("Multiplication:", multiplication)
print("Division:", division)
print("Logical AND:", logical_and)
print("Logical OR:", logical_or)
print("Logical NOT:", logical_not)
print("Comparison:", comparison)