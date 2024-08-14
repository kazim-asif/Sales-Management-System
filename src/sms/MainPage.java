package sms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	private UtilDateModel model;
	private JDatePickerImpl datePicker;
	private JTextField selectedAmountField;
	private JTable table;
	JLabel error;
	int index = -1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPage frame = new MainPage();
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
	public MainPage() {
		setTitle("Sales Importer");
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 805, 505);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Import File:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(156, 11, 81, 23);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setBounds(237, 11, 307, 23);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton ImportFileBtn = new JButton("Import File");
		ImportFileBtn.setForeground(Color.WHITE);
		ImportFileBtn.setBackground(Color.DARK_GRAY);
		ImportFileBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ImportFileBtn.setBounds(561, 11, 106, 23);
		contentPane.add(ImportFileBtn);

		// ActionListener for the importDataButton
		ImportFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open file chooser dialog to select CSV file
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Choose CSV File");
				fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));
				int userSelection = fileChooser.showOpenDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						// Check if the file has already been imported
						if (!isFileAlreadyImported(selectedFile)) {
							// Add file to the imported_files.txt to prevent duplicate imports
							appendToFile(Database.filesRecord, selectedFile.getName());

							// Process the sales data from the CSV file
							ArrayList<SalesRecord> sales = parseCSV(selectedFile);

							for (int i = 0; i < sales.size(); i++) {
								Database.sales.add(sales.get(i));
							}

							// Populate table with imported sales data
							populateTable(Database.sales);
						} else {
							JOptionPane.showMessageDialog(null, "File already imported.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		JLabel lblAdd = new JLabel("Add Line Items:");
		lblAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAdd.setBounds(89, 45, 119, 23);
		contentPane.add(lblAdd);

		JLabel lblDate = new JLabel("Date:");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(127, 79, 50, 23);
		contentPane.add(lblDate);

		// Create UtilDateModel for JDatePicker
		model = new UtilDateModel();

		// Create properties for JDatePicker
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");

		// Create JDatePicker
		datePicker = new JDatePickerImpl(new JDatePanelImpl(model, p), new DateLabelFormatter());

		// Create JTextField to display selected date
		JTextField selectedDateField = new JTextField();
		selectedDateField.setBackground(Color.WHITE);
		selectedDateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		selectedDateField.setEditable(false);
		selectedDateField.setBounds(236, 79, 200, 23);
		contentPane.add(selectedDateField);

		// Create JButton to open the date picker drop down
		JButton datePickerButton = new JButton("Select Date");
		datePickerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Create a custom container panel to hold the JDatePicker
				JPanel datePickerContainer = new JPanel(new BorderLayout());
				datePickerContainer.add(datePicker);

				// Create a JPopupMenu and add the custom container panel
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(datePickerContainer);

				// Show the pop up menu below the button
				popupMenu.show(datePickerButton, 0, datePickerButton.getHeight());
			}
		});
		datePickerButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		datePickerButton.setBounds(450, 79, 106, 23);
		contentPane.add(datePickerButton);

		JLabel lblRegion = new JLabel("Region:");
		lblRegion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRegion.setBounds(127, 116, 50, 23);
		contentPane.add(lblRegion);

		// Listener to update selected date text field when date is picked
		datePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (datePicker.getModel().isSelected()) {
					java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					selectedDateField.setText(dateFormat.format(selectedDate));
				}
			}
		});

		// ComboBox for Region selection
		String[] regions = { "West", "Central", "Mountain" };
		JComboBox<String> selectedRegionField = new JComboBox<>(regions);
		selectedRegionField.setBackground(Color.WHITE);
		selectedRegionField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		selectedRegionField.setBounds(236, 116, 200, 23);
		contentPane.add(selectedRegionField);

		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAmount.setBounds(127, 153, 62, 23);
		contentPane.add(lblAmount);

		selectedAmountField = new JTextField();
		selectedAmountField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		selectedAmountField.setBackground(Color.WHITE);
		selectedAmountField.setBounds(237, 153, 200, 23);
		contentPane.add(selectedAmountField);

		JButton btnAddLine = new JButton("Add Line");
		btnAddLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String selectedRegion = (String) selectedRegionField.getSelectedItem();

				if (!(selectedAmountField.getText().isBlank() && selectedRegion.isBlank()
						&& selectedDateField.getText().isBlank())) {

					String inputDate = selectedDateField.getText();

					// Parse the input date string to a LocalDate object
					LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

					// Format the LocalDate object to the desired format (YYYY-MM-DD)
					String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

					error.setText("");
					// Assuming selectedRegionField is your JComboBox
					SalesRecord s = new SalesRecord(Double.parseDouble(selectedAmountField.getText()), formattedDate,
							selectedRegion);
					Database.sales.add(s);

					populateTable(Database.sales);
				} else {
					error.setText("Fill all fields");
				}

			}
		});
		btnAddLine.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAddLine.setBounds(127, 191, 106, 23);
		contentPane.add(btnAddLine);

		JLabel lblLineItems = new JLabel("Line Items:");
		lblLineItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLineItems.setBounds(89, 221, 119, 23);
		contentPane.add(lblLineItems);

		// ActionListener for the regionComboBox
		selectedRegionField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedRegion = (String) selectedRegionField.getSelectedItem();
				System.out.println(selectedRegion);
			}
		});

		// Table for displaying line items
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(89, 250, 650, 150);
		contentPane.add(scrollPane);

		// Define column names
		String[] columnNames = { "S.no", "Date", "Quarter", "Region", "Amount" };

		// Create table model with no data initially
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);

		// Create JTable with the defined model
		table = new JTable(model);

		// Add table to scroll pane
		scrollPane.setViewportView(table);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String selectedRegion = (String) selectedRegionField.getSelectedItem();
				String inputDate = selectedDateField.getText();

				// Parse the input date string to a LocalDate object
				LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

				// Format the LocalDate object to the desired format (YYYY-MM-DD)
				String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

				Database.sales.set(index, new SalesRecord(Double.parseDouble(selectedAmountField.getText()),
						formattedDate, selectedRegion));

				populateTable(Database.sales);
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpdate.setBounds(171, 424, 106, 31);
		contentPane.add(btnUpdate);

		JButton btnAdd = new JButton("Add to Edit");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// Get the index of the selected row
				index = table.getSelectedRow();

				// Check if a row is selected
				if (index != -1) {
					// Retrieve data from the selected row
					Object[] rowData = new Object[table.getColumnCount()];
					for (int i = 0; i < table.getColumnCount(); i++) {
						rowData[i] = table.getValueAt(index, i);
					}

//					for (int i = 0; i < rowData.length; i++) {
//						System.out.println(rowData[i]);
//					}

					String date = (String) rowData[1];

					// Parse the date string to extract month and day
					String[] dateParts = date.split("-");
					int month = Integer.parseInt(dateParts[1]);

					// Determine the quarter based on the month
					String quarter;
					if (month >= 1 && month <= 3) {
						quarter = "1";
					} else if (month >= 4 && month <= 6) {
						quarter = "2";
					} else if (month >= 7 && month <= 9) {
						quarter = "3";
					} else {
						quarter = "4";
					}

					rowData[0] = rowData[0];
					rowData[1] = date;
					rowData[2] = quarter;
					selectedRegionField.setSelectedItem(rowData[3]);
					selectedAmountField.setText(rowData[4].toString());

//					Database.sales.set(index,
//							new SalesRecord(Double.parseDouble(selectedAmountField.getText()), date,
//									selectedRegionField.getSelectedItem().toString()));

					// Update the table model with the modified data
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.removeRow(index); // Remove the old row
					model.insertRow(index, rowData); // Insert the modified row
				}
			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAdd.setBounds(55, 424, 106, 31);
		contentPane.add(btnAdd);

		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				writeDataToCSV(Database.sales, Database.all_sales_records);
				
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSave.setBounds(287, 424, 106, 31);
		contentPane.add(btnSave);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// Get the index of the selected row
				index = table.getSelectedRow();
				
				
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.removeRow(index); // Remove the old row
				
				Database.sales.remove(index);

//				model.setRowCount(0); // Remove all rows from the table
				
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDelete.setBounds(403, 424, 106, 31);
		contentPane.add(btnDelete);

		JButton btnExport = new JButton("Export");
		btnExport.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        JFileChooser fileChooser = new JFileChooser();
		        int userSelection = fileChooser.showSaveDialog(null);
		        if (userSelection == JFileChooser.APPROVE_OPTION) {
		            File fileToSave = fileChooser.getSelectedFile();
		            try (FileWriter writer = new FileWriter(fileToSave + ".csv")) {
		                DefaultTableModel model = (DefaultTableModel) table.getModel();
		                int rowCount = model.getRowCount();
		                int columnCount = model.getColumnCount();
		                
		                // Write column names
		                for (int i = 0; i < columnCount; i++) {
		                    writer.write(model.getColumnName(i));
		                    if (i < columnCount - 1) {
		                        writer.write(",");
		                    }
		                }
		                writer.write("\n");

		                // Write data
		                for (int row = 0; row < rowCount; row++) {
		                    for (int col = 0; col < columnCount; col++) {
		                        writer.write(model.getValueAt(row, col).toString());
		                        if (col < columnCount - 1) {
		                            writer.write(",");
		                        }
		                    }
		                    writer.write("\n");
		                }
		                writer.close();
		                JOptionPane.showMessageDialog(null, "Data exported successfully!");
		            } catch (IOException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(null, "Error exporting data!");
		            }
		        }
		    }
		});

		btnExport.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExport.setBounds(519, 424, 106, 31);
		contentPane.add(btnExport);

		JButton btnExit = new JButton("Exit");

		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Define the path to the CSV file
				try {
					// Open the CSV file in write mode to truncate it
					PrintWriter writer = new PrintWriter(new FileWriter(Database.filesRecord));
					writer.print(""); // Truncate the file by writing an empty string
					writer.close();
					System.out.println("CSV file cleared successfully.");

					new Login().setVisible(true);
					dispose();

				} catch (IOException ex) {
					ex.printStackTrace();
					System.out.println("Error occurred while clearing CSV file.");
				}

			}
		});
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExit.setBounds(635, 424, 106, 31);
		contentPane.add(btnExit);

		error = new JLabel("");
		error.setForeground(Color.RED);
		error.setFont(new Font("Tahoma", Font.PLAIN, 14));
		error.setBounds(247, 187, 235, 27);
		contentPane.add(error);

		// Populate table with data
		ArrayList<SalesRecord> sales = Database.sales;
		for (SalesRecord record : sales) {
			Object[] rowData = { record.getDate(), record.getRegion(), record.getAmount() };
			model.addRow(rowData);
		}

	}

	// Check if the file has already been imported
	private boolean isFileAlreadyImported(File file) throws IOException {
		String filename = file.getName();
		BufferedReader reader = new BufferedReader(new FileReader(Database.filesRecord));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.equals(filename)) {
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}

	// Append content to a file
	private void appendToFile(String filename, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
		writer.append(content);
		writer.newLine();
		writer.close();
	}

	// Parse CSV file to extract sales data
	private ArrayList<SalesRecord> parseCSV(File file) throws IOException {
		ArrayList<SalesRecord> sales = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			// Split CSV line by comma
			String[] parts = line.split(",");
			// Assuming the format is: amount,year,month,day,region
			double amount = Double.parseDouble(parts[0]);
			String date = parts[1] + "-" + parts[2] + "-" + parts[3];

			// Assuming year, month, day format
			String region = parts[4];
			sales.add(new SalesRecord(amount, date, region));
		}
		reader.close();
		return sales;
	}

	// Populate table with sales data
	private void populateTable(ArrayList<SalesRecord> sales) {

		// Define column names

		// Create table model with no data initially
//    	DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Clear existing data

		// Populate table with data
		int counter = 1;
		for (SalesRecord record : sales) {
			// Extract the date string from the record
			String dateString = record.getDate();

//			System.out.println(record.getDate());

			// Parse the date string to extract month and day
			String[] dateParts = dateString.split("-");
			int month = Integer.parseInt(dateParts[1]);

			// Determine the quarter based on the month
			String quarter;
			if (month >= 1 && month <= 3) {
				quarter = "1";
			} else if (month >= 4 && month <= 6) {
				quarter = "2";
			} else if (month >= 7 && month <= 9) {
				quarter = "3";
			} else {
				quarter = "4";
			}

			// Create an array with the row data
			Object[] rowData = { counter++, dateString, quarter, record.getRegion(), record.getAmount() };

			// Add the row data to the model
			model.addRow(rowData);
		}

//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//        model.setRowCount(0); // Clear existing data
//        for (SalesRecord record : sales) {
//            String formattedDate = formatDate(record.getDate()); // Format date to "MM/dd/yyyy" string
//            Object[] rowData = {record.getAmount(), formattedDate, record.getRegion()};
//            model.addRow(rowData);
//        }
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
	
	public static void writeDataToCSV(ArrayList<SalesRecord> salesRecords, String filePath) {
        // Create a HashSet to store unique records
        Set<String> uniqueRecords = new HashSet<>();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Iterate through the sales records
            for (SalesRecord record : salesRecords) {
                // Format the date string to "YYYY-MM-DD" format
                String[] dateParts = record.getDate().split("-");
                String dateString = dateParts[2] + "-" + dateParts[0] + "-" + dateParts[1];
                
                System.out.println(dateString);
                
             // Construct the record string with commas as delimiters
                String recordString = String.format("%.2f,%s,%s,%s,%s", record.getAmount(), 
                        dateParts[0], dateParts[1], dateParts[2], record.getRegion());

                // Check if the record is unique
                if (!uniqueRecords.contains(recordString)) {
                    // Add the record to the HashSet
                    uniqueRecords.add(recordString);
                    // Write the record to the CSV file
                    writer.println(recordString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
}
