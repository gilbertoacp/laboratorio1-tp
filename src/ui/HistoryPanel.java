package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.History;
import entities.Request;
import utils.ViewUtils;

public class HistoryPanel extends JPanel implements ActionListener, ListSelectionListener {

	private DefaultListModel<Request> listRequestModel;
	private JList<Request> listRequests;
	private DefaultListModel<History> listHistoryModel;
	private JList<History> listHistory;
	private JButton deleteButton;
	private PanelManager panelManager;

	public HistoryPanel(PanelManager panelManager) {
		this.setLayout(new BorderLayout());
		
		this.panelManager = panelManager;
		
		this.listRequestModel = new DefaultListModel<>();
		this.listRequests = new JList<Request>(this.listRequestModel);
		this.listRequests.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listRequests.addListSelectionListener(this);
		this.listRequests.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Request) {
					Request request = (Request) value;
					setText(request.getMethod() + " - " + request.getURL());
				}
				return this;
			}
		});
		
		this.listHistoryModel = new DefaultListModel<>();
		this.listHistory = new JList<History>(this.listHistoryModel);
		this.listHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listHistory.addListSelectionListener(this);
		this.listHistory.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof History) {
					History history = (History) value;
					setText(history.getRequest().getMethod() + " - " + history.getRequest().getURL() + " - " + history.getTimestamp());
				}
				return this;
			}
		});
		
		this.deleteButton = new JButton("Delete");
		this.deleteButton.addActionListener(this);

		JTabbedPane sidePanelPane = new JTabbedPane();
		sidePanelPane.addTab("Favorites", this.listRequests);
		sidePanelPane.addTab("History", this.listHistory);

		this.add(ViewUtils.createScrollableComponent(sidePanelPane, true, true), BorderLayout.CENTER);
		this.add(this.deleteButton, BorderLayout.SOUTH);
	}

	public void populateRequestsList(List<Request> requests) {
		this.listRequestModel.clear();
		for (Request request : requests) {
			this.listRequestModel.addElement(request);
		}
	}
	
	public void populateHistoryList(List<History> history) {
		this.listHistoryModel.clear();
		for (History historyElement : history) {
			this.listHistoryModel.addElement(historyElement);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
		Request selectedRequest = this.listRequests.getSelectedValue();

		if (this.deleteButton == clickedButton && selectedRequest != null) {
			this.panelManager.deleteRequest(selectedRequest);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {		
		if (!e.getValueIsAdjusting()) {
			int selectedIndex = -1;
			Request selectedValue = null;
			
			if(this.listHistory == e.getSource()) {
				selectedIndex = this.listHistory.getSelectedIndex();
				if(this.listHistory.getSelectedValue() == null) return;
				selectedValue = this.listHistory.getSelectedValue().getRequest();
			} else if (this.listRequests == e.getSource()) {
				selectedIndex = this.listRequests.getSelectedIndex();
				selectedValue = this.listRequests.getSelectedValue();
			}
			
			if (selectedIndex != -1) {
				this.panelManager.populateSelectedRequest(selectedValue);
			}
			
			
		}
	}

}
