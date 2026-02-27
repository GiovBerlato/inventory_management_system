[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)

---

# API de Sistema de Gestão de Estoque

Um IMS desenvolvido em Java com o framework Spring Boot, utilizando PostgreSQL. Esta ferramenta ajuda a gerenciar o estoque de armazéns, permitindo que o usuário adicione diferentes tipos de produtos e respectivas entradas de estoque para esses produtos em qualquer armazém.

## Instalação

Você pode compilar e executar o programa localmente usando o wrapper do maven que já vem com o projeto, executando o seguinte comando no root do projeto:

```bash
./mvnw clean package
```

Certifique-se de que o banco de dados está funcionando, e então, é só rodar o arquivo .jar que aparece na pasta target.

Alternativamente, você pode instalar o [Docker](https://www.docker.com/) (os arquivos Dockerfile e compose.yaml já vem prontos com o projeto), e executar a aplicação dentro de um container, usando o comando compose no root do projeto:

```bash
docker compose up --build
```

## Uso

É possível acessar todas as funcionalidades usando requisições HTTP, aqui estão exemplos usando [curl](https://curl.se/download.html) (você também pode usar [Postman](https://www.postman.com/downloads/) ou [httpie](https://httpie.io/)):

### Fornecedores
```bash
# Adicionar um novo fornecedor
curl -X POST "http://localhost:8080/ims/supplier" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Sony Electronics",
           "address": "Tokyo, Japan",
           "contactNumber": "+81-3-6748-2111",
           "email": "contact@sony.com"
         }'

# Buscar um fornecedor pelo nome exato
curl -X GET "http://localhost:8080/ims/supplier?name=Sony Electronics"

# Listar todos os produtos fornecidos por um fornecedor específico
curl -X GET "http://localhost:8080/ims/supplier/Sony Electronics"

# Deletar um fornecedor pelo nome (Aviso: apaga em cascata seus produtos/estoques)
curl -X DELETE "http://localhost:8080/ims/supplier/Sony Electronics"
```
### Produtos

```bash
# Adicionar um produto (Requer um Fornecedor existente)
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

# Retornar todos os produtos
curl -X GET "http://localhost:8080/ims/products"

# Buscar um produto pelo seu SKU exato
curl -X GET "http://localhost:8080/ims/products/sku-search?sku=SONY-PS5"

# Buscar produtos cujo nome contenha uma palavra-chave específica
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword=PlayStation"

# Listar produtos filtrados pelo seu tipo
curl -X GET "http://localhost:8080/ims/products/filter/ELECTRONICS"

# Atualizar um produto existente (Busca pelo SKU, deve incluir todos os campos)
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

# Deletar um produto pelo SKU
curl -X DELETE "http://localhost:8080/ims/products/SONY-PS5"
```

### Armazéns

```bash
# Adicionar um armazém
curl -X POST "http://localhost:8080/ims/warehouses" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Central Warehouse",
           "location": "123 Main St, New York",
           "maxCapacity": 5000
         }'

# Retornar todos os armazéns
curl -X GET "http://localhost:8080/ims/warehouses"

# Buscar um armazém pelo nome exato
curl -X GET "http://localhost:8080/ims/warehouses/filter?name=Central Warehouse"

# Buscar armazéns cujo nome contenha uma palavra-chave específica
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword=Central"

# Atualizar um armazém existente (Busca pelo nome)
curl -X PUT "http://localhost:8080/ims/warehouses/Central Warehouse" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Central Warehouse",
           "location": "456 New St, New York",
           "maxCapacity": 8000
         }'

# Deletar um armazém pelo nome
curl -X DELETE "http://localhost:8080/ims/warehouses/Central Warehouse"
```

### Entradas de Estoque

```bash
# Adicionar uma nova entrada de estoque (Requer Produto, Armazém e Fornecedor existentes)
curl -X POST "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "supplierName": "Sony Electronics",
           "quantity": 100
         }'

# Ajustar a quantidade de estoque (Positivo para aumentar, negativo para diminuir)
curl -X PATCH "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "quantityToAdjust": -15
         }'

# Buscar uma entrada de estoque específica (Pelo Nome do Armazém e SKU do Produto)
curl -X GET "http://localhost:8080/ims/stock/Central Warehouse/SONY-PS5"

# Buscar todas as entradas de estoque dentro de um armazém específico
curl -X GET "http://localhost:8080/ims/stock/warehouse?name=Central Warehouse"

# Buscar todas as entradas de estoque em todos os armazéns para um produto específico
curl -X GET "http://localhost:8080/ims/stock/products?sku=SONY-PS5"

# Deletar uma entrada de estoque
curl -X DELETE "http://localhost:8080/ims/stock/Central Warehouse/SONY-PS5"
```

## Contribuições

Pull requests, modificações e melhorias são bem-vindas.

Não esqueça de atualizar/criar novos testes conforme necessário!

## Licença

Este projeto está sob a [Licença MIT](https://choosealicense.com/licenses/mit/).

---
