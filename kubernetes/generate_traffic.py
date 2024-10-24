import requests
import json
import time
import sys
import threading

# Event to signal threads to stop
stop_event = threading.Event()

def check_success(response):
    """Check if the HTTP request was successful."""
    if response.status_code not in (200, 201):  # Accept 200 and 201 as successful status codes
        print(f"Error: Failed to perform request. Status code: {response.status_code}")
        print(f"Response: {response.text}")
        sys.exit(1)

def create_entity(endpoint, json_data):
    """Create a new entity via POST request and return its ID."""
    url = f"{BASE_URL}/{endpoint}"
    headers = {"Content-Type": "application/json"}
    response = requests.post(url, headers=headers, json=json_data)
    check_success(response)
    return response.json().get("id")

def create_order(id_customer, id_product1, id_product2):
    """Function to create an order repeatedly in a thread."""
    while not stop_event.is_set():
        order_data = {
            "customerId": id_customer,
            "orderLines": [
                {"productId": id_product1, "quantity": 1},
                {"productId": id_product2, "quantity": 1}
            ],
            "paymentMethod": "CREDIT_CARD"
        }
        # Create an order and get its ID
        id_order = create_entity("orders", order_data)
        print(f"Order ID: {id_order}")
        time.sleep(1)

# Read IP address
ip = input("IP: ")

# Base URL for requests
BASE_URL = f"http://{ip}:8222/api"

# Create a new category and get its ID
category_data = {"name": "category1"}
id_category = create_entity("categories", category_data)

# JSON data for creating products
product_data_template = {
    "description": "fzeef",
    "price": 1200.0,
    "name": "jcsfefac",
    "quantity": 2147483647,
    "category": {"id": id_category}
}

# Create two products and get their IDs
id_product1 = create_entity("products", product_data_template)
id_product2 = create_entity("products", product_data_template)

# Create a new customer and get its ID
customer_data = {
    "firstname": "ahmed",
    "lastname": "elmessaoudi",
    "email": "244adazd@gmail.com",
    "address": {
        "street": "Main Street",
        "houseNumber": "123",
        "zipCode": "12345"
    }
}
id_customer = create_entity("customers", customer_data)

print(f"Category ID: {id_category}")
print(f"Product IDs: {id_product1}, {id_product2}")
print(f"Customer ID: {id_customer}")

# Number of threads to create orders
num_threads = 2  # Adjust this number based on your need

threads = []

# Create threads to create orders in parallel
for _ in range(num_threads):
    thread = threading.Thread(target=create_order, args=(id_customer, id_product1, id_product2))
    thread.start()
    threads.append(thread)

def signal_handler(signal, frame):
    print("Script terminated by user.")
    stop_event.set()  # Signal all threads to stop

import signal
signal.signal(signal.SIGINT, signal_handler)  # Handle Ctrl+C

try:
    # Wait for all threads to complete
    for thread in threads:
        thread.join()
except KeyboardInterrupt:
    # Handle case where KeyboardInterrupt is raised
    print("Script terminated by user.")
finally:
    # Ensure all threads are terminated
    stop_event.set()
    for thread in threads:
        thread.join()
    print("All threads have been terminated.")
