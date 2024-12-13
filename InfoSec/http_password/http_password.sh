#!/bin/bash

# Check if tshark is installed
if ! command -v tshark &> /dev/null
then
    echo "Error: tshark is not installed. Please install it first."
    exit 1
fi

# Check if the user provided an interface
if [ -z "$1" ]; then
    echo "Usage: $0 <network_interface> [capture_duration]"
    echo "Example: $0 eth0 20"
    exit 1
fi

# Network interface to capture traffic on
INTERFACE=$1

# Capture duration in seconds (default to 10 seconds if not provided)
DURATION=${2:-10}

# Output file for captured data
OUTPUT_FILE="http_password_traffic.pcap"

# Filter to capture only HTTP traffic
FILTER="tcp port 80"

# Start the capture
echo "Capturing HTTP traffic on interface $INTERFACE for $DURATION seconds..."
tshark -i "$INTERFACE" -a duration:"$DURATION" -f "$FILTER" -w "$OUTPUT_FILE"

# Check if the capture was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to capture packets. Check your interface and permissions."
    exit 1
fi

echo "Capture complete. Analyzing HTTP traffic for plaintext passwords..."

# Analyze HTTP POST requests with potential plaintext credentials
echo -e "\nPotential plaintext passwords in HTTP POST requests:"
tshark -r "$OUTPUT_FILE" -Y "http.request.method == \"POST\" && frame contains \"uname=\" && frame contains \"pass=\"" -T fields -e ip.src -e ip.dst -e http.file_data

# Provide additional information about the capture
echo -e "\nSummary of HTTP traffic:"
tshark -r "$OUTPUT_FILE" -q -z http,stat

# Display selected packets in a readable format
echo -e "\nDisplaying selected packets containing plaintext credentials:"
tshark -r "$OUTPUT_FILE" -Y "http.request.method == \"POST\" && frame contains \"uname=\" && frame contains \"pass=\"" -V

# Clean up and output location of the capture file
echo -e "\nHTTP traffic analysis complete. The raw capture file is saved as $OUTPUT_FILE."
echo "You can open it in Wireshark for further analysis if needed."