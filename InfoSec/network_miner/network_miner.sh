#!/bin/bash

# Define the capture duration and interface
CAPTURE_DURATION=60  # capture for 60 seconds
CAPTURE_INTERFACE="eth0"  # Network interface to capture traffic (change as needed)
CAPTURE_FILE="capture.pcap"  # Capture file name
OUTPUT_DIR="networkminer_output"  # Directory to save NetworkMiner's output

# Check if tcpdump is installed
if ! command -v tcpdump &> /dev/null
then
    echo "tcpdump could not be found. Please install it first."
    exit 1
fi

# Check if NetworkMiner is installed
if ! command -v NetworkMiner &> /dev/null
then
    echo "NetworkMiner could not be found. Please install it first."
    exit 1
fi

# Create a directory for output
mkdir -p "$OUTPUT_DIR"

# Capture packets using tcpdump
echo "Starting packet capture on interface $CAPTURE_INTERFACE for $CAPTURE_DURATION seconds..."
sudo tcpdump -i "$CAPTURE_INTERFACE" -w "$CAPTURE_FILE" -G "$CAPTURE_DURATION" -W 1

# Check if capture was successful
if [ $? -eq 0 ]; then
    echo "Packet capture completed successfully. Analyzing the capture with NetworkMiner..."
else
    echo "Packet capture failed. Exiting..."
    exit 1
fi

# Analyze the capture with NetworkMiner
echo "Running NetworkMiner on the capture file..."
NetworkMiner -r "$CAPTURE_FILE" -d "$OUTPUT_DIR"

# Check if analysis was successful
if [ $? -eq 0 ]; then
    echo "NetworkMiner analysis completed. Results are in the directory: $OUTPUT_DIR"
else
    echo "NetworkMiner analysis failed. Please check for errors."
    exit 1
fi

# Clean up the capture file after analysis (optional)
rm "$CAPTURE_FILE"
echo "Capture file removed."
