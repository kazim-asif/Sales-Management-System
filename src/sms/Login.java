package sms;

import java.awt.EventQueue;
import javax.swing.*;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JPasswordField;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	static boolean check = false;

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

					if (check == false) {
						readUserCSV(Database.filePath);
					}
					check = true;

					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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

	// Method to verify if a password matches the stored hashed password
	public static boolean verifyPassword(String providedPassword, String storedHashedPassword) {
		String hashedProvidedPassword = getMD5(providedPassword);
		return hashedProvidedPassword.equals(storedHashedPassword);
	}

	public static void readUserCSV(String csvFilePath) {
	    String line;
	    String csvSplitBy = ",";

	    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
	        boolean userExists = false;
	        // Parse each row of the CSV file
	        while ((line = br.readLine()) != null) {
	            // Split the line into username and password
	            String[] data = line.split(csvSplitBy);
	            
	            // Check if the array contains both username and password
	            if (data.length >= 2) {
	                String username = data[0];
	                String storedHashedPassword = data[1];

	                // Create User object
	                User user = new User(username, storedHashedPassword);

	                // Store User object in ArrayList
	                Database.u.add(user);
	                userExists = true;
	            } else {
	                // Handle the case where the line does not contain both username and password
	                System.out.println("Invalid line: " + line);
	            }
	        }
	        
	        // If no user exists, display a popup message
	        if (!userExists) {
	            JOptionPane.showMessageDialog(null, "No user exist, register first", "No User Found", JOptionPane.WARNING_MESSAGE);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	

	public static void readCSV() {
		String line;
		String csvSplitBy = ",";

		// Create a file chooser
		JFileChooser fileChooser = new JFileChooser();

		// Show open dialog
		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			// Get the selected file
			File selectedFile = fileChooser.getSelectedFile();

			try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
				// Parse each row of the CSV file
				while ((line = br.readLine()) != null) {
					// Create SalesRecord object for each row
					String[] data = line.split(csvSplitBy);
					double amount = Double.parseDouble(data[0]);
					int year = Integer.parseInt(data[1]);
					int month = Integer.parseInt(data[2]);
					int day = Integer.parseInt(data[3]);
					String region = data[4];

					// Construct date string in format "MM/dd/yyyy"
					String dateString = String.format("%02d/%02d/%04d", month, day, year);

					SalesRecord record = new SalesRecord(amount, dateString, region);

					// Store SalesRecord object in ArrayList
					Database.sales.add(record);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login Form");
		setResizable(false);
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
		usernameField.setBounds(202, 38, 192, 34);
		contentPane.add(usernameField);
		usernameField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(98, 43, 94, 25);
		contentPane.add(lblNewLabel);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPassword.setBounds(98, 91, 94, 25);
		contentPane.add(lblPassword);

		JLabel fillAllFieldsError = new JLabel("");
		fillAllFieldsError.setForeground(Color.RED);
		fillAllFieldsError.setHorizontalAlignment(SwingConstants.CENTER);
		fillAllFieldsError.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fillAllFieldsError.setBounds(108, 130, 286, 25);
		contentPane.add(fillAllFieldsError);

		JButton Login = new JButton("Login");
		Login.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {

				for (int i = 0; i < Database.u.size(); i++) {

					verifyPassword(passwordField.getText(), Database.u.get(i).getPassword());

					if (Database.u.get(i).getUsername().equals(usernameField.getText())
							&& Database.u.get(i).getPassword().equals(passwordField.getText())) {
						new MainPage().setVisible(true);
						dispose();

						break;
					}

				}

				fillAllFieldsError.setText("There is some Error.");
			}
		});
		Login.setForeground(Color.WHITE);
		Login.setBackground(Color.DARK_GRAY);
		Login.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Login.setBounds(202, 166, 105, 34);
		contentPane.add(Login);

		JLabel lblDontHaveAn = new JLabel("Don't have an account? ");
		lblDontHaveAn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDontHaveAn.setBounds(140, 237, 167, 25);
		contentPane.add(lblDontHaveAn);

		JButton register = new JButton("Register");
		register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				new Register().setVisible(true);
				dispose();

			}
		});
		register.setForeground(Color.BLUE);
		register.setFont(new Font("Tahoma", Font.PLAIN, 16));
		register.setBackground(Color.WHITE);
		register.setBounds(300, 237, 94, 25);
		contentPane.add(register);

		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setBounds(202, 88, 192, 34);
		contentPane.add(passwordField);
	}
}
