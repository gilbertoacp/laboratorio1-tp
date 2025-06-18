package data;

import java.util.List;

import entities.Request;
import entities.Response;
import exceptions.DAOException;
import exceptions.InvalidJsonException;

public interface RequestDAO {
	public boolean saveRequest(Request request) throws DAOException;
	public List<Request> getRequests() throws DAOException;
	public Response sendRequest(Request request) throws DAOException, InvalidJsonException;
	public boolean deleteRequest(Request request) throws DAOException;
}
