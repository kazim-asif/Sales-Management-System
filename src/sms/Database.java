package sms;

import java.util.ArrayList;

public class Database {

	public static ArrayList<User> u = new ArrayList<User>();
	public static ArrayList<SalesRecord> sales = new ArrayList<SalesRecord>();
	
	public static int serialNO = sales.size();
	
	static String filePath = System.getProperty("user.dir") + "\\Resources\\users.csv";
	static String filesRecord = System.getProperty("user.dir") + "\\Resources\\imported_files.txt";
	static String all_sales_records = System.getProperty("user.dir") + "\\Resources\\all_sales_records.csv";
	
}
