package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import services.UsuarioService;

public class CrudAppGUI extends JFrame {
    // Configurações de banco de dados
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crud_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Componentes da interface gráfica
    private JTextField txtLoginEmail;
    private JPasswordField txtLoginSenha;
    private JTable table;
    private DefaultTableModel tableModel;
    private UsuarioService usuarioService;

    public CrudAppGUI() {
        usuarioService = new UsuarioService();

        setTitle("CRUD com JFrame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Painel de login
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.add(new JLabel("Email:"));
        txtLoginEmail = new JTextField();
        loginPanel.add(txtLoginEmail);
        loginPanel.add(new JLabel("Senha:"));
        txtLoginSenha = new JPasswordField();
        loginPanel.add(txtLoginSenha);

        JButton btnLogin = new JButton("Login");
        loginPanel.add(new JLabel());
        loginPanel.add(btnLogin);

        panel.add(loginPanel, BorderLayout.NORTH);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnInserir = new JButton("Inserir");
        JButton btnEditar = new JButton("Editar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnPesquisar = new JButton("Pesquisar");

        buttonPanel.add(btnInserir);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnDeletar);
        buttonPanel.add(btnPesquisar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Tabela para mostrar os dados
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Email"}, 0);
        table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Adicionar MouseListener à tabela
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    showEditDialog(row);
                }
            }
        });

        // Ações dos botões
        btnLogin.addActionListener(e -> login());
        btnInserir.addActionListener(e -> showInsertDialog());
        btnEditar.addActionListener(e -> showEditDialog());
        btnDeletar.addActionListener(e -> deleteSelectedUser());
        btnPesquisar.addActionListener(e -> pesquisarUsuarios());

        add(panel);
    }

    private void login() {
        String email = txtLoginEmail.getText();
        String senha = new String(txtLoginSenha.getPassword());
        boolean autenticado = true;

        if (autenticado) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos.");
        }
    }

    private void showInsertDialog() {
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();

        Object[] message = {
                "Nome:", nomeField,
                "Email:", emailField,
                "Senha:", senhaField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Inserir Usuário", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText();
            String email = emailField.getText();
            String senha = new String(senhaField.getPassword());
            if (isEmailValid(email)) {
                inserirUsuario(nome, email, senha);
            } else {
                JOptionPane.showMessageDialog(this, "Email inválido! Por favor, insira um email válido.");
            }
        }
    }

    private void showEditDialog() {
        // Este método será chamado pelo botão "Editar"
        showEditDialog(-1);
    }

    private void showEditDialog(int row) {
        JTextField idField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();

        if (row != -1) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            nomeField.setText(tableModel.getValueAt(row, 1).toString());
            emailField.setText(tableModel.getValueAt(row, 2).toString());
            idField.setEditable(false);
        }

        Object[] message = {
                "ID:", idField,
                "Nome:", nomeField,
                "Email:", emailField,
                "Senha:", senhaField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Usuário", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String nome = nomeField.getText();
            String email = emailField.getText();
            String senha = new String(senhaField.getPassword());
            if (isEmailValid(email)) {
                editarUsuario(id, nome, email, senha);
            } else {
                JOptionPane.showMessageDialog(this, "Email inválido! Por favor, insira um email válido.");
            }
        }
    }

    private void showDeleteDialog() {
        JTextField idField = new JTextField();

        Object[] message = {
                "ID:", idField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Deletar Usuário", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            deletarUsuario(id);
        }
    }

    private void deleteSelectedUser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            deletarUsuario(id);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um usuário para deletar.");
        }
    }

    private void inserirUsuario(String nome, String email, String senha) {
        usuarioService.inserirUsuario(nome, email, senha);
        pesquisarUsuarios();
    }

    private void editarUsuario(int id, String nome, String email, String senha) {
        usuarioService.editarUsuario(id, nome, email, senha);
        pesquisarUsuarios();
    }

    private void deletarUsuario(int id) {
        usuarioService.deletarUsuario(id);
        pesquisarUsuarios();
    }

    private void pesquisarUsuarios() {
        tableModel.setRowCount(0); // Limpar tabela antes de atualizar

        try (ResultSet rs = usuarioService.pesquisarUsuarios()) {
            while (rs != null && rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("nome"), rs.getString("email")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar usuários: " + e.getMessage());
        }
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(Integer.toHexString(0xff & b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

}