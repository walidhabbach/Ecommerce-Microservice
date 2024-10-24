#!/bin/bash

# Function to check if curl command succeeded
check_success() {
  if [ $? -ne 0 ]; then
    echo "Error: Failed to perform request. Exiting."
    exit 1
  fi
}

# Read IP address
read -p "IP: " ip 

# Base URL and headers for requests
BASE_URL="http://$ip:8222/api"
CONTENT_TYPE_HEADER="Content-Type: application/json"

# Function to create a new entity and get its ID
create_entity() {
  local endpoint=$1
  local jsonData=$2
  curl -s -X POST "$BASE_URL/$endpoint" \
    -H "$CONTENT_TYPE_HEADER" \
    -d "$jsonData" | jq -r '.id'
}

# Create a new category and get its ID
categoryData='{
  "name": "category1"
}'
idCategory=$(create_entity "categories" "$categoryData")
check_success

# JSON data for creating products
productDataTemplate='{
  "description": "fzeef",
  "price": 1200.0,
  "name": "jcsfefac",
  "quantity": 2147483647,
  "category": {
    "id": '"$idCategory"'
  }
}'

# Create two products and get their IDs
idProduct1=$(create_entity "products" "$productDataTemplate")
check_success
idProduct2=$(create_entity "products" "$productDataTemplate")
check_success

# Create a new customer and get its ID
customerData='{
  "firstname": "ahmed",
  "lastname": "elmessaoudi",
  "email": "244adazd@gmail.com",
  "address": {
    "street": "Main Street",
    "houseNumber": "123",
    "zipCode": "12345"
  }
}'
idCustomer=$(create_entity "customers" "$customerData")
check_success

echo "Category ID: $idCategory"
echo "Product IDs: $idProduct1, $idProduct2"
echo "Customer ID: $idCustomer"

# Trap to handle script termination gracefully
trap 'echo "Script terminated."; exit' SIGINT SIGTERM

# Infinite loop to create orders
while true; do
  # JSON data for creating orders
  orderData='{
    "customerId": "'"$idCustomer"'",
    "orderLines": [
      { 
        "productId": '"$idProduct1"',
        "quantity": 1
      },
      { 
        "productId": '"$idProduct2"',
        "quantity": 1
      }
    ],
    "paymentMethod": "CREDIT_CARD"
  }'

  # Create an order and get its ID
  idOrder=$(create_entity "orders" "$orderData")
  check_success
  
  echo "Order ID: $idOrder"
  # Sleep for 1 second before the next iteration
  sleep 1
done
