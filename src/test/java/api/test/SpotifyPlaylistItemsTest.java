package api.test;

import java.io.IOException;
import java.util.HashMap;
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
import api.endpoints.SpotifyPlaylistItemsEndPoints;
import api.payload.ThreadSafeDataStore;
import api.utilities.PlayListItemsStorageUtil;
import api.utilities.PlayListPropStorageUtil;
import api.utilities.ReadJsonPayload;
import io.restassured.response.Response;

public class SpotifyPlaylistItemsTest {
	
	public Logger logger= LogManager.getLogger(this.getClass()); 

	@Test(dependsOnMethods={"api.test.SpotifyCommonTest.getCurrentUserId"})
	void AddPlaylistforItems() throws JsonParseException, JsonMappingException, IOException
	{
		logger.info("***** Add multiple playlist for items one by one *******");
		
		String user_id = ThreadSafeDataStore.getUserId();
		
        String Playlistfile="src/test/resources/payloads/playlistItems1.json";
        String responsefile ="src/test/resources/output/PlaylistItems1_op.json";

        List<Map<String, String>> payloadArray = ReadJsonPayload.readJsonAsListOfMaps(Playlistfile);

		for(Map<String, String> payload:payloadArray) 
		{
			System.out.println("Create playlist");
			Response response = SpotifyPlaylistEndPoints.AddPlayLists(user_id, payload);
			//response.then().log().all();
			
			PlayListPropStorageUtil.PlayListPropWritetoFile(responsefile,response);
		}
		
		logger.info("***** Add multiple playlist for items one by one done *******");
		
	}
	
	@Test( dependsOnMethods = {"AddPlaylistforItems"})
	void SearchItems() throws IOException
	{
		logger.info("***** Search Items *******");
				
		String jsonPath = "src/test/resources/payloads/SearchItems1.json";
//		String responsePath = "src/test/resources/output/SearchItems1_res.json";
		String filePath = "src/test/resources/output/trackuri1.json";
		
		Map<String, String> payload = ReadJsonPayload.readJsonAsMap(jsonPath);
		
		Response response = SpotifyPlaylistItemsEndPoints.SearchItems(payload);
//		ResponseLoggerUtil.writeResponseToFile(response, responsePath);
		PlayListItemsStorageUtil.StoreTrackURIs(filePath,response);
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("***** Search Items  *******");
	}
	

	@Test(priority=1,dependsOnMethods= {"SearchItems"})
	void AddlayListItemTest() throws IOException
	{
		logger.info("***** Update Playlist Items *******");
		
		//Read playlist_id 
		String playlistopresponse = "src/test/resources/output/PlaylistItems1_op.json";
		JsonNode jsonIDArray = ReadJsonPayload.readJsonTree(playlistopresponse);
		
		//uris formation using stringArray
		String trackFile = "src/test/resources/output/trackuri1.json";
		List<String> uris = ReadJsonPayload.trackURIsfromJsonAsList(trackFile);
		
		System.out.println("URIS "+uris);
		
		//construct payload 
		Map <String, Object> payload = new HashMap <String, Object> ();
		payload.put("uris", uris);
		payload.put("position", 0);
		
		for(JsonNode node: jsonIDArray)
		{
			System.out.println("Add items");
			String playlist_id = node.get("id").asText();
			Response response = SpotifyPlaylistItemsEndPoints.AddPlaylistItems(playlist_id, payload);
			Assert.assertEquals(response.getStatusCode(), 201);
			Assert.assertNotNull(response.jsonPath().getString("snapshot_id"),"snapshot_id is not present in the response");
		}
		
		logger.info("***** Update Playlist Items done *******");

	}
	

	@Test(dependsOnMethods = {"api.test.SpotifyCommonTest.getCurrentUserId"})
	void SearchItems1() throws IOException
	{
		logger.info("***** Search Items *******");
				
		String jsonPath = "src/test/resources/payloads/SearchItems2.json";
//		String responsePath = "src/test/resources/output/SearchItems1_res.json";
		String filePath = "src/test/resources/output/trackuri2.json";
		
		Map<String, String> payload = ReadJsonPayload.readJsonAsMap(jsonPath);
		
		Response response = SpotifyPlaylistItemsEndPoints.SearchItems(payload);
//		ResponseLoggerUtil.writeResponseToFile(response, responsePath);
		PlayListItemsStorageUtil.StoreTrackURIs(filePath,response);
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("***** Search Items  *******");
	}
	
	
	
	@Test(priority=2,dependsOnMethods= {"SearchItems1"})
	void UpdatePlayListItemTest() throws IOException
	{
		logger.info("***** Update Playlist Items *******");
		
		//ready playlistitem payload
		String playlistitempayload = "src/test/resources/payloads/UpdatePlaylistItemsPayload1.json";
		Map<String, String> payload=ReadJsonPayload.readJsonAsMap(playlistitempayload);
		System.out.println("Items payload "+payload);
		
		//Read playlist_id
		String playlistopresponse = "src/test/resources/output/PlaylistItems1_op.json";
		JsonNode jsonIDArray = ReadJsonPayload.readJsonTree(playlistopresponse);
		
		//uris formation using stringArray
		String trackFile = "src/test/resources/output/trackuri2.json";
		String uris = ReadJsonPayload.trackURIsfromJsonAsString(trackFile);
		System.out.println("URIS "+uris);
		
		for(JsonNode node: jsonIDArray)
		{
			System.out.println("Update items");
			String playlist_id = node.get("id").asText();
			System.out.println("playlist_id "+playlist_id);
			Response response = SpotifyPlaylistItemsEndPoints.UpdatePlaylistItems(uris, playlist_id, payload);
			Assert.assertEquals(response.getStatusCode(), 200);
			Assert.assertNotNull(response.jsonPath().getString("snapshot_id"),"snapshot_id is not present in the response");
		}
		
		logger.info("***** Update Playlist Items done *******");

	}
	
	
	
	@Test(priority=999,dependsOnMethods= {"api.test.SpotifyCommonTest.getCurrentUserId"})
	void DeleteMultiplePlaylists() throws JsonProcessingException, IOException
	{
		logger.info("***** Delete Multiple playlist *******");
		
        String jsonfile ="src/test/resources/output/PlaylistItems1_op.json";
        
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
