# **Projeto CRUD de Usuários em Java**

Este é um projeto simples de CRUD de usuários, utilizando **Java** e uma interface gráfica com **Swing**. Ele se conecta a um banco de dados **MySQL** hospedado no **XAMPP** por meio do **MySQL Connector**.

---

## 📁 **Estrutura do Projeto**

- **`gui`**: Contém a classe `CrudAppGUI` que gerencia a interface gráfica e as operações de CRUD.
- **`services`**: Contém a classe `UsuarioService` que realiza operações no banco de dados.

---

## 🛠️ **Tecnologias Utilizadas**

- **Java**: Linguagem de programação principal.
- **Swing**: Para criação da interface gráfica.
- **MySQL**: Banco de dados para armazenar as informações.
- **XAMPP**: Ambiente para hospedar o servidor MySQL.
- **MySQL Connector**: Biblioteca para conectar a aplicação ao banco de dados.

---

## 🚀 **Como Executar**

1. **Instale o XAMPP** e inicie o servidor MySQL.
2. **Configure o banco de dados**:
   - Crie um banco de dados chamado `crud_db`.
   - Crie a tabela `usuarios` com os seguintes campos:
     ```sql
     CREATE TABLE usuarios (
         id INT AUTO_INCREMENT PRIMARY KEY,
         nome VARCHAR(100),
         email VARCHAR(100),
         senha VARCHAR(100)
     );
     ```
3. **Adicione o MySQL Connector ao projeto**:
   - Certifique-se de que o arquivo `mysql-connector-java-x.x.x.jar` está incluído no classpath.
4. **Compile e execute o projeto**:
   - Execute a classe `Main` para iniciar a aplicação.

---

## ✨ **Funcionalidades**

- **Inserir Usuário**: Adicione um novo usuário ao banco de dados.
- **Editar Usuário**: Atualize as informações de um usuário existente.
- **Deletar Usuário**: Remova um usuário do banco de dados.
- **Pesquisar Usuários**: Liste todos os usuários cadastrados.
