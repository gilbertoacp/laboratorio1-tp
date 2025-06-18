package data;

import java.io.File;
import java.io.FileWriter;

import entities.Response;
import exceptions.DAOException;

public class ResponseDAOImpl implements ResponseDAO {

	public ResponseDAOImpl() {
	}

	@Override
	public boolean saveResponse(Response response) throws DAOException {
		boolean result = false;
        String fileName = "response.txt"; 
        String fileContent = response.getBody();
        String userHomeDir = System.getProperty("user.home");
        String filePath = userHomeDir + File.separator + fileName;
        File file = new File(filePath);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("An error has ocurred while saving the file: " + e.getMessage(), e);
        }
		return result;
	}

}
