package utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ViewUtils {

	public ViewUtils() {
	}
	
	public static void displayMessageBox(String message) {
		JOptionPane.showMessageDialog(new JPanel(), message);
	}
	
	public static JScrollPane createScrollableComponent(JComponent component, boolean horizontalScrolling, boolean verticalScrolling) {
		JScrollPane scrollPane = new JScrollPane(component);
		if(horizontalScrolling) 
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		
		if(verticalScrolling)
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return scrollPane;
	}
	
	public static String formatJSON(String jsonString) {
		String result = "";
		try {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.enable(SerializationFeature.INDENT_OUTPUT);
	        Object json = mapper.readValue(jsonString, Object.class);
	        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
	        result = writer.writeValueAsString(json);
		} catch(Exception e) {	
			System.out.println(e.getMessage());
		}
		
        return result;
	}
	
	public static String formatXML(String xmlString) {
		String result = "";

		try {
	        Document document = createDocument(xmlString);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        StringWriter stringWriter = new StringWriter();
	        StreamResult streamResult = new StreamResult(stringWriter);
	        DOMSource domSource = new DOMSource(document);
			transformer.transform(domSource, streamResult);
			result = stringWriter.toString();
		} catch (Exception e) { 
		}

        return result;
	}

    private static Document createDocument(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        return builder.parse(inputSource);
    }
}
