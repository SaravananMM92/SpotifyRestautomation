package api.utilities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.payload.ThreadSafeDataStore;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TokenStorageUtil {

    @SuppressWarnings("unchecked")
	public static void writeTokensToFile(String filePath, Response response) throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("Updating token_store.json file");
        ObjectMapper mapper = new ObjectMapper();
        String jsonresponse = response.asString(); 
        Map<String, Object> responseJson;
        responseJson = mapper.readValue(jsonresponse, Map.class);
        
        File file = new File(filePath);
        Map<String, Object> existingJson = mapper.readValue(file, Map.class);
       
        // Convert "expires_in" to absolute time (now + seconds)
        if (responseJson.containsKey("expires_in") && responseJson.get("expires_in") != null) {
            try {
                int expiresIn = Integer.parseInt(responseJson.get("expires_in").toString());
                long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                long absoluteExpiry = currentTimeInSeconds + expiresIn;
                responseJson.put("expires_in", absoluteExpiry);  // replace with timestamp
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Invalid 'expires_in' format in response.");
            }
        } 
        
        // Filter out null values before updating
        for (Map.Entry<String, Object> entry : responseJson.entrySet()) {
            if (entry.getValue() != null) {
            	existingJson.put(entry.getKey(), entry.getValue());
            }
        }
      
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, existingJson);
            System.out.println("✅ Tokens saved to: " + filePath);
        } catch (Exception e) {
            System.err.println("❌ Failed to write token JSON: " + e.getMessage());
        }
    }
    
    public static void loadTokensFromFile(String filePath) {
        try {
        	System.out.println("Auth token loading from token_store.json file");
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
			Map<String, Object> tokenData = mapper.readValue(new File(filePath), Map.class);

            ThreadSafeDataStore.setAccessToken((String) tokenData.get("access_token"));
            ThreadSafeDataStore.setRefreshToken((String) tokenData.get("refresh_token"));
            ThreadSafeDataStore.setExpiresAt(((Number) tokenData.get("expires_in")).longValue());
            ThreadSafeDataStore.setScope((String) tokenData.get("scope"));

            System.out.println("✅ Tokens loaded from: " + filePath);
        } catch (Exception e) {
            System.err.println("❌ Failed to load token JSON: " + e.getMessage());
        }
    }

}
