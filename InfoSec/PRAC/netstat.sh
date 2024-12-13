#!/bin/bash

# Display all active connections and listening ports
echo "Active Connections and Listening Ports:"
netstat -a
echo ""

# Display the routing table
echo "Routing Table:"
netstat -r
echo ""

# Display network interface statistics
echo "Network Interface Statistics:"
netstat -i
echo ""

# Display TCP connections
echo "TCP Connections:"
netstat -at
echo ""

# Display UDP connections
echo "UDP Connections:"
netstat -au
echo ""

# Display network statistics
echo "Network Statistics:"
netstat -s
echo ""

# Echo a custom message
echo "Netstat information displayed successfully."