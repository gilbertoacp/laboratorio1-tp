package data;

import java.io.File;
import java.io.FileWriter;

import entities.Setting;
import exceptions.DAOException;

public class SettingDAOImpl implements SettingDAO{

	public SettingDAOImpl() {
	}

	@Override
	public boolean saveResponse(Setting setting) throws DAOException {
		boolean result = false;
        String fileName = "response.txt"; 
        String fileContent = setting.toString();
        String userHomeDir = "~";
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
