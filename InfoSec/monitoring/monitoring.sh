#!/bin/bash

# Define output file names
OUTPUT_DIR="macos_netstat_output"
mkdir -p "$OUTPUT_DIR"

# Check if the required tools are available
if ! command -v netstat &> /dev/null
then
    echo "netstat could not be found. Please install it first."
    exit 1
fi

if ! command -v lsof &> /dev/null
then
    echo "lsof could not be found. Please install it first."
    exit 1
fi

if ! command -v iftop &> /dev/null
then
    echo "iftop could not be found. Please install it using 'brew install iftop'."
    exit 1
fi

if ! command -v tcpdump &> /dev/null
then
    echo "tcpdump could not be found. Please install it using 'brew install tcpdump'."
    exit 1
fi

# Monitor network connections using netstat
echo "Displaying active TCP/IP network connections using netstat..."
netstat -an | grep 'tcp' > "$OUTPUT_DIR/netstat_connections.txt"
echo "TCP/IP network connections saved to $OUTPUT_DIR/netstat_connections.txt"

# Monitor the process using lsof (list open files)
echo "Displaying processes and their associated network connections using lsof..."
sudo lsof -i -n -P > "$OUTPUT_DIR/lsof_connections.txt"
echo "Processes and network connections saved to $OUTPUT_DIR/lsof_connections.txt"

# Monitor network interface statistics using iftop
echo "Displaying real-time network bandwidth usage using iftop..."
# iftop can be run interactively, so we'll capture the output for 10 seconds
sudo iftop -t -s 10 > "$OUTPUT_DIR/iftop_output.txt"
echo "Real-time network bandwidth usage saved to $OUTPUT_DIR/iftop_output.txt"

# Monitor network traffic using tcpdump (Capture packets on default interface)
echo "Capturing network traffic using tcpdump on default network interface..."
# Set the capture duration and output file name
CAPTURE_DURATION=10  # capture for 10 seconds
sudo tcpdump -i en0 -w "$OUTPUT_DIR/tcpdump_capture.pcap" -G "$CAPTURE_DURATION" -W 1
echo "Network traffic captured and saved to $OUTPUT_DIR/tcpdump_capture.pcap"

# Summary message
echo "Monitoring of TCP/IP network connections is complete. Outputs are saved in the '$OUTPUT_DIR' directory."
