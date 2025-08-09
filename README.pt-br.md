[![en](https://img.shields.io/badge/lang-en-red.svg)](https://https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)

---

# API de Sistema de Gestão de Estoque

Um IMS desenvolvido em Java com o framework Spring Boot, utilizando PostgreSQL. Esta ferramenta ajuda a gerenciar o estoque de armazéns, permitindo que o usuário adicione diferentes tipos de produtos e respectivas entradas de estoque para esses produtos em qualquer armazém.

## Instalação

Você pode compilar e executar o programa localmente usando o wrapper do maven que já vem com o projeto, executando o seguinte comando no root do projeto:

```bash
./mvnw clean package
```

E então, é só rodar o arquivo .jar que aparece na pasta target.

Alternativamente, você pode instalar o [Docker](https://www.docker.com/) (os arquivos Dockerfile e compose.yaml já vem prontos com o projeto), e executar a aplicação dentro de um container, usando o comando compose no root do projeto:

```bash
docker compose up --build
```

## Uso

É possível acessar todas as funcionalidades usando requisições HTTP, aqui estão exemplos usando [curl](https://curl.se/download.html) (você também pode usar [Postman](https://www.postman.com/downloads/) ou [httpie](https://httpie.io/)):

### Produtos

```bash
# Adicionar um produto
curl -X POST "http://localhost:8080/ims/products" \
     -H "Content-Type: application/json" \
     -d '{
           "sku": "ABC-123",
           "name": "Videogame",
           "type": "ELECTRONICS",
           "price": 299.99
         }'

# Retornar todos os produtos cadastrados no banco
curl -X GET "http://localhost:8080/ims/products"

# Buscar produtos cujo nome tenha uma palavra-chave específica
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword={keyword}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword=fork"

# Buscar um produto pelo seu SKU
curl -X GET "http://localhost:8080/ims/products/sku-search?sku={sku}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/products/sku-search?sku=ABC-123"

# Listar produtos filtrados pelo tipo
curl -X GET "http://localhost:8080/ims/products/filter/{type}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/products/filter/ELECTRONICS"

# Atualizar o conteúdo de um produto existente (busca pelo SKU)
curl -X PUT "http://localhost:8080/ims/products/ABC-123" \
     -H "Content-Type: application/json" \
     -d '{
           "sku": "ABC-123",
           "name": "Videogame Atualizado",
           "type": "ELECTRONICS",
           "price": 399.99
         }'

# Deletar um produto (busca pelo SKU)
curl -X DELETE "http://localhost:8080/ims/products/ABC-123"
```

### Armazéns

```bash
# Adicionar um armazém
curl -X POST "http://localhost:8080/ims/warehouses" \
     -H "Content-Type: application/json" \
     -d '{
           "id": "a43b5-342n-12hgs",
           "name": "Armazém Central",
           "location": "Rua Principal, 123",
           "capacity": 1000
         }'

# Retornar todos os armazéns
curl -X GET "http://localhost:8080/ims/warehouses"

# Buscar um armazém pelo nome exato
curl -X GET "http://localhost:8080/ims/warehouses/name-search?name={name}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/warehouses/name-search?name=Armazém Central"

# Buscar armazéns cujo nome contenha uma palavra-chave específica
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword={keyword}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword=central"

# Buscar um armazém pelo UUID
curl -X GET "http://localhost:8080/ims/warehouses/id-search?id={uuid}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/warehouses/id-search?id=a43b5-342n-12hgs"

# Atualizar um armazém existente (busca pelo nome)
curl -X PUT "http://localhost:8080/ims/warehouses/Armazém Central" \
     -H "Content-Type: application/json" \
     -d '{
           "id": "a43b5-342n-12hgs",
           "name": "Nome Atualizado do Armazém",
           "location": "Rua Nova, 456",
           "capacity": 1200
         }'

# Deletar um armazém pelo nome
curl -X DELETE "http://localhost:8080/ims/warehouses/Armazém Central"
```

### Entradas de Estoque

```bash
# Buscar todas as entradas de estoque dentro de um armazém específico
curl -X GET "http://localhost:8080/ims/stock/warehouse?id={warehouseId}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/stock/warehouse?id=123e4567-e89b-12d3-a456-426614174000"

# Buscar todas as entradas de estoque para um produto específico
curl -X GET "http://localhost:8080/ims/stock/products?id={productId}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/stock/products?id=123e4567-e89b-12d3-a456-426614174000"

# Buscar uma entrada de estoque para um produto específico em um armazém específico
curl -X GET "http://localhost:8080/ims/stock/{warehouseId}/{productId}"
# Exemplo:
curl -X GET "http://localhost:8080/ims/stock/123e4567-e89b-12d3-a456-426614174000/987e6543-e21b-65d4-a987-426614174999"

# Adicionar uma nova entrada de estoque (requer Produto e Armazém existentes)
curl -X POST "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -d '{
           "productId": "123e4567-e89b-12d3-a456-426614174000",
           "warehouseId": "987e6543-e21b-65d4-a987-426614174999",
           "quantity": 100
         }'

# Deletar uma entrada de estoque pelo seu ID
curl -X DELETE "http://localhost:8080/ims/stock/{stockEntryId}"

# Ajustar a quantidade do estoque pelo stockEntryId (positivo para aumentar, negativo para diminuir)
curl -X PATCH "http://localhost:8080/ims/stock/{stockEntryId}?quantity={amount}"
# Exemplo:
curl -X PATCH "http://localhost:8080/ims/stock/123e4567-e89b-12d3-a456-426614174000?quantity=50"
```

## Contribuições

Pull requests, modificações e melhorias são bem-vindas.

Não esqueça de atualizar/criar novos testes conforme necessário!

## Licença

Este projeto está sob a [Licença MIT](https://choosealicense.com/licenses/mit/).

---
