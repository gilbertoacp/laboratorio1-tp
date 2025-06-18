package services;

import java.util.ArrayList;
import java.util.List;
import data.RequestDAO;
import data.RequestDAOImpl;
import entities.Request;
import entities.Response;
import exceptions.DAOException;
import exceptions.ServiceException;

public class RequestService {

	public RequestService() {
	}
	
	public List<Request> getStoredRequests() throws ServiceException {
		List<Request> result = new ArrayList<Request>();
		
		try {
			RequestDAO dao = new RequestDAOImpl();
			result = dao.getRequests();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		
		return result;
	}

	public boolean storeNewRequest(Request request) throws ServiceException {
		boolean result = false;
		
		try {
			RequestDAO dao = new RequestDAOImpl();
			result = dao.saveRequest(request);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

		return result;
	}
	
	public boolean deleteRequest(Request request) throws ServiceException {
		boolean result = true;

		try {
			RequestDAO dao = new RequestDAOImpl();
			result = dao.deleteRequest(request);
		} catch(DAOException e) {
			throw new ServiceException(e);
		}
		
		return result;
	}
	
	public Response sendHttpRequest(Request request) throws ServiceException {
		RequestDAO dao = new RequestDAOImpl();
		Response response = null;
		
		try {
			response = dao.sendRequest(request);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		return response;
	}
}
