[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)

# Inventory Management System API

An IMS written with Java and the Spring Boot framework, with data being stored in PostgreSQL. This tool helps with managing the inventory of warehouses, with the user being able to add different kinds of products and respective stock entries for those products in any warehouse.

## Installation

You can build and run the program locally using the maven wrapper that is provided with the program, by using the following command on the root folder of the project:

```bash
./mvnw clean package
```
Make sure you have the database up before packaging, then, just run the .jar inside the target folder.

Alternatively, you can install [Docker](https://www.docker.com/) (Pre-made Dockerfile and compose.yaml files are also provided with this project), and run the application inside a container, using the compose command on the root folder:

```bash
docker compose up --build
```

## Usage

You can call all the functionalities using HTTP requests, here are examples using [curl](https://curl.se/download.html) (You may also use [Postman](https://www.postman.com/downloads/) or [httpie](https://httpie.io/)):

### Suppliers
```bash
# Add a new supplier
curl -X POST "http://localhost:8080/ims/supplier" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Sony Electronics",
           "address": "Tokyo, Japan",
           "contactNumber": "+81-3-6748-2111",
           "email": "contact@sony.com"
         }'

# Search for a supplier by exact name
curl -X GET "http://localhost:8080/ims/supplier?name=Sony Electronics"

# List all products provided by a specific supplier
curl -X GET "http://localhost:8080/ims/supplier/Sony Electronics"

# Delete a supplier by name (Warning: cascades and deletes their products/stocks)
curl -X DELETE "http://localhost:8080/ims/supplier/Sony Electronics"
```
### Products
```bash
# Add a product (Requires an existing Supplier)
curl -X POST "http://localhost:8080/ims/products" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "PlayStation 5",
           "sku": "SONY-PS5",
           "type": "ELECTRONICS",
           "price": 499.99,
           "minimumStock": 10,
           "supplierName": "Sony Electronics"
         }'

# Return all products
curl -X GET "http://localhost:8080/ims/products"

# Search for a product by its exact SKU
curl -X GET "http://localhost:8080/ims/products/sku-search?sku=SONY-PS5"

# Search for products whose name contains a specific keyword
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword=PlayStation"

# List products filtered by their type
curl -X GET "http://localhost:8080/ims/products/filter/ELECTRONICS"

# Update an existing product (Search by SKU, must include all fields)
curl -X PUT "http://localhost:8080/ims/products/SONY-PS5" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "PlayStation 5 Pro",
           "sku": "SONY-PS5",
           "type": "ELECTRONICS",
           "price": 599.99,
           "minimumStock": 5,
           "supplierName": "Sony Electronics"
         }'

# Delete a product by SKU
curl -X DELETE "http://localhost:8080/ims/products/SONY-PS5"
```

### Warehouses

```bash
# Add a warehouse
curl -X POST "http://localhost:8080/ims/warehouses" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Central Warehouse",
           "location": "123 Main St, New York",
           "maxCapacity": 5000
         }'

# Return all warehouses
curl -X GET "http://localhost:8080/ims/warehouses"

# Search for a warehouse by exact name
curl -X GET "http://localhost:8080/ims/warehouses/filter?name=Central Warehouse"

# Search for warehouses whose name contains a specific keyword
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword=Central"

# Update an existing warehouse (Search by name)
curl -X PUT "http://localhost:8080/ims/warehouses/Central Warehouse" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Central Warehouse",
           "location": "456 New St, New York",
           "maxCapacity": 8000
         }'

# Delete a warehouse by name
curl -X DELETE "http://localhost:8080/ims/warehouses/Central Warehouse"
```

### Stock Entries

```bash
# Add a new stock entry (Requires existing Product, Warehouse, and Supplier)
curl -X POST "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "supplierName": "Sony Electronics",
           "quantity": 100
         }'

# Adjust stock quantity (Positive to increase, negative to decrease)
curl -X PATCH "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "quantityToAdjust": -15
         }'

# Get a specific stock entry (By Warehouse Name and Product SKU)
curl -X GET "http://localhost:8080/ims/stock/Central Warehouse/SONY-PS5"

# Get all stock entries inside a specific warehouse
curl -X GET "http://localhost:8080/ims/stock/warehouse?name=Central Warehouse"

# Get all stock entries across all warehouses for a specific product
curl -X GET "http://localhost:8080/ims/stock/products?sku=SONY-PS5"

# Delete a stock entry
curl -X DELETE "http://localhost:8080/ims/stock/Central Warehouse/SONY-PS5"
```
## Contributing

Pull requests, modifications and improvements are welcome.

Make sure to update/make new tests accordingly!

## License

This project is under the [MIT License](https://choosealicense.com/licenses/mit/).
