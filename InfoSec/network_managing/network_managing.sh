#!/bin/bash

# Define the output directory
OUTPUT_DIR="network_monitoring_output"
mkdir -p "$OUTPUT_DIR"

# Check if required tools are available
for cmd in netstat lsof ifconfig tcpdump pfctl; do
    if ! command -v $cmd &> /dev/null; then
        echo "Error: $cmd is not installed. Please install it before running this script."
        exit 1
    fi
done

echo "Monitoring and managing network connections on macOS..."
echo "Output will be saved in the $OUTPUT_DIR directory."

# 1. List active network connections using netstat
echo "Listing active network connections..."
netstat -an > "$OUTPUT_DIR/active_connections.txt"
echo "Active network connections saved to $OUTPUT_DIR/active_connections.txt"

# 2. Show processes using network ports using lsof
echo "Listing processes using network ports..."
sudo lsof -i -n -P > "$OUTPUT_DIR/processes_using_ports.txt"
echo "Processes using network ports saved to $OUTPUT_DIR/processes_using_ports.txt"

# 3. Display network interface configuration using ifconfig
echo "Displaying network interface configurations..."
ifconfig > "$OUTPUT_DIR/network_interface_config.txt"
echo "Network interface configurations saved to $OUTPUT_DIR/network_interface_config.txt"

# 4. Capture network traffic using tcpdump (Default interface)
echo "Capturing network traffic using tcpdump..."
DEFAULT_INTERFACE=$(route get default | grep interface | awk '{print $2}')
TCPDUMP_DURATION=10  # Capture for 10 seconds
sudo tcpdump -i "$DEFAULT_INTERFACE" -w "$OUTPUT_DIR/network_traffic.pcap" -G "$TCPDUMP_DURATION" -W 1
echo "Network traffic captured and saved to $OUTPUT_DIR/network_traffic.pcap"

# 5. Display current firewall rules using pfctl
echo "Displaying current firewall rules..."
sudo pfctl -sr > "$OUTPUT_DIR/firewall_rules.txt"
echo "Firewall rules saved to $OUTPUT_DIR/firewall_rules.txt"

# 6. Block or allow traffic using pfctl (Example)
echo "Managing traffic with pfctl..."
BLOCK_IP="192.168.1.100"  # Example IP to block
echo "Blocking traffic to/from IP: $BLOCK_IP"
echo "block drop from $BLOCK_IP to any" | sudo pfctl -f -
sudo pfctl -e  # Enable the packet filter
echo "Traffic to/from $BLOCK_IP is now blocked. Modify this script to adjust rules."

# 7. Display a summary of monitoring results
echo "Network Monitoring Completed. Summary of results:"
echo "1. Active connections: $OUTPUT_DIR/active_connections.txt"
echo "2. Processes using ports: $OUTPUT_DIR/processes_using_ports.txt"
echo "3. Network interfaces: $OUTPUT_DIR/network_interface_config.txt"
echo "4. Captured traffic: $OUTPUT_DIR/network_traffic.pcap"
echo "5. Firewall rules: $OUTPUT_DIR/firewall_rules.txt"

# Provide cleanup option for pfctl
echo "To reset the firewall rules, run: sudo pfctl -F all -d"
