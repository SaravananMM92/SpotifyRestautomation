package api.endpoints;

import static io.restassured.RestAssured.*;
//import java.util.Base64;
//import java.util.ResourceBundle;
//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.payload.PlayListPayload;
import api.payload.ThreadSafeDataStore;
import io.restassured.response.Response;

public class SpotifyPlaylistEndPoints {
	
	public static Response AddPlayList(String user_id, Map<String, String> payload) throws JsonParseException, JsonMappingException, IOException
	{
		String addPlaylisturi = SpotifyCommonEndPoints.getURL().getString("addPlaylist_url");
		String addPlaylist_url = addPlaylisturi.replace("{user_id}", user_id);
  
//        File file = new File("src/test/resources/payloads/playlist1.json");
//
//        ObjectMapper mapper = new ObjectMapper();
//        PlayListPayload payload = mapper.readValue(file, PlayListPayload.class);

		System.out.println("Add Playlist Begins...");
		Response response = given()
								.header("Authorization","Bearer "+ThreadSafeDataStore.getAccessToken())
								.header("Content-Type","application/json")
								.body(payload)
								.log().headers()
								.log().body()
												
							.when()
								.post(addPlaylist_url);
				
		return response;
	}
	

	public static Response GetPlaylists(String playlistId)
	{
		String getPlaylisturi = SpotifyCommonEndPoints.getURL().getString("getPlaylist_url");
		String getPlaylist_url = getPlaylisturi.replace("{playlist_id}", playlistId);
		
		System.out.println("Get Playlist Begins...");

		Response response = given()
								.header("Authorization","Bearer "+ThreadSafeDataStore.getAccessToken())
							
							.when()
								.get(getPlaylist_url);
				
		return response;
	}
	 

	public static Response UpdatePlayList(String playlist_id) throws JsonParseException, JsonMappingException, IOException
	{
		String updatePlaylisturi = SpotifyCommonEndPoints.getURL().getString("updatePlaylist_url");
		String updatePlaylist_url = updatePlaylisturi.replace("{playlist_id}", playlist_id);
  
        File file = new File("src/test/resources/payloads/playlist2.json");

        ObjectMapper mapper = new ObjectMapper();
        PlayListPayload payload = mapper.readValue(file, PlayListPayload.class);

		System.out.println("Update Playlist Begins...");

		Response response = given()
								.header("Authorization","Bearer "+ThreadSafeDataStore.getAccessToken())
								.header("Content-Type","application/json")
								.body(payload)
								.log().headers()
								.log().body()
												
							.when()
								.put(updatePlaylist_url);
				
		return response;
	}
	
	public static Response DeletePlayList(String playlist_id)
	{
		String deletePlaylisturi = SpotifyCommonEndPoints.getURL().getString("deletePlaylist_url");
		String deletePlaylist_url = deletePlaylisturi.replace("{playlist_id}", playlist_id);
		
		Response response = given()
								.header("Authorization","Bearer "+ThreadSafeDataStore.getAccessToken())
							
								.when()
									.delete(deletePlaylist_url);
		return response;
	}
	
	
	public static Response AddPlayLists(String user_id, Map<String, String> payload)
	{
		String addPlaylisturi = SpotifyCommonEndPoints.getURL().getString("addPlaylist_url");
		String addPlaylist_url = addPlaylisturi.replace("{user_id}", user_id);
		
		Response response = given()
				.header("Authorization","Bearer "+ThreadSafeDataStore.getAccessToken())
				.header("Content-Type","application/json")
				.body(payload)

								
			.when()
				.post(addPlaylist_url);

		return response;
	}

}
