package api.utilities;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import api.endpoints.SpotifyCommonEndPoints;
import api.payload.ThreadSafeDataStore;
import io.restassured.response.Response;

public class AuthTokenManager {

    public static void updateTokensFromResponse(Response response) 
    {
    	System.out.println("Updating the Authtoken details to ThreadSafeDataSource");
        String accessToken = response.jsonPath().getString("access_token");
        String refreshToken = response.jsonPath().getString("refresh_token");
        int expiresIn = response.jsonPath().getInt("expires_in");
        long expiresAt = (System.currentTimeMillis() / 1000) + expiresIn;
        String scopes = response.jsonPath().getString("scope");

        ThreadSafeDataStore.setAccessToken(accessToken);
        ThreadSafeDataStore.setRefreshToken(refreshToken);
        ThreadSafeDataStore.setExpiresAt(expiresAt);
        ThreadSafeDataStore.setScope(scopes);
    }

    public static boolean isTokenExpired() 
    {
        Long expiresAt = ThreadSafeDataStore.getExpiresAt();
        return expiresAt == null || System.currentTimeMillis() / 1000 >= expiresAt;
    }

    public static void refreshIfNeeded() throws JsonParseException, JsonMappingException, IOException {
        if (isTokenExpired()) 
        {
        	System.out.println("Refresh Token started..");
            Response response = SpotifyCommonEndPoints.CallRefreshToken();
            updateTokensFromResponse(response);
            TokenStorageUtil.writeTokensToFile("src/test/resources/token_store.json",response);
        }
    }
}

