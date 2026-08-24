
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS supplier (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    contact_number VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    sku VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    supplier_id UUID NOT NULL,
    min_stock INT NOT NULL,

    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES Supplier(id)
);

CREATE TABLE IF NOT EXISTS warehouse (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL UNIQUE,
    max_capacity BIGINT NOT NULL check (max_capacity > 0),
    current_quantity BIGINT NOT NULL check (
        current_quantity >= 0
        AND current_quantity <= max_capacity
    )
);

CREATE TABLE IF NOT EXISTS stock_entries (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    quantity INT NOT NULL check (quantity >= 0),
    CONSTRAINT uq_stock_product_warehouse
    UNIQUE (product_id, warehouse_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES Product(id),
    CONSTRAINT fk_warehouse FOREIGN KEY (warehouse_id) REFERENCES Warehouse(id)
);