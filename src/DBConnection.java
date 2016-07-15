/**
 * Jul 15, 2016
 * DBConnection.java
 * MyVoc
 */
package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author helena
 *
 */
public class DBConnection {
	Connection connection;
	public DBConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("driver loaded");
		
		String databaseUrl = "jdbc:mysql://localhost/your database name";
		String username = "your username";
		String password = "your password";
		//connect to a database, autocommit mode
		connection = DriverManager.getConnection(databaseUrl, username, password);
		System.out.println("connected to database " + databaseUrl);
	}
	public void CloseConnection() throws SQLException{
		connection.close();	
	}
	public ArrayList<String> getExplaination(String word) throws SQLException{
		ArrayList<String> results = new ArrayList<String>();
		//create a statement
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select Entry.word, def from Entry, Explaination"
		+ " where Entry.word = '" + word + "' and Entry.word = Explaination.word");
		while(resultSet.next()){//set the current row to the first row
//			System.out.println(resultSet.getString(1) + "\t"
//					+ resultSet.getString(2));
			results.add(resultSet.getString(2));
		}	
		return results;
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		DBConnection dbc = new DBConnection();
		ArrayList<String> res = dbc.getExplaination("commencement");
		for(String tmp: res){
			System.out.println(tmp);
		}
	}
}
