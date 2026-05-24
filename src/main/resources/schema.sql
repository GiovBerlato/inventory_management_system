CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Supplier (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    contact_number VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    supplier_id UUID NOT NULL,
    min_stock INT NOT NULL,

    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES Supplier(id)
);

CREATE TABLE IF NOT EXISTS Warehouse (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    max_capacity BIGINT NOT NULL,
    current_quantity BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_entries (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    quantity INT,

    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES Product(id),
    CONSTRAINT fk_warehouse FOREIGN KEY (warehouse_id) REFERENCES Warehouse(id)
);
