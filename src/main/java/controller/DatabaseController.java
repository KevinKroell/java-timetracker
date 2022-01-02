package main.java.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {

	private final String JDBC_DRIVER = "org.h2.Driver";
	private final String DB_URL = "jdbc:h2:./src/main/resources/data/db"; // relative path
	private String user;
	private String pass;
	private Connection dbConnection; // H2 Database // Logout: Close DB Connection

	public DatabaseController(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	public Connection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getJDBC_DRIVER() {
		return JDBC_DRIVER;
	}

	public String getDB_URL() {
		return DB_URL;
	}

	public void connect() {
		try {
			Class.forName(JDBC_DRIVER);
			dbConnection = DriverManager.getConnection(DB_URL, user, pass); // username is not case sensitive
			System.out.println("database connected as " + user + ".");
		} catch (SQLException se) { // Handle JDBC errors
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace(); // Handle errors for Class.forName()
		}
	}

	public void close() {
		try {
			dbConnection.close();
			System.out.println("database connection closed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert(String sql) {
		Statement statement = null;
		try {
			Class.forName(JDBC_DRIVER);
			dbConnection = DriverManager.getConnection(DB_URL, user, pass);
			statement = dbConnection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Executed '" + sql + "'.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Object> query(String sql) {
		ArrayList<Object> resultArrayList = new ArrayList<>();
		ResultSet rs = null;
		Statement statement = null;
		try {
			Class.forName(JDBC_DRIVER);
			dbConnection = DriverManager.getConnection(DB_URL, user, pass);
			statement = dbConnection.createStatement();
			rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				int i = 1;
				resultArrayList.add(rs.getObject(i));
				//System.out.println(rs.getString(i));
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) 
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (statement != null) 
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			dbConnection.close();			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return resultArrayList;
	}
	
	public void initializeDB() {
		if(executeSQLScript("./src/main/resources/data/createTables.sql") == 0 && executeSQLScript("./src/main/resources/data/insertDummyData.sql") == 0) {
			System.out.println("Database successfully initialized");
		}
	}

	public int executeSQLScript(String scriptFilePath) {
		BufferedReader bReader = null;
		Statement statement = null;

		try {
			// Load Driver for H2
			Class.forName(JDBC_DRIVER);
			// Create Connection
			dbConnection = DriverManager.getConnection(DB_URL, user, pass);
			// Create Statement object
			statement = dbConnection.createStatement();
			// Read file
			bReader = new BufferedReader(new FileReader(scriptFilePath));
			String line = null;

			while ((line = bReader.readLine()) != null) {
				// execute query
				statement.executeUpdate(line);
			}

			statement.close();
			bReader.close();
			dbConnection.close();
			
			System.out.println("SQL-Script " + scriptFilePath + " executed successfully");
		} catch (Exception e) {
			System.out.println("Problem executing SQL Script");
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
}