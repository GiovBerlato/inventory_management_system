[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.pt-br.md)
[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/GiovBerlato/inventory_management_system/blob/main/README.md)
# API de Sistema de Gestão de Estoque

Um IMS desenvolvido em Java com o framework Spring Boot, utilizando PostgreSQL. Esta ferramenta ajuda a gerenciar o estoque de armazéns, permitindo que o usuário adicione diferentes tipos de produtos e respectivas entradas de estoque para esses produtos em qualquer armazém.

---

## Instalação

**Vá para a seção de Releases para instalar o .jar diretamente.**

Você também pode compilar e executar o programa localmente usando o wrapper do maven que já vem com o projeto, executando o seguinte comando no root do projeto:
```bash
./mvnw clean package
```
Certifique-se de que o banco de dados está funcionando, e então, é só rodar o arquivo .jar que aparece na pasta target. Alternativamente, você pode instalar o Docker (os arquivos `Dockerfile` e `compose.yaml` já vem prontos com o projeto), e executar a aplicação dentro de um container, usando o comando compose no root do projeto:
```bash
docker compose up --build
```
## Uso

Esse projeto utiliza do springdoc-openapi para documentar endpoints da API, campos e funcionalidades, acesse /swagger-ui/index.html enquanto o programa estiver rodando para mais informações.

## Contribuições
Pull requests, modificações e melhorias são bem-vindas. Não esqueça de atualizar/criar novos testes conforme necessário!
## Licença
Este projeto está sob a [Licença MIT](https://choosealicense.com/licenses/mit/).