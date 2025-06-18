package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManagement {

	private Connection connection;
	private static final String SQLITE_CONNECTION_STRING = "jdbc:sqlite:clondepostman.db";

	public DatabaseManagement() {
	}

	public boolean connect() throws SQLException {
		boolean result = false;
		
		result = this.isActive();
		
		if (this.isActive()) {
			result = true;
		} else {
			this.connection = DriverManager.getConnection(SQLITE_CONNECTION_STRING);
			result = this.connection != null;
		}

		return result;
	}

	public boolean close() throws SQLException {
		boolean result = false;

		if (this.isActive()) {
			this.connection.close();
			result = true;
		}

		return result;
	}

	public boolean executeTransaction(String sql) throws SQLException {
		int result = 0;

		this.connection.setAutoCommit(false);
		Statement query = this.connection.createStatement();
		result = query.executeUpdate(sql);
		this.connection.commit();

		return result == 1;
	}

	public List<HashMap<String, String>> executeQuery(String sql, String[] attributes) throws SQLException {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		
		Statement query = this.connection.createStatement();
		ResultSet sqlResult = query.executeQuery(sql);

		while (sqlResult.next()) {
			HashMap<String, String> record = new HashMap<String, String>();
			for (int index = 0; index < attributes.length; index++) {
				record.put(attributes[index], sqlResult.getString(attributes[index]));
			}
			result.add(record);
		}

		return result;
	}

	private boolean isActive() throws SQLException {
		boolean result = false;
		result = this.connection != null && !this.connection.isClosed();
		return result;
	}

}
