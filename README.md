[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)

# Inventory Management System API

An IMS written with Java and the Spring Boot framework, with data being stored in PostgreSQL. This tool helps with managing the inventory of warehouses, with the user being able to add different kinds of products and respective stock entries for those products in any warehouse.

---

## Security & Authentication Architecture

This application features a completely stateless JWT-based security perimeter built with **Spring Security** and **OAuth2 Resource Server**. Session storage is disabled in favor of cryptographically signed tokens.

* **Asymmetric Encryption (RS256):** The application uses an RSA keypair (`private.pem` / `public.pem`). The `JwtService` signs tokens using the Private Key, while the `SecurityFilterChain` verifies incoming request signatures using the Public Key.
* **Role-Based Access Control (RBAC):** Access rights are enforced both at the HTTP filter level and via method-level annotations (`@PreAuthorize`).

### Role Hierarchy
| Role | Permissions & Capabilities |
| :--- | :--- |
| `ROLE_USER` | Read-only access (`GET`) across resources. |
| `ROLE_MANAGER` | View resources, create items, and perform stock adjustments. |
| `ROLE_ADMIN` | Full administrative authorization (including restricted `DELETE` operations). |

---

## Installation

You can build and run the program locally using the maven wrapper that is provided with the program, by using the following command on the root folder of the project:

```bash
./mvnw clean package
```

Make sure you have your PostgreSQL database running before you run the jar file.

You can also install Docker (Pre-made Dockerfile and compose.yaml files are also provided with this project) and run the application inside a container, using the compose command on the root folder:

```bash
docker compose up --build
```

## Usage

You can call all the functionalities using HTTP requests, here are examples using curl (You may also use Postman or httpie):

### Authentication
```bash
# Register a new account (Assigns ROLE_USER by default)
curl -X POST "http://localhost:8080/ims/auth/register" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "admin_user",
           "password": "securepassword123"
         }'

# Log in to obtain a JWT Bearer token
curl -X POST "http://localhost:8080/ims/auth/login" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "admin_user",
           "password": "securepassword123"
         }'

# Export your JWT token as an environment variable in Bash for testing
export TOKEN="your_jwt_token_here"
```

### Suppliers
```bash

# Add a new supplier (Requires ROLE_MANAGER or ROLE_ADMIN)
curl -X POST "http://localhost:8080/ims/supplier" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "Sony Electronics",
           "address": "Tokyo, Japan",
           "contactNumber": "+81-3-6748-2111",
           "email": "contact@sony.com"
         }'

# Search for a supplier by exact name
curl -X GET "http://localhost:8080/ims/supplier?name=Sony%20Electronics" \
     -H "Authorization: Bearer $TOKEN"

# List all products provided by a specific supplier
curl -X GET "http://localhost:8080/ims/supplier/Sony%20Electronics" \
     -H "Authorization: Bearer $TOKEN"

# Delete a supplier by name (Requires ROLE_ADMIN - Warning: cascades and deletes products/stocks)
curl -X DELETE "http://localhost:8080/ims/supplier/Sony%20Electronics" \
     -H "Authorization: Bearer $TOKEN"
```
### Products
```bash
# Add a product (Requires ROLE_MANAGER or ROLE_ADMIN, and an existing Supplier)
curl -X POST "http://localhost:8080/ims/products" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "PlayStation 5",
           "sku": "SONY-PS5",
           "type": "ELECTRONICS",
           "price": 499.99,
           "minimumStock": 10,
           "supplierName": "Sony Electronics"
         }'

# Return all products
curl -X GET "http://localhost:8080/ims/products" \
     -H "Authorization: Bearer $TOKEN"

# Search for a product by its exact SKU
curl -X GET "http://localhost:8080/ims/products/sku-search?sku=SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"

# Search for products whose name contains a specific keyword
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword=PlayStation" \
     -H "Authorization: Bearer $TOKEN"

# List products filtered by their type
curl -X GET "http://localhost:8080/ims/products/filter/ELECTRONICS" \
     -H "Authorization: Bearer $TOKEN"

# Update an existing product (Requires ROLE_MANAGER or ROLE_ADMIN)
curl -X PUT "http://localhost:8080/ims/products/SONY-PS5" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "PlayStation 5 Pro",
           "sku": "SONY-PS5",
           "type": "ELECTRONICS",
           "price": 599.99,
           "minimumStock": 5,
           "supplierName": "Sony Electronics"
         }'

# Delete a product by SKU (Requires ROLE_ADMIN)
curl -X DELETE "http://localhost:8080/ims/products/SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"
```
### Warehouses
```bash

# Add a warehouse (Requires ROLE_MANAGER or ROLE_ADMIN)
curl -X POST "http://localhost:8080/ims/warehouses" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "Central Warehouse",
           "location": "123 Main St, New York",
           "maxCapacity": 5000
         }'

# Return all warehouses
curl -X GET "http://localhost:8080/ims/warehouses" \
     -H "Authorization: Bearer $TOKEN"

# Search for a warehouse by exact name
curl -X GET "http://localhost:8080/ims/warehouses/filter?name=Central%20Warehouse" \
     -H "Authorization: Bearer $TOKEN"

# Search for warehouses whose name contains a specific keyword
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword=Central" \
     -H "Authorization: Bearer $TOKEN"

# Update an existing warehouse (Requires ROLE_MANAGER or ROLE_ADMIN)
curl -X PUT "http://localhost:8080/ims/warehouses/Central%20Warehouse" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "Central Warehouse",
           "location": "456 New St, New York",
           "maxCapacity": 8000
         }'

# Delete a warehouse by name (Requires ROLE_ADMIN)
curl -X DELETE "http://localhost:8080/ims/warehouses/Central%20Warehouse" \
     -H "Authorization: Bearer $TOKEN"
```
### Stock Entries
```bash

# Add a new stock entry (Requires ROLE_MANAGER or ROLE_ADMIN)
curl -X POST "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "supplierName": "Sony Electronics",
           "quantity": 100
         }'

# Adjust stock quantity (Requires ROLE_MANAGER or ROLE_ADMIN)
curl -X PATCH "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "quantityToAdjust": -15
         }'

# Get a specific stock entry (By Warehouse Name and Product SKU)
curl -X GET "http://localhost:8080/ims/stock/Central%20Warehouse/SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"

# Get all stock entries inside a specific warehouse
curl -X GET "http://localhost:8080/ims/stock/warehouse?name=Central%20Warehouse" \
     -H "Authorization: Bearer $TOKEN"

# Get all stock entries across all warehouses for a specific product
curl -X GET "http://localhost:8080/ims/stock/products?sku=SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"

# Delete a stock entry (Requires ROLE_ADMIN)
curl -X DELETE "http://localhost:8080/ims/stock/Central%20Warehouse/SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"
```
## Contributing

Pull requests, modifications and improvements are welcome.

Make sure to update/make new tests accordingly!
## License

This project is under the [MIT License](https://choosealicense.com/licenses/mit/).