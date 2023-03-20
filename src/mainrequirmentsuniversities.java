

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;




public class mainrequirmentsuniversities {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		String[] MenuArray = { "Chose one of the follwing", "A) Search data by country name","B) Show suggestion ",
		"C) Backup DB ","D) Remove table","E) Show all UNVIs","F) fetch data ",
		"G) Search data by colum name ","H) Write data to file ","X) exit the System" };
		try {
		boolean programFlag = true;
		System.out.println("Enter Database Name");
		String url = "jdbc:sqlserver://localhost:1433;" + "databaseName=" + scan.nextLine() + ";"
		+ "encrypt=true;" + "trustServerCertificate=true";
		String username = "sa";
		String password = "root";
		Connection conn = DriverManager.getConnection(url, username, password);
		Statement st = conn.createStatement();
		
		
		while (programFlag) {
		setListOfItems(MenuArray);
		String MainMenuResponce = scan.nextLine().toLowerCase();
		getDatabaseMetaData(conn);
		if (!tableExists(conn, "universities")) {
		String universities = "CREATE TABLE universities (" + " id INTEGER IDENTITY(1,1) Primary key ,"
		+ " name varchar(1000) , " + " country varchar(1000)) ";
		st.executeUpdate(universities);
		} else {
		System.out.println("Table universities Exists");
		}
		
		
		
		
		BufferedReader reader = new BufferedReader(
		new FileReader("C:\\Users\\HP\\eclipse-workspace\\JDBCRequirementsUniversitiesProject\\university.txt"));
		StringBuilder jsonString = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
		jsonString.append(line);
		}
		reader.close();
		JSONArray universities = new JSONArray(jsonString.toString());
		for (int i = 0; i < universities.length(); i++) {
		JSONObject university = universities.getJSONObject(i);
		// int id = university.getInt("id");
//		String name = university.getString("name");
//		String country = university.getString("country");
//		String unviQERY = "insert into universities VALUES ('" + name + "','" + country + "')";
//		st.execute(unviQERY);
		

		}
		
		switch (MainMenuResponce.toLowerCase()) {
		case "a"://Search by country name"
		System.out.println("enter Country name ");
		String searchCon = "select * from universities WHERE country like '" + scan.nextLine() + "'";
		showUnviSet(conn, searchCon);
		break;
		case "b":// Show suggestion
		System.out.println(" list of countries suggestions");
		String sug = "select * from universities WHERE Country IN ( 'India', 'Oman','Canada') ";
		showUnviSet(conn, sug);
		break;
		
		case "c"://backup DB
			
			String backupPath = "C:\\Users\\HP\\eclipse-workspace\\JDBCRequirementsUniversitiesProject\\backups.bak"; // specify the path for the backup file
			 String backupSql = "BACKUP DATABASE UniversityInOman TO DISK = '" + backupPath + "'";
			
			st.execute(backupSql);
			 System.out.println("Database backup created at " + backupPath);
			
		break;
		
		case "d"://remove table
			System.out.println("which table you want to delete?");
			ResultSet rs = st.executeQuery(
					"SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE';");
			ArrayList<String> tablesList = new ArrayList<>();
			int i = 0;
			while (rs.next()) {
				i++;
				System.out.println(i + ") " + rs.getString("TABLE_NAME"));
				tablesList.add(rs.getString("TABLE_NAME"));
			}
			System.out.println();
			System.out.println("chosse the number of table you want to delete");
			int choise = scan.nextInt();
			System.out.println("table is deleted you can checK your SQL ");
			scan.nextLine();
			System.out.println("-------------------------------------------------");
			st.execute("drop table " + tablesList.get(choise - 1));
			
             
		break;
		case "e":// show all UNVIs
			System.out.println(" those are all universities available");
			String availableUNI = "select * from universities";
		
			//st.executeUpdate(availableUNI);
			showUnviSet(conn, availableUNI);
			
		break;
		case "f"://fetch data
		break;
		case "g"://search data by colum name
			
			System.out.println("Enter the column name to search for:");
		    String columnName = scan.nextLine();
		    System.out.println("Enter the search value:");
		    String searchValue = scan.nextLine();

		    String sql = "SELECT * FROM universities WHERE " + columnName + " = ?";
		    PreparedStatement ps = conn.prepareStatement(sql);
		    ps.setString(1, searchValue);
		    ResultSet r = ps.executeQuery();

		    // Print the results
		    while (r.next()) {
		        System.out.println(r.getString("name") + " - " + r.getString("country"));
		    }
		   
		break;
		
		case "h"://write data to file
			
		    try (PrintWriter writer = new PrintWriter("universities.txt")) {
		        String w = "SELECT * FROM universities";
		        ResultSet R = st.executeQuery(w);
		        while (R.next()) {
		            writer.println(R.getInt("id") + ", " + R.getString("name") + ", " + R.getString("country"));
		        }
		        System.out.println("Data has been written to universities.txt");
		    } catch (Exception e) {
		        System.err.println("Error writing data to file: " + e.getMessage());
		    }
		    break;
			
			
		
		case "x"://exit
			System.out.println("Are you sure you want to exit? yes/no");
			String userChoice = scan.nextLine().toLowerCase();
			if (userChoice.equals("no")) {
			    System.out.println("\t\tstarting of the program");
			} else if (userChoice.equals("yes")) {
			    System.out.println(" Thank You See You Soon");
			    programFlag = false;
			}
			break;
		}
		}
		} catch (Exception e) {
		System.err.println("Error: " + e.getMessage());
		}
		}
		static int getSize(Connection con, String tableName) throws SQLException {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from " + tableName);
		int i = 0;
		while (rs.next()) {
		i++;
		}
		return i;
		};
		static boolean tableExists(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
		return resultSet.next();
		}
		static void showUnviSet(Connection con, String str) throws SQLException {
		Statement st = con.createStatement();
		ResultSet resultSet = st.executeQuery(str);
		if (resultSet.next()) {
		while (resultSet.next()) {
		System.out.println("\nid: " + resultSet.getInt("id") + " |country: " + resultSet.getString("country")
		+ " |name: " + resultSet.getString("name")
		+ "\n\n");
		}
		}
		else {
		System.out.println("No data found ");
		}
		}
		
		
		
		static void setListOfItems(String arr[]) {
		for (String a : arr) {
		System.out.println(a + "\n");
		}
		}
		
		
		public static void getDatabaseMetaData(Connection con) throws SQLException
		{
		DatabaseMetaData meta = con.getMetaData();

		} }