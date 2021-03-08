package tt.exporter;

import java.util.Arrays;
import java.util.List;

import io.vertx.core.json.JsonObject;

public class Parser {	
	
	private JsonObject jo;
	
	public Parser(final String encodedJson) {
		this.jo = new JsonObject(encodedJson);	
	}
	
	public Parser(final JsonObject jsonObj) {
		this.jo = jsonObj;	
	}
	
	public int parseInt(final String dottedString) {
		return (int) parseJson(dottedString);
	}
	
	public String parseString(final String dottedString) {
		return (String) parseJson(dottedString);
	}
	
	public boolean parseBool(final String dottedString) {
		return (Boolean) parseJson(dottedString);
	}
	
	private Object parseJson(final String dottedString) {
		List<String> selector = Arrays.asList(dottedString.split(":"));
		JsonObject jsonObj = this.jo;
		for (int i = 0; i  < selector.size()-1; i = i + 1) {
			jsonObj = jsonObj.getJsonObject(selector.get(i));
		}
		return jsonObj.getValue(selector.get(selector.size()-1));   
	}
	
}
