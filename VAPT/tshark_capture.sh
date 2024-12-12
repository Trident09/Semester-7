#!/bin/bash

# Check for root privileges
if [ "$EUID" -ne 0 ]; then
    echo "Please run as root."
    exit 1
fi

# Check if an interface is provided
if [ "$#" -lt 1 ]; then
    echo "Usage: $0 <network_interface> [output_file]"
    exit 1
fi

INTERFACE=$1
OUTPUT_FILE=${2:-capture.pcap}

# Start TShark to capture packets on the specified interface
echo "Starting TShark on interface $INTERFACE..."
tshark -i $INTERFACE -w $OUTPUT_FILE

# Notify the user where the capture is saved
echo "Packet capture saved to $OUTPUT_FILE."