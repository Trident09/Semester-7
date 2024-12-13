#!/bin/bash

# Define output file names
OUTPUT_DIR="netstat_output"
mkdir -p "$OUTPUT_DIR"

# Check if netstat is installed
if ! command -v netstat &> /dev/null
then
    echo "netstat could not be found. Please install it first."
    exit 1
fi

# View information about incoming and outgoing network connections
echo "Displaying incoming and outgoing network connections..."
netstat -tuln > "$OUTPUT_DIR/network_connections.txt"
echo "Network connections saved to $OUTPUT_DIR/network_connections.txt"

# View routing tables
echo "Displaying routing tables..."
netstat -r > "$OUTPUT_DIR/routing_table.txt"
echo "Routing table saved to $OUTPUT_DIR/routing_table.txt"

# View interface statistics
echo "Displaying interface statistics..."
netstat -i > "$OUTPUT_DIR/interface_statistics.txt"
echo "Interface statistics saved to $OUTPUT_DIR/interface_statistics.txt"

# Display detailed network statistics
echo "Displaying detailed network statistics (TCP, UDP, etc.)..."
netstat -s > "$OUTPUT_DIR/network_statistics.txt"
echo "Network statistics saved to $OUTPUT_DIR/network_statistics.txt"

# Display active connections with PID
echo "Displaying active connections with process IDs..."
netstat -tulpn > "$OUTPUT_DIR/active_connections_pid.txt"
echo "Active connections with PID saved to $OUTPUT_DIR/active_connections_pid.txt"

# Summary completion message
echo "Netstat information has been saved to the '$OUTPUT_DIR' directory."
