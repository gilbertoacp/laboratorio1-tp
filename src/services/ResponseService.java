package services;

import data.ResponseDAO;
import data.ResponseDAOImpl;
import entities.Response;
import exceptions.DAOException;
import exceptions.ServiceException;

public class ResponseService {

	public ResponseService() {
	}

	public boolean saveResponseToFile(Response response) throws ServiceException {
		boolean result = false;
		try {
			ResponseDAO dao = new ResponseDAOImpl();
			result = dao.saveResponse(response);
		} catch(DAOException e) {
			throw new ServiceException(e);
		}
		return result;
	}
}
