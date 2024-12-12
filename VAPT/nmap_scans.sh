#!/bin/bash

# Check if target is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <target>"
    exit 1
fi

TARGET=$1

# Perform a TCP Scan
echo "Running TCP Scan on $TARGET..."
nmap -sT $TARGET -oN tcp_scan.txt

echo "TCP Scan completed. Results saved in tcp_scan.txt."

# Perform a UDP Scan
echo "Running UDP Scan on $TARGET..."
nmap -sU $TARGET -oN udp_scan.txt

echo "UDP Scan completed. Results saved in udp_scan.txt."

# Perform Version Detection
echo "Running Version Detection on $TARGET..."
nmap -sV $TARGET -oN version_scan.txt

echo "Version Detection completed. Results saved in version_scan.txt."

# Perform OS Detection
echo "Running OS Detection on $TARGET..."
nmap -O $TARGET -oN os_detection.txt

echo "OS Detection completed. Results saved in os_detection.txt."

echo "All scans completed. Check the respective .txt files for results."
