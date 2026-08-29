[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)

# Inventory Management System API

An IMS written with Java and the Spring Boot framework, with data being stored in PostgreSQL. This tool helps with managing the inventory of warehouses, with the user being able to add different kinds of products and respective stock entries for those products in any warehouse.

---

## Features

- Create and manage warehouses, determining aspects such as max capacity and available items.
- Role hierarchy (User, Manager, Admin), each having their respective authorities and clearance to use the API's functions.
- Also creating and managing products, suppliers, and respective stock entries for each warehouse you create.

## Installation

**Refer to the Releases section to install the .jar directly.**

You can also build and run the program locally by using the maven wrapper that is provided with the program, by using the following command on the root folder of the project:

```bash
./mvnw clean package
```

Make sure you have your PostgreSQL database running before you run the jar file.

You can also install Docker (Pre-made Dockerfile and compose.yaml files are also provided with this project) and run the application inside a container, using the compose command on the root folder:

```bash
docker compose up --build
```

## Usage

This project utilizes springdoc-openapi to document API endpoints, fields and functionalities, go to /swagger-ui/index.html when running the program for more information.

## Contributing

Pull requests, modifications and improvements are welcome.

Make sure to update/make new tests accordingly!
## License

This project is under the [MIT License](https://choosealicense.com/licenses/mit/).