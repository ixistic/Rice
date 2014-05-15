package com.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DatabaseConnector {

	private final String username;
	private final String url;
	private final String password;
	
	private Connection conn = null;
	private Statement statement = null;
	
	public DatabaseConnector(String url, String username, String password) {
		this.username = username;
		this.url = url;
		this.password = password;	
	}
	
	public void establishConnection() {
		regisDriver();
		connect();
	}
	
	private void regisDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("[ ok ] DRIVER REGISTERED");
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}
	}

	private void connect() {
		try {
			conn = DriverManager.getConnection(url, username, password);
			statement = conn.createStatement();
			System.out.println("[ ok ] CONNECTED");
		} catch (SQLException e) {
			System.out.println("ERROR : " + e);
		}
	}
	
	public void close() {
		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public ResultSet executeQuery(String sql) {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
