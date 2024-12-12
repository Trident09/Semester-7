# Import necessary libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans

# Load the dataset
data = pd.read_csv('Mall_Customers.csv')

# Display first few rows of the dataset
print(data.head())

# Select features for clustering (Annual Income and Spending Score)
X = data[['annual_income', 'spending_score']]

# Elbow method to find the optimal number of clusters
inertia = []
K = range(1, 11)
for k in K:
    kmeans = KMeans(n_clusters=k, random_state=42)
    kmeans.fit(X)
    inertia.append(kmeans.inertia_)

# Plot the Elbow Curve
plt.figure(figsize=(8, 5))
plt.plot(K, inertia, marker='o')
plt.title('Elbow Method for Optimal K')
plt.xlabel('Number of Clusters')
plt.ylabel('Inertia')
plt.show()

# From the Elbow Curve, choose the optimal number of clusters (e.g., 5)
k_optimal = 5
kmeans = KMeans(n_clusters=k_optimal, random_state=42)
kmeans.fit(X)

# Add the cluster labels to the dataset
data['Cluster'] = kmeans.labels_

# Visualize the clusters
plt.figure(figsize=(8, 6))
for i in range(k_optimal):
    plt.scatter(
        X.loc[data['Cluster'] == i, 'annual_income'],
        X.loc[data['Cluster'] == i, 'spending_score'],
        label=f'Cluster {i+1}'
    )

plt.scatter(
    kmeans.cluster_centers_[:, 0], 
    kmeans.cluster_centers_[:, 1], 
    s=200, c='red', label='Centroids', marker='X'
)

plt.title('Customer Segments')
plt.xlabel('Annual Income (k$)')
plt.ylabel('Spending Score (1-100)')
plt.legend()
plt.show()

# Save the updated dataset with cluster labels to a new CSV file
data.to_csv('Mall_Customers_with_Clusters.csv', index=False)

print("Clustering complete! Results saved to 'Mall_Customers_with_Clusters.csv'.")
