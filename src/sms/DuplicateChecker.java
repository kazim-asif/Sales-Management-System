package sms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DuplicateChecker {

	// Method to check if a file has already been imported
	public static boolean isFileImported(String fileName) {
		try (BufferedReader reader = new BufferedReader(new FileReader(Database.filesRecord))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.equals(fileName)) {
					return true; // File already imported
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false; // File not found in imported files
	}

	// Method to add a file to the list of imported files
	public static void addImportedFile(String fileName) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Database.filesRecord, true))) {
			writer.write(fileName);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Example usage
		String fileNameToImport = "example.csv";
		if (isFileImported(fileNameToImport)) {
			System.out.println("File already imported.");
		} else {
			System.out.println("Importing file...");
			// Import the file...
			// After successful import, add the file to the list of imported files
			addImportedFile(fileNameToImport);
		}
	}
}
