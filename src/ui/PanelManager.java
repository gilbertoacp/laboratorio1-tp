package ui;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import entities.History;
import entities.Request;
import entities.Response;
import exceptions.ServiceException;
import services.HistoryService;
import services.RequestService;
import utils.ViewUtils;

public class PanelManager  {
	
	private RequestPanel requestPanel;
	private HistoryPanel historyPanel;
	private ResponsePanel responsePanel;
	
	public PanelManager() {
		JFrame mainFrame = new JFrame("Clon de Postman");
		mainFrame.setLayout(new GridLayout(1, 1));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000, 800);
		
		this.requestPanel = new RequestPanel(this);
		this.responsePanel = new ResponsePanel(this);
		this.historyPanel = new HistoryPanel(this);
	
		JPanel secondColumn = new JPanel();	
		secondColumn.setLayout(new GridLayout(2, 1));
		secondColumn.add(this.requestPanel);
		secondColumn.add(this.responsePanel);
		
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.historyPanel, secondColumn);
        splitPane.setDividerLocation(200);  
        splitPane.setOneTouchExpandable(true);
		
        mainFrame.add(splitPane);
        
        this.reloadFavoritesList();
        this.reloadHistoryList();
        
		mainFrame.setVisible(true);	
	}
	
	public void sendButtonHandler(Request request) {
		RequestService requestService = new RequestService();
		try {
			Response response = requestService.sendHttpRequest(request);
			this.responsePanel.setResponseContentText(response);
		} catch (ServiceException e) {
			ViewUtils.displayMessageBox(e.getCause().getMessage());
		}
	}
	
	public void reloadFavoritesList() {
		RequestService requestService = new RequestService();
		try {
			List<Request> requests = requestService.getStoredRequests();
			this.historyPanel.populateRequestsList(requests);
		} catch(ServiceException e) {
			ViewUtils.displayMessageBox(e.getCause().getMessage());
		}
	}
	
	public void reloadHistoryList() {
		HistoryService historyService = new HistoryService();
		try {
			List<History> histories = historyService.getHistory();
			this.historyPanel.populateHistoryList(histories);
		} catch(ServiceException e) {
			ViewUtils.displayMessageBox(e.getCause().getMessage());
		}
	}
	
	public void deleteRequest(Request request) {
		RequestService requestService = new RequestService();
		try {
			requestService.deleteRequest(request);
			this.reloadFavoritesList();
		} catch (ServiceException e) {
			ViewUtils.displayMessageBox(e.getCause().getMessage());
		}
	}
	
	public void saveRequest(Request request) {
		RequestService requestService = new RequestService();
		try {
			requestService.storeNewRequest(request);
			this.reloadFavoritesList();
		} catch (ServiceException e) {
			ViewUtils.displayMessageBox(e.getCause().getMessage());
		}
	}
	
	public void saveRequestHistory(History history) {
		HistoryService historyPanel = new HistoryService();
		try {
			historyPanel.storeHistory(history);
			this.reloadHistoryList();
		} catch (ServiceException e) {
			ViewUtils.displayMessageBox(e.getCause().getMessage());
		}
	}
	
	public void populateSelectedRequest(Request request) {
		this.requestPanel.fillRequest(request);
	}
}
