
<h1 align="center" style="font-weight: bold;">Shop System 💻</h1>


## 📖 Índice

* [Descrição](#descrição)

* [Desenvolvimento](#desenvolvimento)
  
* [Tecnologias Utilizadas](#tecnologias-utilizadas)

* [Estrutura de pastas](#estrutura-de-pastas)

* [Execução](#execução)

* [Acesso](#acesso)

* [Arquitetura](#️arquitetura)

* [Dificuldades](#dificuldades)

* [Desenvolvedores](#desenvolvedores)


## Descrição

Sistema de gerenciamento de pedidos com suporte para mensagens RabbitMQ, permitindo colocação, processamento, atualizações e notificações de pedidos sem problemas.



## Desenvolvimento





## Tecnologias Utilizadas

![spring][SPRING_BADGE]
![java][JAVA_BADGE]
![mysql][MYSQL_BADGE]
![lombok][LOMBOK_BADGE]
![jpa][JPA_BADGE]
![junit][JUNIT_BADGE]
![flyway][FLYWAY_BADGE]
![springsecurity][SPRING_SECURITY_BADGE]
![thymeleaf][THYMELEAF_BADGE]
![rabbitmq][RABBITMQ_BADGE]

[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[MYSQL_BADGE]:https://img.shields.io/badge/MySQL-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white
[LOMBOK_BADGE]:https://img.shields.io/badge/Lombok-%2318A558.svg?style=for-the-badge&logo=lombok&logoColor=white
[JPA_BADGE]:https://img.shields.io/badge/JPA-%2300A3E0.svg?style=for-the-badge&logo=java&logoColor=white
[JUNIT_BADGE]:https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white
[FLYWAY_BADGE]:https://img.shields.io/badge/Flyway-%2300A6A0.svg?style=for-the-badge&logo=flyway&logoColor=white
[SPRING_SECURITY_BADGE]:https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=Spring-Security&logoColor=white
[THYMELEAF_BADGE]:https://img.shields.io/badge/Thymeleaf-005F0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white
[RABBITMQ_BADGE]:https://img.shields.io/badge/-RabbitMQ-FF6600.svg?style=for-the-badge&logo=rabbitmq&logoColor=white

<br>

<br>

## Estrutura de pastas

<br>

<br>

## Execução do Projeto

**1 Passo** - Clone o repositório para a sua máquina

```bash
git clone https://github.com/RonildoLima/ShopSystem.git
```
**2 -Passo** - Configure as variáveis para a execução do projeto:

Crie um arquivo `application-dev.properties` na pasta *src/main/resource* do projeto, seguindo o modelo do arquivo **[aplication.properties.exemple](src/main/resources/application.properties.example)** do repositório, da seguinte maneira:

```properties
spring.application.name=shop-system

##Swagger
springdoc.swagger-ui.path=/api-docs.html
springdoc.api-docs.path=/api-docs
springdoc.packages-to-scan=com.accenture.shopsystem

springdoc.swagger-ui.tagsSorter=alpha

###Database Remote

spring.datasource.url=jdbc:mysql://localhost:3306/seu_schema
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

##LOGS
#spring.jpa.properties.hibernate.format_sql=true
#spring.jpa.properties.hibernate.use_sql_comments=true
#logging.level.org.hibernate.type=trace

##RabbitMQ Remote
rabbitmq.queue.pedido=pedido-queue
rabbitmq.exchange=pedido-exchange
rabbitmq.routingkey.pedido=pedido.routingkey
rabbitmq.routingkey.quantidade=quantidade.routingkey
```

**Passo 3** - Execute o projeto SpringBoot em sua máquina

<br>

<br>

## Acesso

<br>

<br>

## Arquitetura

<br>

<br>

## Dificuldades

<br>

<br>

##  Desenvolvedores

<br>


<p align="center">
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
</p>






<h3>Starting</h3>

```bash
cd ShopSystem
run ShopSystemApplication
```

<h2 id="routes">📍 API Endpoints</h2>

<table>
  <tr>
    <th>Route</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><kbd>POST /pedidos/{vendedorId}</kbd></td>
    <td><a href="#insert-order">Insert order</a></td>
  </tr>
  <tr>
    <td><kbd>DELETE /pedidos/{pedidoId}/vendedor/{vendedorId}</kbd></td>
    <td><a href="#delete-order">Delete order</a></td>
  </tr>
  <tr>
    <td><kbd>POST /produtos/adicionar</kbd></td>
    <td><a href="#insert-product">Add product</a></td>
  </tr>
  <tr>
    <td><kbd>GET /{vendedorId}/listar</kbd></td>
    <td><a href="#show-list-product">List products</a></td>
  </tr>
  <tr>
    <td><kbd>DELETE /{vendedorId}/produtos/{produtoId}</kbd></td>
    <td><a href="#delete-product">Delete product</a></td>
  </tr>
  <tr>
    <td><kbd>POST /pedidoHistoricoStatus/processar</kbd></td>
    <td><a href="#">Process or cancel order</a></td>
  </tr>
  <tr>
    <td><kbd>GET /pedidoHistoricoStatus/gerenciar</kbd></td>
    <td><a href="#">Show form to process or cancel orders</a></td>
  </tr>
  <tr>
    <td><kbd>GET /test-rabbit</kbd></td>
    <td><a href="#">Send message</a></td>
  </tr>
  <tr>
    <td><kbd>POST /user/cadastrar</kbd></td>
    <td><a href="#">Register seller</a></td>
  </tr>
  <tr>
    <td><kbd>GET /user/vendedores</kbd></td>
    <td><a href="#">List sellers</a></td>
  </tr>
</table>

<h3 id="insert-order">POST /pedidos/{vendedorId}</h3>

**REQUEST**
```json
{
  "vendedorId": "123456",
  "produtos": [
    {
      "produtoId": "987654",
      "quantidade": 10
    },
    {
      "produtoId": "654321",
      "quantidade": 5
    }
  ]
}
```

**RESPONSE**
```json
{
  "id": "abc123",
  "pedidoDescricao": "Pedido de venda",
  "pedidoValor": 1000,
  "pedidoDataHora": "2025-01-28T20:11:41.260Z",
  "pedidoQuantidade": 10,
  "produtos": [
    {
      "id": "p001",
      "produto": {
        "id": "987654",
        "produtoDescricao": "Produto A",
        "produtoValor": 100,
        "produtoDataHoraSaida": "2025-01-28T20:11:41.260Z",
        "quantidadeEstoque": 990,
        "descricao": "Descrição do Produto A"
      },
      "quantidade": 10,
      "precoUnitario": 100
    }
  ]
}

```

<h3 id="delete-order">DELETE /pedidos/{pedidoId}/vendedor/{vendedorId}</h3>

**REQUEST**
```HTTP
DELETE /pedidos/abc123/vendedor/123456 HTTP/1.1
Host: api.exemplo.com
Content-Type: application/json
Authorization: Bearer seu_token_aqui
```

**RESPONSE**
```json
{
  "mensagem": "Pedido excluído com sucesso",
  "pedidoId": "abc123"
}
```

<h3 id="insert-product">POST /produtos/adicionar</h3>

**REQUEST**
```json
{
  "produtoDescricao": "Notebook Gamer",
  "produtoValor": 5500.99,
  "quantidadeEstoque": 100
}
```

**RESPONSE**
```json
{
  "id": "prod-987654",
  "produtoDescricao": "Notebook Gamer",
  "produtoValor": 5500.99,
  "quantidadeEstoque": 100
}
```

<h3 id="show-list-product">GET /{vendedorId}/listar</h3>

**REQUEST**
```HTTP
GET /6fcab984-6f5c-413e-b6ae-32fdf4707bed/listar HTTP/1.1
Host: api.exemplo.com
Content-Type: application/json
Authorization: Bearer seu_token_aqui
```

**RESPONSE**
```json
[
  {
    "id": "0e44840f-1ac7-4f67-8e46-f47332800ab9",
    "produtoDescricao": "Redmi",
    "produtoValor": 1500,
    "produtoDataHoraSaida": "2025-01-25T18:55:15",
    "quantidadeEstoque": 6,
    "descricao": null
  }
]
```

<h3 id="delete-product">DELETE /{vendedorId}/produtos/{produtoId}r</h3>

**REQUEST**
```HTTP
DELETE /6fcab984-6f5c-413e-b6ae-32fdf4707bed/produtos/0e44840f-1ac7-4f67-8e46-f47332800ab9 HTTP/1.1
Host: api.exemplo.com
Content-Type: application/json
Authorization: Bearer seu_token_aqui
```

**RESPONSE**
```json
{
  "mensagem": "Produto excluído com sucesso",
  "produtoId": "0e44840f-1ac7-4f67-8e46-f47332800ab9"
}
```

<h2 id="colab">🤝 Collaborators</h2>

Special thank you for all people that contributed for this project.

<table>
  <tr>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/80013701?v=4" width="100px;" alt="Ronildo Lima Profile Picture"/><br>
        <sub>
          <b>Ronildo Lima</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/110574063?v=4" width="100px;" alt="Rayane Rodrigues Profile Picture"/><br>
        <sub>
          <b>Rayane Rodrigues</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/100695815?v=4" width="100px;" alt="Tamires Xavier Profile Picture"/><br>
        <sub>
          <b>Tamires Xavier</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
