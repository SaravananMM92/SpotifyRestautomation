package api.utilities;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadJsonPayload {

    public static Map<String, String> readJsonAsMap(String filePath) throws IOException 
    {
    	File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> payload = new HashMap<String, String>();
        
        payload = mapper.readValue(file, new TypeReference <Map<String, String>>() {});
        
        return payload;
                
    }
    
    public static List<Map<String, String>> readJsonAsListOfMaps(String filePath) throws IOException 
    {
    	File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> payload = new ArrayList<>();
        
        payload= mapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});
        
        return payload;
    }
    
    public static JsonNode readJsonTree(String filePath) throws JsonProcessingException, IOException
    {
    	File file = new File(filePath);
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode tree = mapper.readTree(file);
    	return tree;
    }

    public static String trackURIsfromJsonAsString(String filePath) throws JsonParseException, JsonMappingException, IOException
    {
        // JSON file path
        File file = new File(filePath);

        // Read JSON array from file
        ObjectMapper mapper = new ObjectMapper();
        List<String> trackIds = mapper.readValue(file, new TypeReference<List<String>>() {});

        // Format to spotify:track:<id>
        String uris = trackIds.stream()
                                   .map(id -> "spotify:track:" + id)
                                   .collect(Collectors.joining(","));

        return uris;
    }
    

    public static List<String> trackURIsfromJsonAsList(String filePath) throws JsonParseException, JsonMappingException, IOException
    {
        // JSON file path
        File file = new File(filePath);

        // Read JSON array from file
        ObjectMapper mapper = new ObjectMapper();
        List<String> trackIds = mapper.readValue(file, new TypeReference<List<String>>() {});

        // Format to spotify:track:<id>
        List<String> uris = trackIds.stream()
                                   .map(id -> "spotify:track:" + id)
                                   .collect(Collectors.toList());

        return uris;
    }
}

