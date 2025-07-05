package api.test;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import api.endpoints.SpotifyPlaylistEndPoints;
import api.payload.ThreadSafeDataStore;
import api.utilities.PlayListPropStorageUtil;
import api.utilities.ReadJsonPayload;
import io.restassured.response.Response;

//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

public class SpotifyPlaylistTest {
	
	public Logger logger= LogManager.getLogger(this.getClass()); 
	
	@Test(priority=1, dependsOnMethods = {"api.test.SpotifyCommonTest.getCurrentUserId"})
	void AddPlayListTest() throws JsonParseException, JsonMappingException, IOException
	{

		logger.info("****** Create new play list *******");
		
		String user_id = ThreadSafeDataStore.getUserId();
		String payloadjson = "src/test/resources/payloads/playlist1.json";
		
		Map<String, String> payload = ReadJsonPayload.readJsonAsMap(payloadjson);
		
		Response response = SpotifyPlaylistEndPoints.AddPlayList(user_id,payload);
		response.then().log().all();
		
		String playlist_id=response.jsonPath().get("id");
		ThreadSafeDataStore.setPlaylistId(playlist_id);
		
		Assert.assertEquals(response.getStatusCode(), 201);
		
		logger.info("****** Create new play done *******");
	}
	
	@Test(priority=2, dependsOnMethods = {"AddPlayListTest"})
	void GetPlayListTest()
	{
		logger.info("***** Read playlist *******");
		
		String playlist_id = ThreadSafeDataStore.getPlaylistId();
		
		Response response = SpotifyPlaylistEndPoints.GetPlaylists(playlist_id);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.jsonPath().get("id"),playlist_id);
		Assert.assertEquals(response.jsonPath().get("name"), "Test1");
		Assert.assertEquals(response.jsonPath().get("description"), "Test1 playlist description");
		Assert.assertEquals(response.jsonPath().getBoolean("public"), true);
		
		
		logger.info("***** Read playlist done*******");
	}
	
	@Test(priority=3, dependsOnMethods = {"GetPlayListTest"})
	void UpdatePlayListTest() throws JsonParseException, JsonMappingException, IOException
	{
		logger.info("***** Update playlist *******");
		
		String playlist_id = ThreadSafeDataStore.getPlaylistId();
		
		Response response = SpotifyPlaylistEndPoints.UpdatePlayList(playlist_id);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);		
		
		logger.info("***** Update playlist done*******");
		
	}
	
	@Test(priority=4, dependsOnMethods = {"UpdatePlayListTest"})
	void DeletePlaylistTest()
	{
		logger.info("***** Delete playlist *******");
		
		String playlist_id = ThreadSafeDataStore.getPlaylistId();
		
		Response response = SpotifyPlaylistEndPoints.DeletePlayList(playlist_id);
		//response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);		
		
		logger.info("***** Delete playlist done*******");
		
	}
	
	@Test(dependsOnMethods = {"api.test.SpotifyCommonTest.getCurrentUserId"})
	void CreateMultiplePlaylists() throws JsonParseException, JsonMappingException, IOException
	{
		logger.info("***** Add multiple playlist for items one by one *******");
		
		String user_id = ThreadSafeDataStore.getUserId();
		
        String Playlistfile="src/test/resources/payloads/Playlist3.json";
        String responsefile ="src/test/resources/output/playlist3_op.json";

        List<Map<String, String>> payloadArray = ReadJsonPayload.readJsonAsListOfMaps(Playlistfile);

		for(Map<String, String> payload:payloadArray) 
		{
			Response response = SpotifyPlaylistEndPoints.AddPlayLists(user_id, payload);
			//response.then().log().all();
			
			PlayListPropStorageUtil.PlayListPropWritetoFile(responsefile,response);
		}
		
		logger.info("***** Add multiple playlist for items one by one done *******");
		
	}
	
	@Test(dependsOnMethods = {"CreateMultiplePlaylists"})
	void DeleteMultiplePlaylists() throws JsonProcessingException, IOException
	{
		logger.info("***** Delete Multiple playlist *******");
		
        String jsonfile ="src/test/resources/output/playlist3_op.json";
        
        JsonNode jsonArray = ReadJsonPayload.readJsonTree(jsonfile);
		
        for(JsonNode node : jsonArray)
        {
        	String playlist_id = node.get("id").asText();
        	Response response = SpotifyPlaylistEndPoints.DeletePlayList(playlist_id);
       		Assert.assertEquals(response.getStatusCode(), 200);		
        }
		
		logger.info("***** Delete Multiple playlist done*******");
	}
		
}
