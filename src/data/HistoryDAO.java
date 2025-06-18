package data;

import java.util.List;
import entities.History;
import exceptions.DAOException;

public interface HistoryDAO {
	public boolean saveRequestHistory(History historyRequest) throws DAOException;
	public boolean deleteRequestHistory(History historyRequest) throws DAOException;
	public List<History> getHistory() throws DAOException;
}
