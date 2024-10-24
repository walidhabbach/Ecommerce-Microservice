#!/bin/bash

# Define color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' 

# Function to apply Kubernetes manifests
apply_manifest() { 
    local manifest=$1
    local namespace=$2
 
    echo -e "${YELLOW}Applying manifest: $manifest...${NC}"
    if kubectl apply -f "$manifest" -n $namespace; then
        echo -e "${GREEN}Successfully applied: $manifest${NC}\n"
    else
        echo -e "${RED}Error applying: $manifest${NC}" >&2
        exit 1
    fi
}

# Function to delete Kubernetes manifests
delete_manifest() {
    local manifest=$1
    local namespace=$2
    echo -e "${YELLOW}Deleting manifest: $manifest...${NC}"
    if kubectl delete -f "$manifest" -n $namespace --grace-period=0 --force 2>/dev/null; then
        echo -e "${GREEN}Successfully deleted: $manifest${NC}\n"
    else
        echo -e "${YELLOW}Manifest not found or could not be deleted: $manifest. Skipping.${NC}\n"
    fi
}

# Function to apply or delete Kubernetes manifests
manage_layer_manifests() {
    local layer=$1
    local action=$2
    local namespace="default"
    # Check if layer and action are provided as arguments
    if [ -z "$layer" ] || [ -z "$action" ]; then
        echo -e "${RED}Usage: $0 <layer> <action>${NC}" >&2
        exit 1
    fi

    echo -e "${YELLOW}Starting $action operation for $layer manifests...${NC}\n"

    if [ "$layer" == "config" ]; then
        local manifests=(
            "./services/$layer/service.yaml"
            "./services/$layer/deployment.yaml"
        )
    elif [ "$layer" == "api-gateway" ]; then
        local manifests=(  
            "./services/$layer/deployment.yaml"
             "./services/$layer/service.yaml"
            # "./services/$layer/ingress.yaml" 
    )
    elif [ "$layer" == "customer" ]; then
        local manifests=( 
            "./services/$layer/mongo/service.yaml" 
            "./services/$layer/mongo/statefulset.yaml"
            "./services/$layer/service.yaml"
            "./services/$layer/deployment.yaml"
            "./services/$layer/volume/pv.yaml"
            "./services/$layer/volume/pvc.yaml"
            
            
        )
    elif [ "$layer" == "discovery" ]; then
        local manifests=( 
            "./services/$layer/deployment.yaml" 
        )
    elif [ "$layer" == "notification" ]; then
        local manifests=( 
            "./services/$layer/volume/pv.yaml"
            "./services/$layer/volume/pvc.yaml"
            "./services/$layer/mongo/service.yaml" 
            "./services/$layer/mongo/statefulset.yaml"
            "./services/$layer/service.yaml"
            "./services/$layer/deployment.yaml"
        )
        
    elif [ "$layer" == "product" ]; then
        local manifests=(
            "./services/$layer/volume/pv.yaml"
            "./services/$layer/volume/pvc.yaml"
            "./services/$layer/postgres/service.yaml" 
            "./services/$layer/postgres/statefulset.yaml"
            "./services/$layer/service.yaml"
            "./services/$layer/deployment.yaml"
          
        )
    elif [ "$layer" == "payment" ]; then
        local manifests=(
             "./services/$layer/volume/pv.yaml"
            "./services/$layer/volume/pvc.yaml"
            "./services/$layer/postgres/service.yaml" 
            "./services/$layer/postgres/statefulset.yaml"
            "./services/$layer/service.yaml"
            "./services/$layer/deployment.yaml"
        
        )
  
    elif [ "$layer" == "order" ]; then
        local manifests=(
            "./services/$layer/volume"
            "./services/$layer/postgres/"
            "./services/$layer/"
            "./services/$layer/auto-scaling"  
            
        )
    elif [ "$layer" == "kafka" ]; then
        local manifests=(
            "./infrastructure/$layer/service.yaml" 
            "./infrastructure/$layer/statefulset.yaml" 
            "./infrastructure/$layer/storage.yaml" 
        )
    # elif [ "$layer" == "keycloak" ]; then
    #     local manifests=(
    #         "./infrastructure/$layer/deployment.yaml" 
    #     )
    elif [ "$layer" == "configurations" ]; then
        namespace="kube-system"
        local manifests=( 
             "./infrastructure/$layer/metrics-configs"
             "./infrastructure/$layer/vpa"  
        )
    elif [ "$layer" == "zipkin" ]; then
        local manifests=(
            "./infrastructure/$layer/deployment.yaml" 
        )
    elif [ "$layer" == "argocd" ]; then
        namespace="argocd"
        local manifests=(
            "./infrastructure/$layer/namespace.yaml" 
            "./infrastructure/$layer/install.yaml" 
            "./infrastructure/$layer/application/infrastructures.yaml"
            "./infrastructure/$layer/application/services.yaml"  
        )
    elif [ "$layer" == "zookeeper" ]; then
        local manifests=(
            "./infrastructure/$layer/networkPolicy.yaml" 
            "./infrastructure/$layer/service.yaml" 
            "./infrastructure/$layer/statefulset.yaml"
            "./infrastructure/$layer/storage.yaml" 
        )
    elif [ "$layer" == "grafana" ]; then
        namespace="monitoring"
        local manifests=( 
            "./infrastructure/$layer/config-map.yaml" 
            "./infrastructure/$layer/service.yaml"  
            "./infrastructure/$layer/deployment.yaml"
        )
    elif [ "$layer" == "prometheus" ]; then
        namespace="monitoring"
        local manifests=( 
            "./infrastructure/$layer/namespace.yaml"
            "./infrastructure/$layer/clusterRole.yaml" 
            "./infrastructure/$layer/config-map.yaml" 
            "./infrastructure/$layer/service.yaml"
            "./infrastructure/$layer/deployment.yaml"
            
            "./infrastructure/$layer/operator"
            "./infrastructure/$layer/adapter/setup"
            "./infrastructure/$layer/adapter/resource-metrics"
            "./infrastructure/$layer/adapter/customer-metrics"

        )
    elif [ "$layer" == "alert-manager" ]; then
        namespace="monitoring"
        local manifests=(
            "./infrastructure/$layer/template-config-map.yaml" 
            "./infrastructure/$layer/config-map.yaml" 
            "./infrastructure/$layer/service.yaml"
            "./infrastructure/$layer/ingress.yaml"
            "./infrastructure/$layer/deployment.yaml"
        )
    elif [ "$layer" == "dashboard" ]; then
        namespace="kubernetes-dashboard"
        local manifests=(
            "./infrastructure/$layer/namespace.yaml" 
            "./infrastructure/$layer/user.yaml" 
            "./infrastructure/$layer/user-token.yaml" 
            "./infrastructure/$layer/clusterrolebinding.yaml"
        )
        if [  "$action" == "delete" ]; then
            helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
            helm uninstall kubernetes-dashboard --namespace kubernetes-dashboard
        else
            helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
            helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard
        fi

    else
        echo -e "${RED}Invalid layer: $layer. Valid options are 'backend', 'frontend', 'mongo', 'cronjob' ,'job', or 'all'.${NC}" >&2
        exit 1
    fi
 
    for manifest in "${manifests[@]}"; do
        if [ "$action" == "apply" ]; then
            apply_manifest "$manifest" $namespace
        elif [ "$action" == "delete" ]; then
            delete_manifest "$manifest" $namespace
        else
            echo -e "${RED}Invalid action: $action. Use 'apply' or 'delete'.${NC}" >&2
            exit 1
        fi
    done

    echo -e "${GREEN}$action operation for $layer manifests completed successfully.${NC}\n"
}

 

# Function to manage manifests based on input
manage_manifests() {
    local layer=$1
    local action=$2
    if [ "$layer" == "services" ]; then 
        echo -e "${RED}IN SURE TO CREATE OR DELETE NEEDED SECRET" 
        read -p "Press Enter to continue..."
        for layer in config discovery product payment notification order customer api-gateway ; do
            manage_layer_manifests "$layer" "$action"
        done
    elif [ "$layer" == "infrastructure" ]; then 
        echo -e "${RED}IN SURE TO CREATE OR DELETE NEEDED SECRET" 
        read -p "Press Enter to continue..."
        for layer in configurations zookeeper kafka zipkin dashboard prometheus grafana; do
            manage_layer_manifests "$layer" "$action"
        done
    else
        manage_layer_manifests "$layer" "$action"
    fi
}

# Main script execution
manage_manifests "$1" "$2"
