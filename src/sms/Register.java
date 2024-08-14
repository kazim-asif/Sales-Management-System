package sms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JPasswordField;

public class Register extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Register() {
		setTitle("Register Account");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 336);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		usernameField = new JTextField();
		usernameField.setHorizontalAlignment(SwingConstants.CENTER);
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameField.setColumns(10);
		usernameField.setBounds(208, 35, 192, 34);
		contentPane.add(usernameField);

		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(104, 40, 94, 25);
		contentPane.add(lblNewLabel);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPassword.setBounds(104, 88, 94, 25);
		contentPane.add(lblPassword);

		JLabel fillAllFieldsError = new JLabel();
		fillAllFieldsError.setHorizontalAlignment(SwingConstants.CENTER);
		fillAllFieldsError.setForeground(Color.RED);
		fillAllFieldsError.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fillAllFieldsError.setBounds(114, 127, 286, 25);
		contentPane.add(fillAllFieldsError);

		JButton Register = new JButton("Register");
		Register.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {

				if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
					fillAllFieldsError.setText("Kindly fill out both fields.");
				} else {

					User u1 = new User(usernameField.getText(), passwordField.getText());
					Database.u.add(u1);
					
					String encryptedPassword = getMD5(passwordField.getText());

					// Write the user information from the Database.u list to the CSV file
					try (PrintWriter writer = new PrintWriter(new FileWriter(Database.filePath, true))) {
					    // Append the new user information to the CSV file
					    writer.println(u1.getUsername() + "," + encryptedPassword);
					} catch (IOException ex) {
					    ex.printStackTrace();
					}
					
					new Login().setVisible(true);
					dispose();
				}
			}
		});
		Register.setForeground(Color.WHITE);
		Register.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Register.setBackground(Color.DARK_GRAY);
		Register.setBounds(208, 163, 105, 34);
		contentPane.add(Register);

		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				new Login().setVisible(true);
				dispose();

			}
		});
		btnBack.setForeground(Color.DARK_GRAY);
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnBack.setBackground(Color.WHITE);
		btnBack.setBounds(377, 261, 94, 25);
		contentPane.add(btnBack);

		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setBounds(208, 79, 192, 34);
		contentPane.add(passwordField);
	}

	// Method to convert a string to MD5 hash
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
