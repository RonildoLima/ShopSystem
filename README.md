[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[MYSQL_BADGE]:https://img.shields.io/badge/MySQL-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white
[LOMBOK_BADGE]:https://img.shields.io/badge/Lombok-%2318A558.svg?style=for-the-badge&logo=lombok&logoColor=white
[JPA_BADGE]:https://img.shields.io/badge/JPA-%2300A3E0.svg?style=for-the-badge&logo=java&logoColor=white
[JUNIT_BADGE]:https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white
[FLYWAY_BADGE]:https://img.shields.io/badge/Flyway-%2300A6A0.svg?style=for-the-badge&logo=flyway&logoColor=white
[SPRING_SECURITY_BADGE]:https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=Spring-Security&logoColor=white
[THYMELEAF_BADGE]:https://img.shields.io/badge/Thymeleaf-005F0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white


<h1 align="center" style="font-weight: bold;">Shop System 💻</h1>

![spring][SPRING_BADGE]
![java][JAVA_BADGE]
![mysql][MYSQL_BADGE]
![lombok][LOMBOK_BADGE]
![jpa][JPA_BADGE]
![junit][JUNIT_BADGE]
![flyway][FLYWAY_BADGE]
![springsecurity][SPRING_SECURITY_BADGE]
![thymeleaf][THYMELEAF_BADGE]


<p align="center">
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
</p>

<p align="center">
  <b>Order management system with support for RabbitMQ messaging, enabling seamless order placement, processing, updates, and notifications.</b>
</p>

<h2 id="started">🚀 Getting started</h2>

<h3>Prerequisites</h3>

- [Java](https://download.oracle.com/java/17/archive/jdk-17.0.6_windows-x64_bin.msi)
- [Spring Boot](https://start.spring.io/)
- [MySQL](https://dev.mysql.com/downloads/installer/)
- [Git](https://git-scm.com/downloads)

<h3>Cloning</h3>

```bash
git clone https://github.com/RonildoLima/ShopSystem.git
```

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
    <td><a href="#process-order">Process or cancel order</a></td>
  </tr>
  <tr>
    <td><kbd>POST /user/cadastrar</kbd></td>
    <td><a href="#register-seller">Register seller</a></td>
  </tr>
  <tr>
    <td><kbd>GET /user/vendedores</kbd></td>
    <td><a href="#list-seller">List sellers</a></td>
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
<h3 id="process-order">POST /pedidoHistoricoStatus/processar</h3>

**REQUEST**
```json
{
  "pedidold": "123456",
  "acao": "cancelar"
}
```

**RESPONSE**
```json
{
  "pedidoId": "123456",
  "status": "CANCELADO",  
  "acao": "cancelar"
}
```

<h3 id="register-seller">POST /user/cadastrar</h3>

**REQUEST**
```json
{
  "vendedorNome": "João Silva",
  "vendedorSetor": "Vendas",
  "email": "joao.silva@email.com",
  "password": "senha123"
}
```

**RESPONSE**
```json
{
  "mensagem": "Vendedor cadastrado com sucesso.",
  "vendedor": {
    "vendedorNome": "João Silva",
    "vendedorSetor": "Vendas",
    "email": "joao.silva@email.com",
    "password": null
  }
}
```
<h3 id="list-seller">GET /user/vendedores</h3>

**REQUEST**
```HTTP
GET /user/vendedores HTTP/1.1
Host: api.exemplo.com
Content-Type: application/json
Authorization: Bearer seu_token_aqui
```

**RESPONSE**
```json
[
  {
    "id": "3fea09cc-4812-4371-b48e-f6197343d3eb",
    "email": "ronildo@email.com",
    "password": "$2a$10$hB/cUbdwbxahbNXyS.5yenQMLEmRnTFGI0d/LItRBmQ16.RSSS",
    "vendedorNome": "Ronildo",
    "vendedorSetor": "TI",
    "roles": ["USER"]
  },
  {
    "id": "cafbcf89-c6a9-4569-87b7-82ad9a276929",
    "email": "ronilolima229@gmail.com",
    "password": "$2a$10$yAk4xKjmDv1Y67C7dwOpD9p.72d63CJLJhaLZ5315LiW/m180Q1pTXe",
    "vendedorNome": "Ronildo Lima",
    "vendedorSetor": "Serviços",
    "roles": ["USER"]
  }
]
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
