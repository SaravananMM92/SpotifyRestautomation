package api.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;

public class PlayListPropStorageUtil {
	
	public static void PlayListPropWritetoFile(String filePath, Response response) throws IOException
	{
		String id = response.jsonPath().getString("id");
		String idname = response.jsonPath().getString("name");
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("id", id);
		data.put("name", idname);
		
		File responseFile = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> existingList = new ArrayList<>();

        if (responseFile.exists() && responseFile.length() > 0) {
            try {
                existingList = mapper.readValue(responseFile, new TypeReference<List<Map<String, String>>>() {});
            } catch (Exception e) {
                System.out.println("Warning: Existing file is not a valid array. It will be overwritten.");
            }
        }
        existingList.add(data);
        mapper.writerWithDefaultPrettyPrinter().writeValue(responseFile, existingList);
    }
}
