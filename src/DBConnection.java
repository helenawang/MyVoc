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
		
		String databaseUrl = "jdbc:mysql://localhost/MyVocDB";
		String username = "scott";
		String password = "tiger";
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
	public String saveWord(Entry entry) throws SQLException{
		String word = entry.word;
		ArrayList<String> exp = entry.defination;
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select Entry.word from Entry where Entry.word = '" + word + "'");
		if(resultSet.next() == true) return "entry existed";//已存在这个词条，不重复插入
		System.out.println(exp.size());
		statement.execute("insert into Entry values('" + word + "', " + exp.size() + ")");
		for(int i=0; i<exp.size(); i++){
			statement.execute("insert into Explaination values('" + word + "', " + i + ", '" + exp.get(i) + "')");
		}
		return "Entry [" + word + "] has been inserted into database";
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		DBConnection dbc = new DBConnection();
		ArrayList<String> res = dbc.getExplaination("commencement");
		for(String tmp: res){
			System.out.println(tmp);
		}
		
	}
}
