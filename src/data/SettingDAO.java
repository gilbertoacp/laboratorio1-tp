package data;

import entities.Setting;
import exceptions.DAOException;

public interface SettingDAO {
	public boolean saveResponse(Setting setting) throws DAOException;
}
