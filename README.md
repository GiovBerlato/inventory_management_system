[![en](https://img.shields.io/badge/lang-en-red.svg)](https://https://github.com/GiovBerlato/inventory_management_system/edit/main/README.md)
[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/edit/main/README.pt-br.md)

# Inventory Management System API

An IMS written with Java and the Spring Boot framework, with data being stored in PostgreSQL. This tool helps with managing the inventory of warehouses, with the user being able to add different kinds of products and respective stock entries for those products in any warehouse.

## Installation

You can build and run the program locally using the maven wrapper that is provided with the program, by using the following command on the root folder of the project:

```bash
./mvnw clean package
```
Then, just run the .jar inside the target folder.

Alternatively, you can install [Docker](https://www.docker.com/) (Pre-made Dockerfile and compose.yaml files are also provided with this project), and run the application inside a container, using the compose command on the root folder:

```bash
docker compose up --build
```

## Usage

You can call all the functionalities using HTTP requests, here are examples using [curl](https://curl.se/download.html) (You may also use [Postman](https://www.postman.com/downloads/) or [httpie](https://httpie.io/)):

### Products
```bash
# Add a product
curl -X POST "http://localhost:8080/ims/products" \
     -H "Content-Type: application/json" \
     -d '{
           "sku": "ABC-123",
           "name": "Videogame",
           "type": "ELECTRONICS",
           "price": 299.99
         }'

# Return all products added to the database
curl -X GET "http://localhost:8080/ims/products"

# Search for products who's name contains a specific keyword
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword={keyword}"
# Ex:
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword=fork"

# Search for a product by it's SKU
curl -X GET "http://localhost:8080/ims/products/sku-search?sku={sku}"
# Ex:
curl -X GET "http://localhost:8080/ims/products/sku-search?sku=ABC-123"

# List products filtered by their type
curl -X GET "http://localhost:8080/ims/products/filter/{type}"
# Ex:
curl -X GET "http://localhost:8080/ims/products/filter/ELECTRONICS"

# Update an existing product's content (search by SKU)
curl -X PUT "http://localhost:8080/ims/products/ABC-123" \
     -H "Content-Type: application/json" \
     -d '{
           "sku": "ABC-123",
           "name": "Updated Videogame",
           "type": "ELECTRONICS",
           "price": 399.99
         }'

# Deleting a product (search by SKU)
curl -X DELETE "http://localhost:8080/ims/products/ABC-123"
```

### Warehouses

```bash
# Add a warehouse
curl -X POST http://localhost:8080/ims/warehouses \
     -H "Content-Type: application/json" \
     -d '{
           "id": "a43b5-342n-12hgs",
           "name": "Central Warehouse",
           "location": "123 Main St",
           "capacity": 1000
         }'

# Return all warehouses
curl -X GET http://localhost:8080/ims/warehouses

# Search for a warehouse by exact name
curl -X GET "http://localhost:8080/ims/warehouses/name-search?name={name}"
# Ex:
curl -X GET "http://localhost:8080/ims/warehouses/name-search?name=Central Warehouse"

# Search for warehouses whose name contains a specific keyword
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword={keyword}"
# Ex:
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword=central"

# Search for a warehouse by UUID
curl -X GET "http://localhost:8080/ims/warehouses/id-search?id={uuid}"
# Ex:
curl -X GET "http://localhost:8080/ims/warehouses/id-search?id=a43b5-342n-12hgs"

# Update an existing warehouse (search by name)
curl -X PUT "http://localhost:8080/ims/warehouses/Central Warehouse" \
     -H "Content-Type: application/json" \
     -d '{
           "id": "a43b5-342n-12hgs",
           "name": "Updated Warehouse Name",
           "location": "456 New St",
           "capacity": 1200
         }'

# Delete a warehouse by name
curl -X DELETE "http://localhost:8080/ims/warehouses/Central Warehouse"

```

### Stock Entries

```bash
# Get all stock entries inside a specific warehouse
curl -X GET "http://localhost:8080/ims/stock/warehouse?id={warehouseId}"
# Ex:
curl -X GET "http://localhost:8080/ims/stock/warehouse?id=123e4567-e89b-12d3-a456-426614174000"

# Get all stock entries for a specific product
curl -X GET "http://localhost:8080/ims/stock/products?id={productId}"
# Ex:
curl -X GET "http://localhost:8080/ims/stock/products?id=123e4567-e89b-12d3-a456-426614174000"

# Get a stock entry for a specific product in a specific warehouse
curl -X GET "http://localhost:8080/ims/stock/{warehouseId}/{productId}"
# Ex:
curl -X GET "http://localhost:8080/ims/stock/123e4567-e89b-12d3-a456-426614174000/987e6543-e21b-65d4-a987-426614174999"

# Add a new stock entry (requires existing Product and Warehouse)
curl -X POST "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -d '{
           "productId": "123e4567-e89b-12d3-a456-426614174000",
           "warehouseId": "987e6543-e21b-65d4-a987-426614174999",
           "quantity": 100
         }'

# Delete a stock entry by its ID
curl -X DELETE "http://localhost:8080/ims/stock/{stockEntryId}"

# Adjust stock quantity by stockEntryId (positive to increase, negative to decrease)
curl -X PATCH "http://localhost:8080/ims/stock/{stockEntryId}?quantity={amount}"
# Ex:
curl -X PATCH "http://localhost:8080/ims/stock/123e4567-e89b-12d3-a456-426614174000?quantity=50"

```
## Contributing

Pull requests, modifications and improvements are welcome.

Make sure to update/make new tests accordingly!

## License

This project is under the [MIT License](https://choosealicense.com/licenses/mit/).
