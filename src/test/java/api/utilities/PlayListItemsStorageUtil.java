package api.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.restassured.response.Response;

public class PlayListItemsStorageUtil {
	
	public static void StoreTrackURIs(String filePath, Response response) throws JsonGenerationException, JsonMappingException, IOException
	{
		
		JSONObject jobj = new JSONObject(response.asString());
		
		List<String> uri = new ArrayList<String> ();
		
		for(int i=0;i<jobj.getJSONObject("tracks").getJSONArray("items").length(); i++)
		{
			String uriString = jobj.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getString("uri");
			String[] parts = uriString.split(":");
			String finalText = parts[parts.length - 1];
			uri.add(finalText);
		}		
		System.out.println(uri);
		WriteJsonFileUtil.writeListtoJsonFile(filePath, uri);
		
	}

}
