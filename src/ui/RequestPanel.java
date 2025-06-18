package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import entities.History;
import entities.Request;
import utils.ViewUtils;

public class RequestPanel extends JPanel implements ActionListener, KeyListener {

	private JButton sendButton;
	private JButton saveButton;
	private JPanel topRequestPanel;
	private JTextArea bodyTextArea;
	private JTextField urlTextField;
	private JPanel bottomRequestPanel;
	private JTextArea headersTextArea;
	private PanelManager panelManager;
	private JComboBox<String> methodComboxBox;
	private JTable paramsTable;
	private JTable headersTable;

	public RequestPanel(PanelManager panelManager) {
		this.panelManager = panelManager;
		
		this.urlTextField = new JTextField();
		this.urlTextField.addKeyListener(this);

		this.saveButton = this.createButton("+");
		this.bodyTextArea = this.createTextArea();
		this.sendButton = this.createButton("Send");
		this.headersTextArea = this.createTextArea();
		
		this.paramsTable = this.createTablePanel("paramsTable");
		this.headersTable = this.createTablePanel("headersTable");
		
		this.methodComboxBox = new JComboBox<String>(new String[] { "GET", "POST", "PUT", "PATCH", "DELETE" });
		this.topRequestPanel = this.buildTopPanel(this.methodComboxBox, this.sendButton, this.saveButton, this.urlTextField);
		this.bottomRequestPanel = this.buildBottomPanel(
				this.headersTextArea, 
				this.bodyTextArea, 
				this.createGridPanel(this.paramsTable, "paramsTable"),
				this.createGridPanel(this.headersTable, "headersTable")
				);
		
		this.setLayout(new BorderLayout());

		this.add(this.topRequestPanel, BorderLayout.NORTH);
		this.add(this.bottomRequestPanel, BorderLayout.CENTER);
	}
	
	public void fillRequest(Request request) {
		this.bodyTextArea.setText(request.getBody());
		this.urlTextField.setText(request.getURL());
		this.methodComboxBox.setSelectedItem(request.getMethod());
		// this.headersTextArea.setText(HttpUtils.headersToString(request.getHeaders()));
		this.populateQueryParamsTable(request.getURL());
		this.populateHeadersTable(request.getHeaders());
	}
	
	private JPanel buildTopPanel(JComboBox<String> methodComboBox, JButton sendButton, JButton saveButton, JTextField urlTextField) {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(methodComboBox);
		topPanel.add(urlTextField);
		topPanel.add(sendButton);
		topPanel.add(saveButton);
		return topPanel;
	}
	
	private JPanel buildBottomPanel(JTextArea headersTextArea, JTextArea bodyTextArea, 
			JPanel paramsTable, JPanel headersTable) {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		
		JTabbedPane requestConstructorPane = new JTabbedPane();
		
		requestConstructorPane.addTab("Params", paramsTable);
		requestConstructorPane.addTab("Headers", headersTable);
		requestConstructorPane.addTab("Body", this.createRequestBuilderPanel(bodyTextArea));
		  
		bottomPanel.add(requestConstructorPane, BorderLayout.CENTER);
		
		return bottomPanel;
	}
		
	private JPanel createRequestBuilderPanel(JTextArea textArea) {
		JPanel rawTextPanel = new JPanel();
		rawTextPanel.setLayout(new BorderLayout());
		rawTextPanel.add(ViewUtils.createScrollableComponent(textArea, true, true), BorderLayout.CENTER);
		return rawTextPanel;
	}
	
	private JButton createButton(String content) {
		JButton button = new JButton(content);
		button.addActionListener(this);
		return button;
	}
	
	private JButton createButton(String content, String key, String value) {
		JButton button = this.createButton(content);
		AccessibleContext context = button.getAccessibleContext();
		context.setAccessibleName(key + "::" + value);
		return button;
	}
		
	private JTextArea createTextArea() {
		JTextArea textArea = new JTextArea();
		return textArea;
	}
		
	private JTable createTablePanel(String identifier) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] { "Key", "Value" });
		JTable paramsTablePanel = new JTable();
		AccessibleContext context = paramsTablePanel.getAccessibleContext();
		context.setAccessibleName(identifier);
		paramsTablePanel.setModel(model);
		paramsTablePanel.addKeyListener(this);
		return paramsTablePanel;
	}
	
	private JPanel createGridPanel(JTable table, String identifier) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
	
		JPanel bottomButtonsPanel = new JPanel();
		bottomButtonsPanel.setLayout(new FlowLayout());
		bottomButtonsPanel.add(this.createButton("Add", identifier, "addButton"));
		bottomButtonsPanel.add(this.createButton("Delete", identifier, "deleteButton"));
		
		panel.add(ViewUtils.createScrollableComponent(table, true, true), BorderLayout.CENTER);
		panel.add(bottomButtonsPanel, BorderLayout.SOUTH);

		return panel;
	}
	
	private HashMap<String, String> extractQueryParams(String URL) {
		HashMap<String, String> queryParams = new HashMap<String, String>();

		String regex = "[?&]([^=&]+)(?:=([^&]*))?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(URL);
        
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            
            queryParams.put(key, value);
        }

		return queryParams;
	}
	
	private void populateQueryParamsTable(String URL) {
		HashMap<String, String> queryParams = this.extractQueryParams(URL);
		DefaultTableModel model =  (DefaultTableModel) this.paramsTable.getModel();
		model.setRowCount(0);
		
		for(Entry<String, String> entry: queryParams.entrySet()) {
			model.addRow(new String[] { entry.getKey(), entry.getValue() });
		}
	}
	
	private void populateHeadersTable(HashMap<String, String> headers) {
		
		if(headers == null)
			return;
		
		DefaultTableModel model =  (DefaultTableModel) this.headersTable.getModel();
		model.setRowCount(0);
		for(Entry<String, String> entry: headers.entrySet()) {
			model.addRow(new String[] { entry.getKey(), entry.getValue() });
		}
	}
	
	private HashMap<String, String> extractHeaders() {
		HashMap<String, String> headers = new HashMap<String, String>();
		DefaultTableModel model = (DefaultTableModel) this.headersTable.getModel();
		List<String> values = new ArrayList<String>();
		
		for (int row = 0; row < model.getRowCount(); row++) {
		    for (int col = 0; col < model.getColumnCount(); col++) {
		        Object value = model.getValueAt(row, col);
		        values.add(value.toString());
		    }
		}
		
        for (int i = 0; i < values.size(); i += 2) {
            String key = values.get(i);
            String value = values.get(i + 1);
            headers.put(key, value);
        }
        
        return headers;
	}
	
	private void handleKeyEvents(KeyEvent e) {		
		
		if(e.getSource() instanceof JTable) {
			JTable table = (JTable) e.getSource();
			AccessibleContext context = table.getAccessibleContext();
			
			if(context.getAccessibleName() == "paramsTable") {
				
				String queryParams = "";
				DefaultTableModel model = (DefaultTableModel) this.paramsTable.getModel();
				
				List<String> rowsColumnsContent = new ArrayList<String>();
				
				for (int row = 0; row < model.getRowCount(); row++) {
				    for (int col = 0; col < model.getColumnCount(); col++) {
				        Object value = model.getValueAt(row, col);
				        rowsColumnsContent.add(value.toString());
				    }
				}
				
		        for (int i = 0; i < rowsColumnsContent.size(); i += 2) {
		            String key = rowsColumnsContent.get(i);
		            String value = rowsColumnsContent.get(i + 1);
		                  
		            if(i == 0) 
		            	queryParams += "?";
		            
		            queryParams += key + "=" + value;
		            
		            if(i != rowsColumnsContent.size() - 2)
		            	queryParams += "&";
		        }
		        
		        Pattern pattern = Pattern.compile("^([^:/?#]+)://([^/?#]+)(/[^?#]*)?");
		        Matcher matcher = pattern.matcher(this.urlTextField.getText());
		        
		        if (matcher.find()) {
		            String schema = matcher.group(1);
		            String domain = matcher.group(2);
		            String path = matcher.group(3) != null ? matcher.group(3) : "/";
		            this.urlTextField.setText(schema + "://" + domain + path + queryParams);
		        }
			}
		}
		
		if(e.getSource() instanceof JTextField) {
			JTextField textArea = (JTextField) e.getSource();
			this.populateQueryParamsTable(textArea.getText());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		this.handleKeyEvents(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.handleKeyEvents(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.handleKeyEvents(e);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
			
		Request request = new Request(
				this.urlTextField.getText().trim(),
				this.methodComboxBox.getSelectedItem().toString(),
				this.extractHeaders(),
				this.bodyTextArea.getText().replaceAll("\\s+", "")
				);
		
		if(this.sendButton == clickedButton) {
			this.panelManager.sendButtonHandler(request);
			this.panelManager.saveRequestHistory(new History(request, LocalDateTime.now().toString()));
		} else if (this.saveButton == clickedButton) {
			this.panelManager.saveRequest(request);
		}
		
		AccessibleContext context = clickedButton.getAccessibleContext();
		
		if(!context.getAccessibleName().contains("::")) return;
		
		String tableName = context.getAccessibleName().split("::")[0];
		String buttonName = context.getAccessibleName().split("::")[1];
		JTable selectedTable = null;
		
		switch(tableName) {
			case "paramsTable":
				selectedTable = this.paramsTable;
				break;
			
			case "headersTable":
				selectedTable = this.headersTable;
				break;
		}
		
		if(selectedTable != null) {
			DefaultTableModel model =  (DefaultTableModel) selectedTable.getModel();
			
			switch(buttonName) {
				case "deleteButton":
					int index = selectedTable.getSelectedRow();
					if(index != -1) {
						model.removeRow(index);
					}
					break;
					
				case "addButton":
					model.addRow(new String[] { "", "" });
					break;
			}			
		}
		
	}

}
