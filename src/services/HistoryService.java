package services;

import java.util.ArrayList;
import java.util.List;

import data.HistoryDAO;
import data.HistoryDAOImpl;
import entities.History;
import exceptions.DAOException;
import exceptions.ServiceException;

public class HistoryService {

	public HistoryService() {
	}

	public boolean storeHistory(History history) throws ServiceException {
		boolean result = false;
		try {
			HistoryDAO dao = new HistoryDAOImpl();
			result = dao.saveRequestHistory(history);
		} catch (DAOException e) {
			throw new ServiceException();
		}
		
		return result;
	}
	
	public List<History> getHistory() throws ServiceException {
		List<History> result = new ArrayList<History>();
		
		try {
			HistoryDAO dao = new HistoryDAOImpl();
			result = dao.getHistory();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		
		return result;
	}

	
	public boolean deleteHistory(History history) throws ServiceException {
		boolean result = true;

		try {
			HistoryDAO dao = new HistoryDAOImpl();
			result = dao.deleteRequestHistory(history);
		} catch(DAOException e) {
			throw new ServiceException(e);
		}
		
		return result;
	}
}
