package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

	public HttpUtils() {
	}
	
	public static String headersToString(HashMap<String, String> headers) {
		String result = "";
		
		if(headers == null) {
			return null;
		}
		
		for(Map.Entry<String, String> header: headers.entrySet()) {
			result += header.getKey() + ": " + header.getValue() + "\r\n"; 
		}
		
		return result;
	}
	
	public static HashMap<String, String> stringToHeaders(String stringHeaders) {
		if(stringHeaders == null || stringHeaders.isEmpty()) {
			return null;
		}
		
		HashMap<String, String> result = new HashMap<String, String>();
		List<String> parsedStringHeaders = Arrays.asList(stringHeaders.split("\n"));
		
		for(String entry: parsedStringHeaders) {
			String[] splitEntry = entry.split(": ");
			if(splitEntry.length == 2) {
				result.put(splitEntry[0].trim(), splitEntry[1].trim());
			}
		}
		
		return result;
	}
}
