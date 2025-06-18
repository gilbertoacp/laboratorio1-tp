package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import entities.History;
import exceptions.DAOException;
import utils.HttpUtils;

public class HistoryDAOImpl implements HistoryDAO {

	private DatabaseManagement db;
	
	public HistoryDAOImpl() {
		this.db = new DatabaseManagement();
		
		try {
			this.createDBTable();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void createDBTable() throws SQLException {
		this.db.connect();
		String sql = "CREATE TABLE IF NOT EXISTS history (" 
					+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "method  TEXT,"
					+ "url     TEXT,"
					+ "body    TEXT,"
					+ "headers TEXT,"
					+ "datetime TEXT"
					+ ");";
	
		this.db.executeTransaction(sql);
		this.db.close();
	}

	@Override
	public boolean saveRequestHistory(History historyRequest) throws DAOException {
		boolean result = false;
		
		try {
			this.db.connect();
			String sql = "INSERT INTO history (method, url, body, headers, datetime) VALUES ("
					+ "'" + historyRequest.getRequest().getMethod() + "', "
					+ "'" + historyRequest.getRequest().getURL() + "', "
					+ "'" + historyRequest.getRequest().getBody() + "', "
					+ "'" + HttpUtils.headersToString(historyRequest.getRequest().getHeaders()) + "', "
					+ "'" + historyRequest.getTimestamp() + ""
					+ "'" + ");";
			this.db.executeTransaction(sql);
			result = true;
			this.db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DAOException("History could not be stored", e1);
		}

		return result;
	}

	@Override
	public boolean deleteRequestHistory(History historyRequest) throws DAOException {
		boolean result = false;
		try {
			this.db.connect();
			String sql = "DELETE FROM history WHERE ID = " + historyRequest.getRequest().getId() + ";";
			this.db.executeTransaction(sql);
			result = true;
			this.db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Could not delete the following history: " + historyRequest.getRequest().getURL(), e);
		}
		
		return result;
	}

	@Override
	public List<History> getHistory() throws DAOException {
		List<History> result = new ArrayList<History>();
		
		try {
			this.db.connect();
			String sql = "SELECT * FROM history;";
			String[] attributes = {"ID", "method", "url", "body", "headers", "datetime"};
			List<HashMap<String, String>> dbResult = this.db.executeQuery(sql, attributes);
			
			// Filas
			for(HashMap<String, String> record: dbResult) {
				History history = new History();
				
				// Columnas
				for(Entry<String, String> requestObject: record.entrySet()) {
					switch(requestObject.getKey()) {
						case "ID":
							history.getRequest().setId(Integer.parseInt(requestObject.getValue()));
							break;
						case "method":
							history.getRequest().setMethod(requestObject.getValue());
							break;
						
						case "url":
							history.getRequest().setURL(requestObject.getValue());
							break;
						
						case "body":
							history.getRequest().setBody(requestObject.getValue());
							break;
							
						case "headers":
							history.getRequest().setHeaders(HttpUtils.stringToHeaders(requestObject.getValue()));
							break;
							
						case "datetime":
							history.setTimestamp(requestObject.getValue());
							break;
					}
				}
				result.add(history);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DAOException("Couldn't fetch history entries", e1);
		}
		
		return result;
	}


}
