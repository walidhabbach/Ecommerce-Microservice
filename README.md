# Ecommerce Microservice on AKS

This repository contains the source code and Kubernetes deployment manifests for an e-commerce platform using microservices architecture. The application is deployed on **Azure Kubernetes Service (AKS)** with monitoring and continuous deployment managed through **Argo CD**.

## Project Structure

Ecommerce-Microservice-AKS```plaintext
│
├───.idea                      # IDE configuration files
├───kubernetes                  # Kubernetes manifests and infrastructure configurations
│   ├───infrastructure
│   │   ├───alert-manager       # AlertManager configuration for alerting
│   │   ├───argocd              # Argo CD application configurations
│   │   │   └───application     # Argo CD application definitions
│   │   ├───configurations      # Metrics and VPA configurations
│   │   │   ├───metrics-configs # Metrics setup for monitoring
│   │   │   └───vpa             # Vertical Pod Autoscaler configurations
│   │   ├───grafana             # Grafana monitoring dashboards
│   │   ├───kafka               # Kafka for event streaming
│   │   ├───prometheus          # Prometheus monitoring setup
│   │   ├───zipkin              # Zipkin for distributed tracing
│   │   └───zookeeper           # Zookeeper for Kafka coordination
│   ├───services
│   │   ├───api-gateway         # API Gateway service
│   │   ├───config              # Configuration service
│   │   ├───customer            # Customer service
│   │   ├───discovery           # Service discovery (Eureka)
│   │   ├───notification        # Notification service
│   │   ├───order               # Order service
│   │   ├───payment             # Payment service
│   │   └───product             # Product service
│   └───terraform               # Terraform configurations for AKS
├───services                    # Source code for each microservice
└───kube-layer-manager.sh       # Script to automate Kubernetes deployments



## Microservices Overview

This e-commerce platform consists of several microservices, each responsible for specific functionality:
- **API Gateway**: Routes requests to appropriate backend services.
- **Config Service**: Centralized configuration management for all services.
- **Customer Service**: Manages customer-related data.
- **Discovery Service**: Service discovery (Eureka) for locating services dynamically.
- **Notification Service**: Handles notifications to customers.
- **Order Service**: Manages orders and related processes.
- **Payment Service**: Processes payments and transactions.
- **Product Service**: Manages product inventory and details.

## Kubernetes Setup
Prerequisites
- **Azure** Kubernetes Service (AKS) is used as the orchestrator.
- **kubectl** and Helm must be installed locally.
- **Terraform** is used to provision infrastructure resources on AKS.
Deployment
- **Clone this repository:**: 
  ```bash
  git clone https://github.com/your-repo/ecommerce-microservice-aks.git
  cd ecommerce-microservice-aks
The `kube-layer-manager.sh` script automates the deployment and management of Kubernetes manifests. It supports various commands such as:
- **Deploying all services**: 
  ```bash
  ./kube-layer-manager.sh apply all
- **Deleting services**: 
  ```bash
  bash kube-layer-manager.sh delete <service-name>
- **Applying specific layers (services , infrastructure):**
  ```bash
  bash kube-layer-manager.sh apply infrastructure
This script simplifies the deployment and management process for different Kubernetes resources.


The application is deployed on AKS using Kubernetes manifests stored in the `kubernetes` folder. Key infrastructure and service files include:

- **Infrastructure**:
  - **Prometheus**: Used for monitoring and scraping application metrics.
  - **Grafana**: Dashboards for visualizing metrics.
  - **Alert Manager**: Alerting based on Prometheus rules.
  - **Argo CD**: Continuous deployment to automate Kubernetes management.
  - **Kafka & Zookeeper**: Event streaming and coordination.
  - **Zipkin**: Distributed tracing for monitoring microservice requests.

- **Services**: Each microservice has its own deployment manifest, including:
  - StatefulSets or Deployments for service containers.
  - Persistent volumes for data storage.
  - Auto-scaling configurations.
  - Service discovery and load balancing.

## Monitoring Setup

Prometheus and Grafana are set up to monitor the health of the microservices:
- **Prometheus**: Collects metrics from services and stores them.
- **Grafana**: Provides real-time dashboards for viewing metrics like response times, error rates, and resource usage.
- **Zipkin**: Distributed tracing tool for monitoring the flow of requests across microservices.
- To access Grafana:
  ```bash
  kubectl port-forward svc/grafana 3000:3000
Then, open http://localhost:3000 in your browser and log in with the default credentials (admin/admin).

## Continuous Deployment with Argo CD

**Argo CD** is used to automate the deployment of services. Changes pushed to this repository are automatically applied to the AKS cluster via Argo CD's GitOps approach.

Deployment manifests for Argo CD are located in the `kubernetes/infrastructure/argocd/application` directory.
- **Install Argo CD:**
  ```bash
  kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
- **To access the Argo CD UI:**
  ```bash
  kubectl port-forward svc/argocd-server -n argocd 8080:443
- **Set up your application in Argo CD to manage Kubernetes manifests:**
  ```bash
  kubectl apply -f kubernetes/infrastructure/argocd/application/your-app.yaml
Argo CD will automatically deploy the manifests stored in the repository, keeping your microservices synchronized with Git.


## Conclusion

This repository provides a comprehensive setup for deploying an e-commerce microservice application on AKS with monitoring and GitOps. 
