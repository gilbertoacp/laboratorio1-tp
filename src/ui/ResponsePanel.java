package ui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import entities.Response;
import utils.ViewUtils;

public class ResponsePanel extends JPanel {
	
	private JTextArea responseContent;
	private JTextArea formattedResponseContent;
	private JTabbedPane responsePane;

	public ResponsePanel(PanelManager panelManager) {
		this.setLayout(new BorderLayout());
		
		this.responseContent = this.createResponseContent();
		this.formattedResponseContent = this.createResponseContent();
		
		Border innerBorder = BorderFactory.createTitledBorder("Status code: ");
		Border outerBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		this.setBorder(BorderFactory.createCompoundBorder(innerBorder, outerBorder));
		
		this.responsePane = new JTabbedPane();
		
		this.responsePane.add("Raw", ViewUtils.createScrollableComponent(this.responseContent, true, true));
		this.responsePane.add("Pretty", ViewUtils.createScrollableComponent(this.formattedResponseContent, true, true));
		
		this.add(responsePane , BorderLayout.CENTER);
	}
	
	public void setResponseContentText(Response response) {
		
		if(this.responsePane.getTabCount() == 3)
			this.responsePane.removeTabAt(this.responsePane.getTabCount() - 1);
		
		Border innerBorder = BorderFactory.createTitledBorder("Status code: " + response.getCode());
		Border outerBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		this.setBorder(BorderFactory.createCompoundBorder(innerBorder, outerBorder));
		
		this.responseContent.setText(response.getBody());
		this.responseContent.setCaretPosition(0);
		
		HashMap<String, String> headers = response.getHeaders();
		System.out.println(headers);
		String contentType = this.getContentType(headers);
		String formattedContent = "JSON, XML, PNG, JPG are only supported";
		String cleanedResponseContent = response.getBody().replaceAll("\\s+", "");
		System.out.println(contentType.length());
		
		if(contentType != null) {
			if(contentType.contains(";"))
				contentType = contentType.split(";")[0];
	
			switch(contentType) {
				case "application/json":
					formattedContent = ViewUtils.formatJSON(cleanedResponseContent);
					break;
					
				case "text/xml":
				case "application/xml":
					formattedContent = ViewUtils.formatXML(cleanedResponseContent);
					break;
					
				case "image/png":
				case "image/svg+xml":
				case "image/jpeg":
					InputStream inputStream = new ByteArrayInputStream(response.getBody().getBytes());
					try {
						BufferedImage image = ImageIO.read(inputStream);

						if (image != null) {
							ImageIcon icon = new ImageIcon(image);
							JLabel label = new JLabel(icon);
							this.responsePane.addTab("Image", label);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
			}
			
			this.formattedResponseContent.setText(formattedContent);
			this.formattedResponseContent.setCaretPosition(0);
		}
			
	}
	
	private JTextArea createResponseContent() {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		return textArea;
	}
	
	private String getContentType(HashMap<String, String> headers) {
		String contentType = headers.get("content-type");
		if(contentType == null) {
			contentType = headers.get("Content-Type");
		}
		return contentType;
	}
	
	
}
