

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;




public class mainrequirmentsuniversities {

	public static void main(String[] args) {
		
		  Scanner scanner = new Scanner(System.in);
		try {
			
			System.out.println("Enter database name to check if avaialable");
			String url = "jdbc:sqlserver://localhost:1433;" +
			"databaseName="+scanner.nextLine()+";" +
			"encrypt=true;" + "trustServerCertificate=true";
			
			
//	        String url = "jdbc:sqlserver://localhost:1433;" +
//	    				"databaseName=UniversityInOman;" +
//	    				"encrypt=true;" + "trustServerCertificate=true";
	            String username = "sa";
	            String password = "root";
	            Connection conn = DriverManager.getConnection(url, username, password);
	            Statement st = conn.createStatement();
	            
					 //System.out.println("country not available in the file");
					 
	            
	            
	            
            String universityTable = "CREATE TABLE  universities ("
            		+ "                   id INTEGER IDENTITY(1,1) Primary key  ,"
            		+ "                    name TEXT , "
            		+ "                    country TEXT )   " ;
            
             //st.executeUpdate(universityTable);
            
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\HP\\eclipse-workspace\\JDBCRequirementsUniversitiesProject\\university.txt"));
            StringBuilder jsonString = new StringBuilder();
            
   
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
            JSONArray universities = new JSONArray(jsonString.toString());
            for (int i = 0; i < universities.length(); i++) {
                JSONObject university = universities.getJSONObject(i);
                String name = university.getString("name");
                String country = university.getString("country");
                //System.out.println(name + " - " + country);
                
                
                
                String unviQERY = "insert into universities values ('"+name+"','"+country+"')";
			      
			      st.execute(unviQERY);
            }
          
            System.out.println("Enter country you want to search");
            
           
            
            String searchCountry = "select * from universities where country like '" + scanner.nextLine()+"'";
        
            ResultSet resultSet = st.executeQuery(searchCountry);
            if (resultSet.next())  {
				 
            while (resultSet.next()) {
            	System.out.println("country:" + resultSet.getString("country"));
            	
            	System.out.println("id:" + resultSet.getInt("id"));
            	System.out.println("name:" + resultSet.getString("name")+"\n\n");}
            }else {
				 System.out.println("country not available in data base ");
            
            }
            
            
            
            
            
            
            
            
            
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
