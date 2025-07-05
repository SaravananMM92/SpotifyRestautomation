package api.endpoints;

import static io.restassured.RestAssured.*;
import java.util.Base64;
import java.util.ResourceBundle;
//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

import api.payload.ThreadSafeDataStore;
import io.restassured.response.Response;

public class SpotifyCommonEndPoints {
	
	public static String AuthCodeuri;
	
	//Method to read properties routes
	static ResourceBundle getURL()
	{
		ResourceBundle routes = ResourceBundle.getBundle("routes");
		return routes;
	}
	
	public static String AuthURL()
	{

		String client_id = getURL().getString("client_id");
		String redirect_uri = getURL().getString("redirect_uri");
		String scope = getURL().getString("scope");
		String getAuthCode_url = getURL().getString("getAuthCode_url");
		AuthCodeuri = getAuthCode_url+"?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri+"&scope="+scope;
		
		//System.out.println("Authcodeuri :"+AuthCodeuri);
		
		return AuthCodeuri;
	}
	
	public static Response AuthCode()
	{
		
		String client_id = getURL().getString("client_id");
		String redirect_uri = getURL().getString("redirect_uri");
		String scope = getURL().getString("scope");
		String getAuthCode_url = getURL().getString("getAuthCode_url");
		AuthCodeuri = getAuthCode_url+"?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri+"&scope="+scope;
		
		System.out.println("Authcodeuri :"+AuthCodeuri);
		
//https://accounts.spotify.com/authorize?response_type=code&client_id=1e43efa9abf14ca594ccc87daa8d5bc3&redirect_uri=https://demo.admanagerplus.com&scope=playlist-read-private playlist-modify-private
				
				
		Response response=given()
							.queryParam("response_type", "code")
							.queryParam("client_id", client_id)
							.queryParam("redirect_uri", redirect_uri)
							.queryParam("scope", scope)
							.log().uri()
		
						.when()
							.get(getAuthCode_url);
		
		return response;
	}
	
	public static Response AuthToken(String code)
	{

	    String client_id = getURL().getString("client_id");
	    String client_secret = getURL().getString("client_secret");
	    String redirect_uri = getURL().getString("redirect_uri");
	    String encoded = Base64.getEncoder().encodeToString((client_id+":"+client_secret).getBytes());
	    String AuthHeader = "Basic " + encoded;
	    String getAPIToken_url = getURL().getString("getAPIToken_url");
	    

	    Response response = given()
	    						.headers("Content-Type", "application/x-www-form-urlencoded",
	    							"Authorization", AuthHeader)
	    						.formParam("code", code)
	    						.formParam("grant_type", "authorization_code")
	    						.formParam("redirect_uri",redirect_uri)
	    						
	    						
	    					.when()
	    						.post(getAPIToken_url); // Replace with your method if needed
	    return response;
	}
	
	
	public static Response CallRefreshToken()
	{
		
		String client_id = getURL().getString("client_id");
		String client_secret = getURL().getString("client_secret");
	    String encoded = Base64.getEncoder().encodeToString((client_id+":"+client_secret).getBytes());
	    String AuthHeader = "Basic " + encoded;
		String getRefreshToekn_url = getURL().getString("getRefreshToekn_url");
				
		Response response = given()
								.headers("Authorization", AuthHeader,
										"Content-Type", "application/x-www-form-urlencoded")
								.formParam("refresh_token", ThreadSafeDataStore.getRefreshToken())
								.formParam("grant_type", "refresh_token")
								.formParam("client_id", client_id)
						
								
							.when()
								.post(getRefreshToekn_url);
		
		return response;
	}
	
	public static Response GetCurrentUser()
	{
		String getCurrentUser_url = getURL().getString("getCurrentUser_url");
		
		Response response = given()
								.header("Authorization", "Bearer "+ThreadSafeDataStore.getAccessToken())
							
							.when()
								.get(getCurrentUser_url);
		return response;
	}

	public static Response GetUserPlaylist(String user_id)
	{
		String getUserPlaylist_url = getURL().getString("getUserPlaylist_url");
		String getUserPlaylist_finalurl = getUserPlaylist_url.replace("{user_id}", user_id);
		System.out.println(getUserPlaylist_finalurl);

		Response response = given()
								.header("Authorization", "Bearer "+ThreadSafeDataStore.getAccessToken())
			
							.when()
								.get(getUserPlaylist_finalurl);
		return response;
		
	}
	
	
}
