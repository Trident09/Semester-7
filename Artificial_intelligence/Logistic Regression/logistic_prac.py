# Import necessary libraries
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, confusion_matrix, roc_curve, auc
import matplotlib.pyplot as plt
import numpy as np

# Step 1: Load the dataset
data = pd.read_csv('cryotherapy.csv')

# Step 2: Preprocess the data
# Drop rows with missing values (if any)
data = data.dropna()

# Separate features and target variable
X = data[['sex', 'age', 'Time', 'Number_of_Warts', 'Type', 'Area']]
y = data['Result_of_Treatment']  # Target (binary: 0 or 1)

# Normalize the features
scaler = StandardScaler()
X = scaler.fit_transform(X)

# Step 3: Split the dataset into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Step 4: Train the Logistic Regression model
model = LogisticRegression()
model.fit(X_train, y_train)

# Step 5: Test the model
y_pred = model.predict(X_test)
y_pred_prob = model.predict_proba(X_test)[:, 1]  # Get probabilities for the positive class

# Step 6: Evaluate the model
accuracy = accuracy_score(y_test, y_pred)
conf_matrix = confusion_matrix(y_test, y_pred)

# Print results
print("\n" + "="*40)
print("          Model Evaluation Results          ")
print("="*40)
print(f"\n1. Model Accuracy: {accuracy:.2f}")
print("\n2. Confusion Matrix:")
print(f"""
      | Predicted: 0 | Predicted: 1 |
-------------------------------------
Actual: 0 |    {conf_matrix[0, 0]}         |    {conf_matrix[0, 1]}         |
Actual: 1 |    {conf_matrix[1, 0]}         |    {conf_matrix[1, 1]}         |
""")
print("\n3. Insights:")
print(f"   - True Negatives (TN): {conf_matrix[0, 0]}")
print(f"   - False Positives (FP): {conf_matrix[0, 1]}")
print(f"   - False Negatives (FN): {conf_matrix[1, 0]}")
print(f"   - True Positives (TP): {conf_matrix[1, 1]}")

# Step 7: Plot ROC Curve
fpr, tpr, thresholds = roc_curve(y_test, y_pred_prob)
roc_auc = auc(fpr, tpr)

plt.figure(figsize=(8, 6))
plt.plot(fpr, tpr, color='blue', lw=2, label=f'ROC Curve (AUC = {roc_auc:.2f})')
plt.plot([0, 1], [0, 1], color='gray', linestyle='--', lw=1)  # Diagonal line
plt.title('Receiver Operating Characteristic (ROC) Curve')
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.legend(loc="lower right")
plt.grid(alpha=0.3)
plt.show()


age_range = np.linspace(X[:, 1].min(), X[:, 1].max(), 300).reshape(-1, 1)
age_range_scaled = scaler.transform(np.hstack([np.zeros((300, 1)), age_range, np.zeros((300, 4))]))
probabilities = model.predict_proba(age_range_scaled)[:, 1]

plt.figure(figsize=(8, 6))
plt.scatter(X[:, 1], y, color='blue', alpha=0.6, label='Actual Data')
plt.plot(age_range, probabilities, color='red', label='Logistic Sigmoid Curve', linewidth=2)
plt.title('Logistic Regression - Predicted Probability Curve')
plt.xlabel('Age (Standardized)')
plt.ylabel('Probability of Positive Treatment Result')
plt.legend(loc='lower right')
plt.grid(alpha=0.3)
plt.show()