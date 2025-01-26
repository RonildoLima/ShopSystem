[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[MYSQL_BADGE]:https://img.shields.io/badge/MySQL-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white
[AWS_BADGE]:https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white
[LOMBOK_BADGE]:https://img.shields.io/badge/Lombok-%2318A558.svg?style=for-the-badge&logo=lombok&logoColor=white
[JPA_BADGE]:https://img.shields.io/badge/JPA-%2300A3E0.svg?style=for-the-badge&logo=java&logoColor=white
[JUNIT_BADGE]:https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white
[FLYWAY_BADGE]:https://img.shields.io/badge/Flyway-%2300A6A0.svg?style=for-the-badge&logo=flyway&logoColor=white
[SPRING_SECURITY_BADGE]:https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=Spring-Security&logoColor=white
[THYMELEAF_BADGE]:https://img.shields.io/badge/Thymeleaf-005F0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white


<h1 align="center" style="font-weight: bold;">Shop System 💻</h1>

![AWS][AWS_BADGE]
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
- [Git 2](https://github.com)

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

| route                                 | description                                          
|---------------------------------------|---------------------------------------------------------------------------------------------------------
| <kbd>POST /user/cadastrar</kbd>       | register a vendor, see [request details](#post-user-detail)

<h3 id="post-user-detail">POST /user/cadastrar</h3>

**REQUEST**
```json
{
  "vendedorNome": "Ronildo",
  "vendedorSetor": "T.I",
  "email": "ronildo@example.com",
  "password": "password1234"
}
```

**RESPONSE**
```json
{
  "vendedorNome": "Ronildo",
  "vendedorSetor": "T.I",
  "email": "ronildo@example.com",
  "password": "null"
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
