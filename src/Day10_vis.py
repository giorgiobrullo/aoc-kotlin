import matplotlib.pyplot as plt
import numpy as np
import matplotlib.colors as mcolors

# Function to convert the string matrix to a numerical matrix
def convert_to_matrix(string_matrix):
    matrix = []
    for line in string_matrix.strip().split("\n"):
        row = [int(x) if x != '-1' else -1 for x in line.split()]
        matrix.append(row)
    return matrix

# Read the formatted matrix from a file
with open('matrix.txt', 'r') as file:
    string_matrix = file.read()

# Convert to a numerical matrix
matrix = convert_to_matrix(string_matrix)

# Convert the matrix to a NumPy array for better handling
np_matrix = np.array(matrix, dtype=float)  # Ensure the data type is float

# Replace -1 with np.nan for visualization
np_matrix[np_matrix == -1] = np.nan

# Create a color map
cmap = plt.cm.viridis  # You can choose any colormap you like
cmap.set_bad('black', 1.0)  # Set the color for NaN values

# Visualization with matplotlib
plt.imshow(np_matrix, cmap=cmap, interpolation='nearest')
plt.colorbar()
plt.show()
