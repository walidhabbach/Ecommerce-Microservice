#!/bin/bash

# Define color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' 

# Define valid images
VALID_IMAGES=("config" "discovery" "product" "payment" "notification" "order" "customer" "api-gateway")

# Function to display usage information
usage() {
    echo -e "${YELLOW}Usage: $0 <image|all> <action> <tag>${NC}"
    echo -e "${YELLOW}Actions: build | delete${NC}"
    echo -e "${YELLOW}Valid images: ${VALID_IMAGES[*]}${NC}"
    exit 1
}

# Function to check if the provided image is valid
is_valid_image() {
    local image=$1
    for valid_image in "${VALID_IMAGES[@]}"; do
        if [[ "$valid_image" == "$image" ]]; then
            return 0
        fi
    done
    return 1
}

# Function to build the Docker image
build_image() {
    local image=$1
    local tag=$2
    local dir="./$image-service"  # Directory for the specific image

    if [ $image == "api-gateway" ]; then
        dir="./$image"
    fi

    echo -e "${YELLOW}Building image: $image in directory: $dir ${NC}"
    
    # Change to the image directory
    if [ -d "$dir" ]; then
        cd "$dir" || { echo -e "${RED}Failed to enter directory: $dir${NC}"; exit 1; }
        
        if docker build -t shogun101/ecommerce-"$image"-service:"$tag" .; then
            echo -e "${GREEN}Successfully built: $image${NC}"
            if docker push shogun101/ecommerce-"$image"-service:"$tag"; then
                echo -e "${GREEN}Successfully pushed: $image${NC}"
            else
                echo -e "${RED}Error pushing: $image${NC}" >&2
                exit 1
            fi
        else
            echo -e "${RED}Error building: $image${NC}" >&2
            exit 1
        fi
        
        # Return to the original directory
        cd - > /dev/null || exit 1
    else
        echo -e "${RED}Directory does not exist: $dir${NC}" >&2
        exit 1
    fi
}

# Function to delete the Docker image
delete_image() {
    local image=$1
    local tag=$2
    echo -e "${YELLOW}Deleting image: $image...${NC}"
    if docker rmi shogun101/ecommerce-"$image"-service:"$tag" 2>/dev/null; then
        echo -e "${GREEN}Successfully deleted: $image${NC}"
    else
        echo -e "${YELLOW}Image not found or could not be deleted: $image. Skipping.${NC}"
    fi
}

# Function to manage the Docker image based on action
manage_image() {
    local image=$1
    local action=$2
    local tag=$3

    # Check if image and action are provided as arguments
    if [ -z "$image" ] || [ -z "$action" ]; then
        usage
    fi

    echo -e "${YELLOW}Starting $action operation for $image image...${NC}"
    
    # Validate the image name
    if ! is_valid_image "$image"; then
        echo -e "${RED}Invalid image: $image. Valid options are: ${VALID_IMAGES[*]}${NC}" >&2
        exit 1
    fi

    if [ "$action" == "build" ]; then
        build_image "$image" "$tag"
    elif [ "$action" == "delete" ]; then
        delete_image "$image" "$tag"
    else
        echo -e "${RED}Invalid action: $action. Use 'build' or 'delete'.${NC}" >&2
        exit 1
    fi   

    echo -e "${GREEN}$action operation for $image completed successfully.${NC}"
}

# Function to manage all images
manage_all_images() {
    local action=$1
    local tag=$2
    
    for image in "${VALID_IMAGES[@]}"; do
        manage_image "$image" "$action" "$tag"
    done
}

# Main script execution
if [ "$#" -lt 2 ]; then
    usage
fi

image="$1"
action="$2"
tag="$3"

if [ "$image" == "all" ]; then
    manage_all_images "$action" "$tag"
else
    manage_image "$image" "$action" "$tag"
fi