# 🛒 Carrinho API - IF Goiano - Campus Urutaí

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23C1E1C1?style=for-the-badge&logo=swagger&logoColor=black)

API didática para o ensino de **Desenvolvimento de APIs RESTful com Java**, focada em evolução incremental, modelagem de agregados e documentação profissional.

---

## 🗺️ Roteiro de Aprendizado (Branches)

O projeto está organizado em ramos para acompanhar a evolução do aprendizado. Para trocar de versão, utilize `git checkout nome-da-branch`.

* 🌱 **`main`**: Versão estável e atualizada (atualmente na v4).
* 📦 **`versao-1-mock`**: Fundamentos de API com dados em memória.
* 🗄️ **`versao-2-jpa`**: Persistência de dados com MySQL e Spring Data JPA.
* 🛡️ **`versao-3-dto-valid`**: Implementação de **DTOs**, **Bean Validation** e uso de **VO (Value Objects)**.
* 🚀 **`versao-4-swagger`**: Documentação interativa, Paginação e novos modelos (**Cliente**, **Carrinho** e **ItemCarrinho**).

---

## 🏗️ Arquitetura e Modelos de Dados

Nesta fase avançada (v4), a API gerencia um fluxo completo de compras:
* **Cliente:** Cadastro e identificação do usuário.
* **Produto:** Catálogo com controle de estoque e inventário.
* **Carrinho:** Agregado que gerencia a experiência de compra, composto por múltiplos **Itens de Carrinho**.

---

## 📖 Documentação da API (Swagger)

A API utiliza **SpringDoc OpenAPI** para gerar documentação automática. Com a aplicação rodando, acesse:
👉 `http://localhost:8080/swagger-ui.html`

### Visualização do Painel:
> **Nota:** Substitua a imagem abaixo pelo seu print do Swagger na pasta do projeto.
![Interface do Swagger UI](docs/swagger-ui.png)

---

## 🛣️ Endpoints Principais (v4)

### 🛍️ Carrinho de Compras
| Método | Caminho | Descrição |
| :--- | :--- | :--- |
| `POST` | `/carrinhos/adicionar` | Adiciona ou atualiza item no carrinho |
| `PATCH` | `/carrinhos/cliente/{id}/atualizar-quantidade` | Altera a quantidade de um item |
| `DELETE` | `/carrinhos/cliente/{id}/remover-produto/{pId}` | Remove um produto do carrinho |
| `GET` | `/carrinhos/cliente/{id}` | Recupera o carrinho completo do cliente |

### 📦 Catálogo de Produtos
| Método | Caminho | Descrição |
| :--- | :--- | :--- |
| `GET` | `/produtos` | Listagem com **Paginação e Filtro** |
| `GET` | `/produtos/{id}/estoque` | Consulta rápida de saldo em estoque |
| `PATCH` | `/produtos/{id}/baixar-estoque` | Realiza baixa manual de estoque |
| `POST` | `/produtos` | Cadastro de novo produto com validação |

---

### 👤 Clientes
| Método | Caminho | Descrição |
| :--- | :--- | :--- |
| `POST` | `/clientes` | Cadastro de novo cliente com validação |

---

## 🎓 Conceitos Avançados Aplicados

* **Data Transfer Objects (DTO):** Desacoplamento entre as entidades de banco e a camada de visualização.
* **Bean Validation:** Garantia de integridade de dados com `@Valid`, `@NotNull` e `@Min`.
* **Value Objects (VO):** Encapsulamento de lógicas de negócio em objetos imutáveis (Ex: Preços).
* **Paginação:** Uso de `Pageable` e `Page<T>` para otimização de consultas.
* **Exceções Customizadas:** Tratamento padronizado de erros com `ResponseEntityExceptionHandler`.

---

## 🚀 Como clonar

### 🔹 Clonar o repositório
```bash
git clone https://github.com/junio-ifgo-urt/api-ensino-carrinho.git
cd api-ensino-carrinho
```

---

### 🔹 Acessar a versão 3 (DTO + Validação)
```bash
git checkout versao-3-dto-valid
```

---

## 🛠️ Configuração do Banco de Dados

Este projeto utiliza **MySQL**.

### 🔹 Criar o banco de dados
```sql
CREATE DATABASE carrinho_db;
```

---

### 🔹 Configurar o `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/carrinho_db
spring.datasource.username=root
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## ▶️ Executar a aplicação

No terminal, execute:

```bash
./mvnw spring-boot:run
```

Ou, se estiver usando Maven instalado:

```bash
mvn spring-boot:run
```

---

## 🌐 Acessar a API

Após iniciar, a aplicação estará disponível em:

```
http://localhost:8080
```

---


## 👨‍🏫 Autor

**Prof. Dr. Junio Lima**  
IF Goiano - Campus Urutaí  

Projeto desenvolvido para fins didáticos na disciplina **Programação Web 2**.
