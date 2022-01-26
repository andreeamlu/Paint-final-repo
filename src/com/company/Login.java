package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Login extends JDialog implements ActionListener{

    private JTextField tfName;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnRegister;
    private JButton cancelButton;
    private JPanel registerPanel;
    private JButton btnLogin;

   public Login(JFrame parent) {
       super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450 ,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

       btnRegister.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               registerUser();
           }
       });

       cancelButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose();
           }
       });

       btnLogin.addActionListener(this);
       setVisible(true);
   }

    private void registerUser() {
       String name = tfName.getText();
       String email = tfEmail.getText();
       String password = String.valueOf(pfPassword.getPassword());

       if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
           JOptionPane.showMessageDialog(this, "Please enter all fields", "Try again", JOptionPane.ERROR_MESSAGE);
           return;
       }

       user = addUserToDatabase(name, email, password);

       if(user != null) {
           new Paint();
           dispose();
       }
       else {
           JOptionPane.showMessageDialog(this, "Failed to register new user", "Try again", JOptionPane.ERROR_MESSAGE);
       }

    }

    public User user;

    private User addUserToDatabase(String name, String email, String password) {
       User user = null;
       final String DB_URL = "jdbc:mysql://localhost/ucreate?serverTimezone=UTC";
       final String USERNAME = "root";
       final String PASSWORD = "";

       try{
           Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
           // Conectat la baza de date

           Statement stmt = conn.createStatement();
           String sql = "INSERT INTO users (name, email, password) " + "VALUES (?, ?, ?)";
           PreparedStatement preparedStatement = conn.prepareStatement(sql);
           preparedStatement.setString(1, name);
           preparedStatement.setString(2, email);
           preparedStatement.setString(3, password);

           //Inserarea in tabela
           int addedRows = preparedStatement.executeUpdate();
           if(addedRows > 0) {
               user = new User();
               user.name = name;
               user.email = email;
               user.password = password;
           }

           stmt.close();
           conn.close();

       } catch(Exception e) {
           e.printStackTrace();
       }

       return user;
    }


    public static void main(String[] args) {
        Login myForm = new Login(null);
        User user = myForm.user;
        if(user != null) {
            System.out.println("Successful registration of: " + user.name);
        }
        else {
            System.out.println("Registration canceled");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Register(new JFrame());
        dispose();
    }
}
