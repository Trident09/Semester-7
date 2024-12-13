#!/bin/bash

# Define the target host (replace with the target hostname or IP)
TARGET_HOST="192.168.1.19"  # Replace with the desired IP or hostname

# Output directory to store scan results
OUTPUT_DIR="nmap_scan_results"
mkdir -p "$OUTPUT_DIR"

# Check if Nmap is installed
if ! command -v nmap &> /dev/null; then
    echo "Error: Nmap is not installed. Install it using 'brew install nmap' or equivalent."
    exit 1
fi

echo "Starting Nmap scans on $TARGET_HOST..."
echo "Results will be stored in the $OUTPUT_DIR directory."

# 1. Basic Ping Scan
echo "Performing basic ping scan..."
nmap -sn "$TARGET_HOST" > "$OUTPUT_DIR/ping_scan.txt"
echo "Ping scan completed. Results saved to $OUTPUT_DIR/ping_scan.txt"

# 2. Basic Port Scan (Default 1000 ports)
echo "Performing basic port scan..."
nmap "$TARGET_HOST" > "$OUTPUT_DIR/basic_port_scan.txt"
echo "Basic port scan completed. Results saved to $OUTPUT_DIR/basic_port_scan.txt"

# 3. Service and Version Detection
echo "Performing service and version detection..."
nmap -sV "$TARGET_HOST" > "$OUTPUT_DIR/service_version_detection.txt"
echo "Service and version detection completed. Results saved to $OUTPUT_DIR/service_version_detection.txt"

# 4. OS Detection
echo "Performing OS detection..."
sudo nmap -O "$TARGET_HOST" > "$OUTPUT_DIR/os_detection.txt"
echo "OS detection completed. Results saved to $OUTPUT_DIR/os_detection.txt"

# 5. Aggressive Scan (Combines multiple options)
echo "Performing aggressive scan..."
nmap -A "$TARGET_HOST" > "$OUTPUT_DIR/aggressive_scan.txt"
echo "Aggressive scan completed. Results saved to $OUTPUT_DIR/aggressive_scan.txt"

# 6. Scan All 65535 Ports
echo "Performing full port scan..."
nmap -p- "$TARGET_HOST" > "$OUTPUT_DIR/full_port_scan.txt"
echo "Full port scan completed. Results saved to $OUTPUT_DIR/full_port_scan.txt"

# 7. Script Scan (Default scripts)
echo "Performing script scan..."
nmap -sC "$TARGET_HOST" > "$OUTPUT_DIR/script_scan.txt"
echo "Script scan completed. Results saved to $OUTPUT_DIR/script_scan.txt"

# Display Summary of Results
echo "Nmap scans completed. Summary of output files:"
echo "1. Ping Scan: $OUTPUT_DIR/ping_scan.txt"
echo "2. Basic Port Scan: $OUTPUT_DIR/basic_port_scan.txt"
echo "3. Service and Version Detection: $OUTPUT_DIR/service_version_detection.txt"
echo "4. OS Detection: $OUTPUT_DIR/os_detection.txt"
echo "5. Aggressive Scan: $OUTPUT_DIR/aggressive_scan.txt"
echo "6. Full Port Scan: $OUTPUT_DIR/full_port_scan.txt"
echo "7. Script Scan: $OUTPUT_DIR/script_scan.txt"

echo "Review the results to understand the open ports, running services, operating system, and other details about the host."
