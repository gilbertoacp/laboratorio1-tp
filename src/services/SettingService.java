package services;

import data.SettingDAO;
import data.SettingDAOImpl;
import entities.Setting;
import exceptions.DAOException;
import exceptions.ServiceException;

public class SettingService {

	public SettingService() {
	}

	public boolean saveSettingToFile(Setting setting) throws ServiceException {
		boolean result = false;
		
		try {
			SettingDAO dao = new SettingDAOImpl();
			result = dao.saveResponse(setting);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		
		return result;
	}
}
