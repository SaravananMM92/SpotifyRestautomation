package api.payload;

import java.util.HashMap;

public class ThreadSafeDataStore {


	private static final ThreadLocal<String> authCode = new ThreadLocal<>();
    private static final ThreadLocal<String> accessToken = new ThreadLocal<>();
    private static final ThreadLocal<String> refreshToken = new ThreadLocal<>();
    private static final ThreadLocal<Long> expiresAt = new ThreadLocal<>();
    private static final ThreadLocal<String> scope = new ThreadLocal<>();
    private static final ThreadLocal<String> user_id = new ThreadLocal<>();
    private static final ThreadLocal<String> playlist_id = new ThreadLocal<>();
    private static final ThreadLocal<HashMap <String, String>> playlistMap = new ThreadLocal<>();


    // Auth Code
    public static void setAuthCode(String token) { 
    	authCode.set(token); }
    
    public static String getAuthCode() { 
    	return authCode.get(); }

    // Access Token
    public static void setAccessToken(String token) { 
    	accessToken.set(token); }
    
    public static String getAccessToken() { 
    	return accessToken.get(); }

    // Refresh Token
    public static void setRefreshToken(String token) { 
    	refreshToken.set(token); }
    
    public static String getRefreshToken() { 
    	return refreshToken.get(); }

    // Token Expiry
    public static void setExpiresAt(Long time) { 
    	expiresAt.set(time); }
    
    public static Long getExpiresAt() { 
    	return expiresAt.get(); }
    // scope
    public static void setScope(String scopes) {
    	scope.set(scopes);
    }
    
    public static String getScope() {
    	return scope.get();
    }
    
    // User ID
    public static void setUserId(String id) { 
    	user_id.set(id); }
    
    public static String getUserId() { 
    	return user_id.get(); }

    // Playlist ID
    public static void setPlaylistId(String id) { 
    	playlist_id.set(id); }
    
    public static String getPlaylistId() { 
    	return playlist_id.get(); }
    

    public static void setPlaylistMap(HashMap<String, String> map) {
    	playlistMap.set(map);
    }

    public static HashMap<String, String> getPlaylistMap() {
    	return playlistMap.get();
    }

    // Clear method (use after test method)
    public static void clear() {
        accessToken.remove();
        refreshToken.remove();
        expiresAt.remove();
        user_id.remove();
        playlist_id.remove();
    }
}
