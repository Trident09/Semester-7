import pandas as pd
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import (
    accuracy_score,
    confusion_matrix,
    classification_report,
    precision_score,
    recall_score,
    f1_score,
)
import seaborn as sns
import matplotlib.pyplot as plt

# Load and inspect the dataset
data = pd.read_csv('immunotherapy.csv')

# Data overview
print("First 5 rows of the dataset:")
print(data.head())

print("\nDataset Information:")
data.info()

print("\nClass Distribution:")
print(data['Result_of_Treatment'].value_counts(normalize=True))

# Feature correlation
print("\nFeature Correlation Matrix:")
print(data.corr())

# Step 1: Preprocess the data
data = data.dropna()  # Drop missing values

# Separate features and target variable
X = data[['sex', 'age', 'Time', 'Number_of_Warts', 'Type', 'Area', 'induration_diameter']]
y = data['Result_of_Treatment']

# Normalize the features
scaler = StandardScaler()
X = scaler.fit_transform(X)

# Step 2: Split the dataset into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, stratify=y)

# Step 3: Perform hyperparameter tuning for 'k'
param_grid = {'n_neighbors': range(1, 21)}
knn = KNeighborsClassifier()
grid_search = GridSearchCV(knn, param_grid, cv=5, scoring='accuracy', n_jobs=-1)
grid_search.fit(X_train, y_train)

# Get the best model
best_knn = grid_search.best_estimator_
print(f"\nBest K: {grid_search.best_params_['n_neighbors']}")

# Step 4: Evaluate the model
y_pred = best_knn.predict(X_test)

# Print evaluation metrics
accuracy = accuracy_score(y_test, y_pred)
precision = precision_score(y_test, y_pred)
recall = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)
print("\nEvaluation Metrics:")
print(f"Accuracy: {accuracy:.2f}")
print(f"Precision: {precision:.2f}")
print(f"Recall: {recall:.2f}")
print(f"F1-Score: {f1:.2f}")

# Detailed classification report
print("\nClassification Report:")
print(classification_report(y_test, y_pred))

# Confusion matrix
conf_matrix = confusion_matrix(y_test, y_pred)
print("\nConfusion Matrix:")
print(conf_matrix)

# Visualize confusion matrix
plt.figure(figsize=(6, 5))
sns.heatmap(conf_matrix, annot=True, fmt='d', cmap='Blues')
plt.title("Confusion Matrix")
plt.xlabel("Predicted")
plt.ylabel("Actual")
plt.show()