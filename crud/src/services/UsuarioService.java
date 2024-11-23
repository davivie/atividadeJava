package services;

import java.sql.*;
import javax.swing.JOptionPane;

public class UsuarioService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crud_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private String hashSenha(String senha) {
        // Método fictício para hash da senha
        return senha; // Substitua por sua implementação real
    }

    public void inserirUsuario(String nome, String email, String senha) {
        String checkEmailSql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Este email já está em uso. Tente outro.");
                return;
            }

            String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.setString(3, hashSenha(senha));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuário inserido com sucesso!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir usuário: " + e.getMessage());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar email: " + e.getMessage());
        }
    }

    public void editarUsuario(int id, String nome, String email, String senha) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, hashSenha(senha));
            stmt.setInt(4, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário editado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar usuário: " + e.getMessage());
        }
    }

    public void deletarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar usuário: " + e.getMessage());
        }
    }

    public ResultSet pesquisarUsuarios() {
        String sql = "SELECT * FROM usuarios";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Carrega o driver JDBC

            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver JDBC não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar usuários: " + e.getMessage());
        }
        return null;
    }
}
