package data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Request;
import entities.Response;
import exceptions.DAOException;
import exceptions.InvalidJsonException;
import kotlin.Pair;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import utils.HttpUtils;

public class RequestDAOImpl implements RequestDAO {

	private DatabaseManagement db;
	
	public RequestDAOImpl() {
		this.db = new DatabaseManagement();
		
		try {
			this.createDBTable();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void createDBTable() throws SQLException {
		this.db.connect();
		String sql = "CREATE TABLE IF NOT EXISTS requests (" 
					+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "method  TEXT,"
					+ "url     TEXT,"
					+ "body    TEXT,"
					+ "headers TEXT"
					+ ");";
	
		this.db.executeTransaction(sql);
		this.db.close();
	}

	@Override
	public boolean saveRequest(Request request) throws DAOException {
		boolean result = false;
			
		try {
			this.db.connect();
			String sql = "INSERT INTO requests (method, url, body, headers) VALUES ("
					+ "'" + request.getMethod() + "', "
					+ "'" + request.getURL() + "', "
					+ "'" + request.getBody() + "', "
					+ "'" + HttpUtils.headersToString(request.getHeaders()) + ""
					+ "'" + ");";
			this.db.executeTransaction(sql);
			result = true;
			this.db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DAOException("Request could not be stored", e1);
		}

		return result;
	}

	@Override
	public List<Request> getRequests() throws DAOException {
		List<Request> result = new ArrayList<Request>();
		
		try {
			this.db.connect();
			String sql = "SELECT * FROM requests;";
			String[] attributes = {"ID", "method", "url", "body", "headers"};
			List<HashMap<String, String>> dbResult = this.db.executeQuery(sql, attributes);
			
			// Filas
			for(HashMap<String, String> record: dbResult) {
				Request request = new Request();
				
				// Columnas
				for(Entry<String, String> requestObject: record.entrySet()) {
					switch(requestObject.getKey()) {
						case "ID":
							request.setId(Integer.parseInt(requestObject.getValue()));
							break;
						case "method":
							request.setMethod(requestObject.getValue());
							break;
						
						case "url":
							request.setURL(requestObject.getValue());
							break;
						
						case "body":
							request.setBody(requestObject.getValue());
							break;
							
						case "headers":
							request.setHeaders(HttpUtils.stringToHeaders(requestObject.getValue()));
							break;
					}
				}
				result.add(request);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DAOException("Couldn't fetch HTTP request entries", e1);
		}
		
		return result;
	}
	

	@Override
	public boolean deleteRequest(Request request) throws DAOException {
		boolean result = false;
		try {
			this.db.connect();
			String sql = "DELETE FROM requests WHERE ID = " + request.getId() + ";";
			this.db.executeTransaction(sql);
			result = true;
			this.db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Could not delete the following request: " + request.getURL(), e);
		}
		
		return result;
	}

	@Override
	public Response sendRequest(Request request) throws DAOException, InvalidJsonException {
		Response response = null;
		boolean isJsonHeader = false;
		RequestBody body = new FormBody.Builder().build();

		OkHttpClient httpClient = new OkHttpClient();
		okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
		
		if(request.getHeaders() != null) {
		    String contentType = request.getHeaders().get("Content-Type");
		    String contentTypeLower = request.getHeaders().get("content-type");

		    if ((contentType != null && contentType.equalsIgnoreCase("application/json")) ||
		        (contentTypeLower != null && contentTypeLower.equalsIgnoreCase("application/json"))) {
		        isJsonHeader = true;
		    }
			
			for(Entry<String, String> header: request.getHeaders().entrySet()) {
				requestBuilder.addHeader(header.getKey(), header.getValue());
			}
			
			if(isJsonHeader) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					mapper.writer().writeValueAsString(mapper.readTree(request.getBody()));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new InvalidJsonException("Body is not a valid JSON", e);
				}
				body = RequestBody.create(request.getBody(), MediaType.get("application/json; charset=utf-8"));
			}
		}
		
		if(!isJsonHeader) {
			okhttp3.FormBody.Builder formBuilder = new FormBody.Builder();
			List<String> bodyEntries = Arrays.asList(request.getBody().split("&"));
			
			for(String entry: bodyEntries) {
				if(entry.contains("=")) {
					String key = entry.split("=")[0];
					String value = entry.split("=")[1]; 
					formBuilder.add(key, value);
				}
			}
			
			body = formBuilder.build();			
		}
       
        requestBuilder.url(request.getURL());
        
        switch(request.getMethod()) {
        	case "GET":
        		requestBuilder.get();
        	break;
        	
        	case "POST":
        		requestBuilder.post(body);
        	break;
        		
        	case "PUT":
        		requestBuilder.put(body);
        	break;
        	
        	case "DELETE":
        		requestBuilder.delete();
        	break;
        	
        	default:
        		requestBuilder.get();
        	break;
        }
        
        try {
        	okhttp3.Response httpResponse = httpClient.newCall(requestBuilder.build()).execute();
        	Iterator<Pair<String, String>> iterator = httpResponse.headers().iterator();
        	
        	HashMap<String, String> responseHeaders = new HashMap<String, String>();
        	
        	while(iterator.hasNext()) {
        		Pair<String, String> item = iterator.next();
        		responseHeaders.put(item.getFirst(), item.getSecond());
        	}
        	
        	response = new Response(responseHeaders, httpResponse.body().string(), httpResponse.code());
        	httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DAOException(e);
        }
        
        return response;
	}
}
