[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)
[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
# API de Sistema de Gestão de Estoque

Um IMS desenvolvido em Java com o framework Spring Boot, utilizando PostgreSQL. Esta ferramenta ajuda a gerenciar o estoque de armazéns, permitindo que o usuário adicione diferentes tipos de produtos e respectivas entradas de estoque para esses produtos em qualquer armazém.

---

## Arquitetura de Segurança e Autenticação

A aplicação possui um perímetro de segurança stateless baseado em JWT construído com **Spring Security** e **OAuth2 Resource Server**. O armazenamento de sessões foi desativado em favor de tokens assinados criptograficamente.

* **Criptografia Assimétrica (RS256)**: A aplicação faz uso de um par de chaves RSA (`private.pem` / `public.pem`). O `JwtService` assina os tokens com a Chave Privada, enquanto a SecurityFilterChain valida a assinatura das requisições via Chave Pública.
* **Controle de Acesso Baseado em Funções (RBAC)**: Direitos de acesso são aplicados tanto na camada de filtros HTTP quanto no nível dos métodos (`@PreAuthorize`).

### Hierarquia de Permissões
| Role | Permissões |
| :--- | :--- |
| `ROLE_USER` | Acesso de leitura (GET) em todos os recursos. |
| `ROLE_MANAGER` | Visualização, criação de registros e ajustes de estoque. |
| `ROLE_ADMIN` | Autorização administrativa total (incluindo operações restritas de DELETE). |

---

## Instalação

Você pode compilar e executar o programa localmente usando o wrapper do maven que já vem com o projeto, executando o seguinte comando no root do projeto:
```bash
./mvnw clean package
```
Certifique-se de que o banco de dados está funcionando, e então, é só rodar o arquivo .jar que aparece na pasta target. Alternativamente, você pode instalar o Docker (os arquivos `Dockerfile` e `compose.yaml` já vem prontos com o projeto), e executar a aplicação dentro de um container, usando o comando compose no root do projeto:
```bash
docker compose up --build
```
## Uso
É possível acessar todas as funcionalidades usando requisições HTTP, aqui estão exemplos usando curl (você também pode usar Postman ou httpie):

# Autenticação
```bash
# Cadastrar um novo usuário (Atribui ROLE_USER por padrão)
curl -X POST "http://localhost:8080/ims/auth/register" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "admin_user",
           "password": "securepassword123"
         }'

# Realizar login para obter um token JWT Bearer
curl -X POST "http://localhost:8080/ims/auth/login" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "admin_user",
           "password": "securepassword123"
         }'

# Exportar seu token JWT como uma variável de ambiente no Bash para testes
export TOKEN="seu_jwt_token_aqui"
```
### Fornecedores
```bash
# Adicionar um novo fornecedor (Requer ROLE_MANAGER ou ROLE_ADMIN)
curl -X POST "http://localhost:8080/ims/supplier" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "Sony Electronics",
           "address": "Tokyo, Japan",
           "contactNumber": "+81-3-6748-2111",
           "email": "contact@sony.com"
         }'

# Buscar um fornecedor pelo nome exato
curl -X GET "http://localhost:8080/ims/supplier?name=Sony%20Electronics" \
     -H "Authorization: Bearer $TOKEN"

# Listar todos os produtos fornecidos por um fornecedor específico
curl -X GET "http://localhost:8080/ims/supplier/Sony%20Electronics" \
     -H "Authorization: Bearer $TOKEN"

# Deletar um fornecedor pelo nome (Requer ROLE_ADMIN - Aviso: apaga em cascata seus produtos/estoques)
curl -X DELETE "http://localhost:8080/ims/supplier/Sony%20Electronics" \
     -H "Authorization: Bearer $TOKEN"
```
### Produtos
```bash
# Adicionar um produto (Requer ROLE_MANAGER ou ROLE_ADMIN, e um Fornecedor existente)
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

# Retornar todos os produtos
curl -X GET "http://localhost:8080/ims/products" \
     -H "Authorization: Bearer $TOKEN"

# Buscar um produto pelo seu SKU exato
curl -X GET "http://localhost:8080/ims/products/sku-search?sku=SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"

# Buscar produtos cujo nome contenha uma palavra-chave específica
curl -X GET "http://localhost:8080/ims/products/keyword-search?keyword=PlayStation" \
     -H "Authorization: Bearer $TOKEN"

# Listar produtos filtrados pelo seu tipo
curl -X GET "http://localhost:8080/ims/products/filter/ELECTRONICS" \
     -H "Authorization: Bearer $TOKEN"

# Atualizar um produto existente (Requer ROLE_MANAGER ou ROLE_ADMIN)
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

# Deletar um produto pelo SKU (Requer ROLE_ADMIN)
curl -X DELETE "http://localhost:8080/ims/products/SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"
```
### Armazéns
```bash
# Adicionar um armazém (Requer ROLE_MANAGER ou ROLE_ADMIN)
curl -X POST "http://localhost:8080/ims/warehouses" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "Central Warehouse",
           "location": "123 Main St, New York",
           "maxCapacity": 5000
         }'

# Retornar todos os armazéns
curl -X GET "http://localhost:8080/ims/warehouses" \
     -H "Authorization: Bearer $TOKEN"

# Buscar um armazém pelo nome exato
curl -X GET "http://localhost:8080/ims/warehouses/filter?name=Central%20Warehouse" \
     -H "Authorization: Bearer $TOKEN"

# Buscar armazéns cujo nome contenha uma palavra-chave específica
curl -X GET "http://localhost:8080/ims/warehouses/keyword-search?keyword=Central" \
     -H "Authorization: Bearer $TOKEN"

# Atualizar um armazém existente (Requer ROLE_MANAGER ou ROLE_ADMIN)
curl -X PUT "http://localhost:8080/ims/warehouses/Central%20Warehouse" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "name": "Central Warehouse",
           "location": "456 New St, New York",
           "maxCapacity": 8000
         }'

# Deletar um armazém pelo nome (Requer ROLE_ADMIN)
curl -X DELETE "http://localhost:8080/ims/warehouses/Central%20Warehouse" \
     -H "Authorization: Bearer $TOKEN"
```
### Entradas de Estoque
```bash
# Adicionar uma nova entrada de estoque (Requer ROLE_MANAGER ou ROLE_ADMIN)
curl -X POST "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "supplierName": "Sony Electronics",
           "quantity": 100
         }'

# Ajustar a quantidade de estoque (Requer ROLE_MANAGER ou ROLE_ADMIN)
curl -X PATCH "http://localhost:8080/ims/stock" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{
           "productSKU": "SONY-PS5",
           "warehouseName": "Central Warehouse",
           "quantityToAdjust": -15
         }'

# Buscar uma entrada de estoque específica (Pelo Nome do Armazém e SKU do Produto)
curl -X GET "http://localhost:8080/ims/stock/Central%20Warehouse/SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"

# Buscar todas as entradas de estoque dentro de um armazém específico
curl -X GET "http://localhost:8080/ims/stock/warehouse?name=Central%20Warehouse" \
     -H "Authorization: Bearer $TOKEN"

# Buscar todas as entradas de estoque em todos os armazéns para um produto específico
curl -X GET "http://localhost:8080/ims/stock/products?sku=SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"

# Deletar uma entrada de estoque (Requer ROLE_ADMIN)
curl -X DELETE "http://localhost:8080/ims/stock/Central%20Warehouse/SONY-PS5" \
     -H "Authorization: Bearer $TOKEN"
```
## Contribuições
Pull requests, modificações e melhorias são bem-vindas. Não esqueça de atualizar/criar novos testes conforme necessário!
## Licença
Este projeto está sob a [Licença MIT](https://choosealicense.com/licenses/mit/).