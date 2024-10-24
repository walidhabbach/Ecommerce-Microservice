#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Function to print messages
print_message() {
    echo -e "\n\033[1;32m$1\033[0m"
}

# Function to show a spinner while waiting
show_spinner() {
    local pid=$1
    local delay=0.1
    local spin='/-\|'

    while ps -p $pid > /dev/null; do
        for i in $(seq 0 3); do
            echo -ne "\r${spin:i:1} "
            sleep $delay
        done
    done
    echo -ne "\r"  # Clear the spinner line
}
# Check if Minikube is already running
if minikube status | grep -q "Running"; then
    print_message "Minikube is already running. Stopping and deleting the existing instance..."
    minikube stop > /dev/null 2>&1
    minikube delete > /dev/null 2>&1
fi

# Start Minikube
print_message "Starting Minikube with 6938 MiB memory and 4 CPUs..."
minikube start --memory 6938 --cpus 4 > /dev/null 2>&1 &  # Run in background
show_spinner $!  # Show spinner while Minikube starts

# Enable Minikube addons
print_message "Enabling add-ons: metrics-server, ingress, ingress-dns..."
minikube addons enable metrics-server 
minikube addons enable ingress  
minikube addons enable ingress-dns
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml

# Clone the VPA repository
print_message "Adding and updating Helm repository for Vertical Pod Autoscaler..."
if [ ! -d "autoscaler" ]; then
    git clone https://github.com/kubernetes/autoscaler.git
fi
cd autoscaler/vertical-pod-autoscaler/

# Install VPA using Helm
print_message "Installing Vertical Pod Autoscaler using Helm..."
bash ./hack/vpa-up.sh > /dev/null 2>&1

# Retrieve Minikube dashboard URL
print_message "Retrieving Minikube dashboard URL..."
minikube dashboard > /dev/null 2>&1 & 

# Start Minikube tunnel in the background
print_message "Starting Minikube tunnel in the background..."
minikube tunnel > /dev/null 2>&1 &
kubectl create namespace monitoring 
print_message "Minikube setup complete!"