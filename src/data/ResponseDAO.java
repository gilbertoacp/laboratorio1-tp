package data;

import entities.Response;
import exceptions.DAOException;

public interface ResponseDAO {
	public boolean saveResponse(Response response) throws DAOException;
}
