# Import necessary libraries
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Step 1: Load the Iris dataset
df = pd.read_csv('iris.csv')

# Step 2: Generate plots
# (a) Bar Plot
plt.figure(figsize=(8, 6))
species_counts = df['species'].value_counts()
species_counts.plot(kind='bar', color=['blue', 'orange', 'green'])
plt.title('Count of Each Species')
plt.xlabel('Species')
plt.ylabel('Count')
plt.show()

# (b) Density Plot
plt.figure(figsize=(8, 6))
sns.kdeplot(df['sepal_length'], shade=True, color='purple')
plt.title('Density Plot for Sepal Length')
plt.xlabel('Sepal Length')
plt.ylabel('Density')
plt.show()

# (c) Pairwise Plot
sns.pairplot(df, hue='species', palette='husl')
plt.suptitle('Pairwise Plot of Features', y=1.02)
plt.show()

# (d) Heatmap
plt.figure(figsize=(10, 8))
correlation_matrix = df.drop('species', axis=1).corr()
sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm', fmt='.2f')
plt.title('Correlation Heatmap')
plt.show()