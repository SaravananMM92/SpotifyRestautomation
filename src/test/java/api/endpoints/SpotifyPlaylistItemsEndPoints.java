package api.endpoints;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.util.Map;

import api.payload.ThreadSafeDataStore;
import io.restassured.response.Response;


public class SpotifyPlaylistItemsEndPoints {
	
	public static Response SearchItems(Map<String, String> payload) throws IOException
	{
		String searchItems_url = SpotifyCommonEndPoints.getURL().getString("searchItems_url");

		
		Response response = given()
								.header("Authorization","Bearer "+ThreadSafeDataStore.getAccessToken())
								.header("Content-Type","application/json")
								.queryParams(payload)
							
							.when()
								.get(searchItems_url);
		return response;
	}
	
	public static Response UpdatePlaylistItems(String uris, String playlistid, Map<String, String> payload)
	{
		String updatePlaylistitemuri = SpotifyCommonEndPoints.getURL().getString("updatePlaylistitem_url");
		String updatePlaylistitem_url = updatePlaylistitemuri.replace("{playlist_id}", playlistid);
		
		Response response = given()
								.header("Authorization", "Bearer "+ThreadSafeDataStore.getAccessToken())
								.header("Content-Type","application/json")
								.body(payload)
								.queryParam("uris", uris)
							
							.when()
								.put(updatePlaylistitem_url);
		return response;
	}
	
	public static Response AddPlaylistItems(String playlistid, Map<String, Object> payload)
	{
		String addPlaylistitemuri = SpotifyCommonEndPoints.getURL().getString("addPlaylistitem_url");
		String addPlaylistitem_url = addPlaylistitemuri.replace("{playlist_id}", playlistid);
		
		Response response = given()
								.header("Authorization", "Bearer "+ThreadSafeDataStore.getAccessToken())
								.header("Content-Type","application/json")
								.body(payload)	
											
							.when()
								.post(addPlaylistitem_url);
		return response;
	}
	
}
