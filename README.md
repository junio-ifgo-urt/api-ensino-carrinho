# 🛒 Carrinho API - IFMaker (IFG)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

API didática para o ensino de **Desenvolvimento Web com Java**, focada em evolução incremental de funcionalidades e boas práticas.



## 🗺️ Roteiro de Aprendizado (Branches)

O projeto está organizado em ramos (branches) para que o aluno acompanhe a evolução do código:

* 🌱 **`main`**: Versão estável e atualizada.
* 📦 **`versao-1-mock`**: Fundamentos de API com dados em memória (Mock).
* 🗄️ **`versao-2-jpa`**: Persistência de dados com MySQL e Spring Data JPA.

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem:** Java 17+
* **Framework:** Spring Boot 3.x
* **Banco de Dados:** MySQL 8.x
* **ORM:** Hibernate / Spring Data JPA
* **Logs:** Java Util Logging (JUL)

---
## 🛣️ Endpoints da API

| Método | Caminho | Descrição |
| :--- | :--- | :--- |
| `GET` | `/produtos` | Listagem completa |
| `GET` | `/produtos/{id}` | Detalhes do produto |
| `POST` | `/produtos` | Cadastro de novo item |
| `PUT` | `/produtos/{id}` | Atualização total |
| `DELETE` | `/produtos/{id}` | Exclusão do sistema |
| `PATCH` | `/produtos/{id}/baixar-estoque` | Lógica de baixa de estoque |

## 🎓 Conceitos Aplicados na v2

* **Mapeamento de Entidades:** Transformando classes Java em tabelas MySQL através do Hibernate.
* **Injeção de Dependências:** Desacoplando as camadas de **Controller**, **Service** e **Repository**.
* **Tratamento de Exceções:** Uso de exceções personalizadas para regras de negócio (como estoque insuficiente).
* **Integridade Transacional:** Uso da anotação `@Transactional` para garantir que operações no banco sejam atômicas.

## 🚀 Como Executar a Versão 2 (JPA)

### 1. Clonar o repositório e acessar a Branch
```bash
git clone [https://github.com/junio-ifgo-urt/api-ensino-carrinho.git](https://github.com/junio-ifgo-urt/api-ensino-carrinho.git)
cd api-ensino-carrinho
git checkout versao-2-jpa

## 👨‍🏫 Autor

**Prof. Junio Lima**
*IF Goiano - Campus Urutaí - IFMaker
