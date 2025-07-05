package api.utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WriteJsonFileUtil {
	
	public static void writeHashMaptoJsonFile(String filePath, HashMap<String, String> hash) throws JsonGenerationException, JsonMappingException, IOException
	{
		File file = new File(filePath);		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(file, HashMap.class);
		
	}
	

	public static void writeListtoJsonFile(String filePath, List<String> list) throws JsonGenerationException, JsonMappingException, IOException
	{
		File file = new File(filePath);		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
		
	}

}
