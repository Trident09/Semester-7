# Import Pandas library
import pandas as pd
import numpy as np

# Step 1: Create a sample DataFrame
data = {
    'Name': ['Alice', 'Bob', 'Charlie', 'David', 'Eve'],
    'Age': [24, 27, 22, 32, 29],
    'Score': [85, 90, 78, 88, 95]
}
df = pd.DataFrame(data)

# Step 2: Perform 10 functionalities
# 1. Select specific columns
print("Select 'Name' column:\n", df['Name'])

# 2. Add a new column
df['Passed'] = df['Score'] > 80
print("\nDataFrame after adding 'Passed' column:\n", df)

# 3. Filter rows based on a condition
filtered_df = df[df['Age'] > 25]
print("\nRows where Age > 25:\n", filtered_df)

# 4. Group data and calculate mean
grouped = df.groupby('Passed')['Score'].mean()
print("\nMean score based on 'Passed':\n", grouped)

# 5. Handle missing data
df.loc[5] = [np.nan, 26, np.nan, np.nan]  # Add a row with missing values
print("\nDataFrame with missing data:\n", df)
df_filled = df.fillna({'Name': 'Unknown', 'Score': df['Score'].mean()})
print("\nDataFrame after filling missing data:\n", df_filled)

# 6. Statistical measure: Mean
mean_score = df['Score'].mean()
print("\nMean Score:", mean_score)

# 7. Sort data by 'Age'
sorted_df = df.sort_values(by='Age')
print("\nDataFrame sorted by Age:\n", sorted_df)

# 8. Merge two DataFrames
extra_data = pd.DataFrame({
    'Name': ['Alice', 'Bob'],
    'City': ['New York', 'Los Angeles']
})
merged_df = pd.merge(df, extra_data, on='Name', how='left')
print("\nMerged DataFrame:\n", merged_df)

# 9. Export data to CSV
df.to_csv('output.csv', index=False)
print("\nData exported to 'output.csv'.")

# 10. Apply lambda function
df['Age Group'] = df['Age'].apply(lambda x: 'Youth' if x < 30 else 'Adult')
print("\nDataFrame with 'Age Group' column:\n", df)