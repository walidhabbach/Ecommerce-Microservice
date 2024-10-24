#!/bin/bash

# Retrieve the token and decode it
kubernetes_token=$(kubectl get secret dashboard-user-token -n kubernetes-dashboard -o jsonpath="{.data.token}" | base64 --decode)

# # Set up port forwarding and store the process IDs
kubernetes_job=$(kubectl port-forward -n kubernetes-dashboard service/kubernetes-dashboard-kong-proxy 8443:443 > /dev/null 2>&1 & echo $!)
argocd_job=$(kubectl port-forward -n argocd service/argocd-server 8080:80 > /dev/null 2>&1 & echo $!)

prometheus_job=$(kubectl port-forward -n monitoring service/prometheus-service  9090:9090 > /dev/null 2>&1 & echo $!)
grafana_job=$(kubectl port-forward -n monitoring service/grafana-service  3000:3000 > /dev/null 2>&1 & echo $!)
discovery_job=$(kubectl port-forward service/discovery-service 8761:8761 > /dev/null 2>&1 & echo $!)
zipkin_job=$(kubectl port-forward service/zipkin-service 9411:9411 > /dev/null 2>&1 & echo $!)


# Retrieve the Argo CD initial admin password
password_argocd=$(kubectl get secret argocd-initial-admin-secret -n argocd -o yaml -o jsonpath="{.data.password}")
password_argocd=$(echo $password_argocd | base64 --decode)
# Save the token and password to a file in valid JSON format
cat <<EOF > vars.json
{
  "kubernetes": {
    "token": "$kubernetes_token",
    "port": 8443,
    "job": $kubernetes_job
  },
  "argocd": {
    "username": "admin",
    "password": "$password_argocd",
    "port": 8080,
    "job": $argocd_job 
  },
  
  "prometheus": {
    "port": 9090,
    "job": $prometheus_job
  },
   
  "grafana": {
    "username": "admin",
    "password": "admin",
    "port": 3000,
    "job": $grafana_job
  },
  "discovery": {
    "port": 8761,
    "job": $discovery_job
  },
  "zipkin": {
    "port": 9411,
    "job": $zipkin_job
  }
}
EOF
# Function to kill the background processes
cleanup() {
  # Check if the process IDs are valid before killing them
  if ps -p $kubernetes_job > /dev/null; then
  kill $kubernetes_job
  fi 
  if ps -p $argocd_job > /dev/null; then 
    kill $argocd_job
  fi
  if ps -p $zipkin_job > /dev/null; then 
    kill $zipkin_job
  fi
  if ps -p $prometheus_job > /dev/null; then
    kill $prometheus_job
  fi

  if ps -p $grafana_job > /dev/null; then
    kill $grafana_job
  fi

  if ps -p $discovery_job > /dev/null; then
    kill $discovery_job
  fi
}

# Trap the EXIT signal to execute the cleanup function
trap cleanup EXIT

# Wait for user input to keep the script running
read -p "Press Enter to exit..."