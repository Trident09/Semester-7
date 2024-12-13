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
    echo "Example: $0 eth0 10"
    exit 1
fi

# Network interface to capture on
INTERFACE=$1

# Capture duration in seconds (default to 10 seconds if not provided)
DURATION=${2:-10}

# Output file for captured data
OUTPUT_FILE="tcp_capture.pcap"

# Start the capture
echo "Starting TCP capture on interface $INTERFACE for $DURATION seconds..."
tshark -i "$INTERFACE" -a duration:"$DURATION" -w "$OUTPUT_FILE"

# Check if the capture was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to capture packets. Check your interface and permissions."
    exit 1
fi

echo "Capture complete. Analyzing TCP connections..."

# Analyze the captured file for TCP statistics
echo "TCP Connection Analysis:"
tshark -r "$OUTPUT_FILE" -q -z conv,tcp

# Analyze TCP streams
echo -e "\nTCP Stream Information:"
tshark -r "$OUTPUT_FILE" -q -z follow,tcp,ascii,0

# Optional: Display a summary of the packets
echo -e "\nPacket Summary:"
tshark -r "$OUTPUT_FILE" -q -z io,phs

# Clean up
echo -e "\nAnalysis complete. The raw capture file is saved as $OUTPUT_FILE."
echo "You can open it in Wireshark for detailed analysis if needed."