package api.test;

import org.testng.annotations.*;
import org.testng.annotations.Test;
import org.testng.Assert;
import api.utilities.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.chrome.ChromeDriver;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.openqa.selenium.*;

import api.endpoints.SpotifyCommonEndPoints;
import api.payload.ThreadSafeDataStore;
import io.restassured.response.Response;

public class SpotifyCommonTest {
	
	public Logger logger;
		
	
	//Method to read properties routes
	static ResourceBundle getURL()
	{
		ResourceBundle routes = ResourceBundle.getBundle("routes");
		return routes;
	}
	
	
	@BeforeSuite
	void clearOutputFiles() {
		// Specify the paths of the output JSON files to clear
	    final String[] OUTPUT_FILES = { 
	        "src/test/resources/output/playlist3_op.json",
	        "src/test/resources/output/PlaylistItems1_op.json",
	        "src/test/resources/output/SearchItems1_res.json",
	        "src/test/resources/output/trackuri1.json",
	        "src/test/resources/output/trackuri2.json"
	    };

		for (String filePath : OUTPUT_FILES) {
			File file = new File(filePath);
			try {
				if (file.exists()) {
					// Clear contents (truncate)
					FileWriter writer = new FileWriter(file, false);
					writer.write(""); 
					writer.close();
					System.out.println("Cleared: " + filePath);
				} else {
					// Optionally create the file if it doesn't exist
					file.getParentFile().mkdirs(); // Ensure directory exists
					file.createNewFile();
					System.out.println("Created: " + filePath);
				}
			} catch (IOException e) {
				System.err.println("Failed to clear file: " + filePath);
				e.printStackTrace();
			}
		}
	}

	@Test(priority=1,enabled=false)
	
	void AuthCodebrowser() throws Exception
	{
		logger= LogManager.getLogger(this.getClass()); 
		
		System.setProperty("webdriver.chrome.driver","/Users/saravana-2405/GitHUB_Personal/SeleniumServerJar/chromedriver-mac-x64/chromedriver");
		WebDriver driver = new ChromeDriver();
		String url = SpotifyCommonEndPoints.AuthURL();
		System.out.println("url called :"+url);
		driver.get(url);
		driver.manage().window().maximize();
		
		Thread.sleep(2000);

		WebElement user_name = driver.findElement(By.id("login-username"));
		user_name.sendKeys(getURL().getString("user_name"));
		
		Thread.sleep(2000);

		WebElement continueBtn = driver.findElement(By.id("login-button"));
		continueBtn.click();
				
		Thread.sleep(10000);
		
		WebElement loginwithPwdLink = driver.findElement(By.cssSelector("button[data-encore-id=\"buttonTertiary\"]"));
		loginwithPwdLink.click();
		Thread.sleep(5000);
		
		WebElement password = driver.findElement(By.id("login-password"));
		password.sendKeys(getURL().getString("password"));
		System.out.println("Pwd typed");
		Thread.sleep(10000);

		
		WebElement loginBtn = driver.findElement(By.cssSelector("#login-button>span>span"));
		loginBtn.click();
		Thread.sleep(10000);
		
		System.out.println("Selenium action over");
		
		String authRedirectURL = driver.getCurrentUrl();
		System.out.println("URL :"+authRedirectURL);
		
		
        Map<String, String> queryParams = Seleniumutility.getQueryParams(authRedirectURL);
        ThreadSafeDataStore.setAuthCode(queryParams.get("code"));
        logger.info("AuthCode from browser :"+ThreadSafeDataStore.getAuthCode());
        
        //driver.quit();
		
	}
	
	
	@Test(enabled=false)
	void getAuthCode()
	{
		logger= LogManager.getLogger(this.getClass()); //used to print the classname before logger message 
		
		logger.info("************ Getting AuthCode *************");
		Response response = SpotifyCommonEndPoints.AuthCode();
		logger.info("Response url    :",response.asString());
		response.then().log().all();
		logger.info("********** Getting AuthCode done ***********");
	}
	
	@Test(priority=2, enabled=false)
	void getAccessToken() throws JsonParseException, JsonMappingException, IOException
	{
		logger= LogManager.getLogger(this.getClass()); //used to print the classname before logger message 
		logger.info("************ Getting AccessToken *************");
		
		String authCode = ThreadSafeDataStore.getAuthCode();
		
		Response response = SpotifyCommonEndPoints.AuthToken(authCode);

		Assert.assertEquals(response.getStatusCode(),200);
		
		AuthTokenManager.updateTokensFromResponse(response);

		TokenStorageUtil.writeTokensToFile("src/test/resources/token_store.json", response);
		
		
		logger.info("************ Getting AccessToken done *************");
		
	}
	
	@Test(priority=3)
	void getCurrentUserId() throws JsonParseException, JsonMappingException, IOException
	{
		logger= LogManager.getLogger(this.getClass()); //used to print the classname before logger message 
		logger.info("************ Getting CurrentUserId *************");
		
		TokenStorageUtil.loadTokensFromFile("src/test/resources/token_store.json");

		AuthTokenManager.refreshIfNeeded();
				
		Response response = SpotifyCommonEndPoints.GetCurrentUser();
		
		ThreadSafeDataStore.setUserId(response.body().jsonPath().getString("id"));

		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("Current User_ID :" + ThreadSafeDataStore.getUserId());
		logger.info("************ Getting CurrentUserID completed *************");
		

	}
	
	//Getting User's Playlists
	
	@Test(priority=4, dependsOnMethods= {"getCurrentUserId"})
	void getUsersPlaylist()
	{
		logger= LogManager.getLogger(this.getClass()); //used to print the classname before logger message 
		logger.info("************ Getting Users Playlists *************");
		
		String user_id=ThreadSafeDataStore.getUserId();
				
		Response response = SpotifyCommonEndPoints.GetUserPlaylist(user_id);
		
		Assert.assertEquals(response.getStatusCode(),200);
		Assert.assertEquals(response.jsonPath().getString("total"), "2");

		response.then().log().all();
		
		JSONObject res = new JSONObject(response.asString());
		
		HashMap <String, String> playlist = new HashMap <String, String>();
		
		for(int i=0;i<res.getJSONArray("items").length();i++) {
			playlist.put(res.getJSONArray("items").getJSONObject(i).getString("name"), res.getJSONArray("items").getJSONObject(i).getString("id"));
		}
		
		ThreadSafeDataStore.setPlaylistMap(playlist);
		
		List <String> names = response.jsonPath().getList("items.name");
		List <String> descriptions = response.jsonPath().getList("items.description");
		
		for(int i=0; i < names.size();i++) {
			String name = names.get(i);
			String description = descriptions.get(i);
			
			if(name.equals("PlayList Name2")) {
				Assert.assertEquals(description, "PlayList desc2");
			} else if (name.equals("PlayList Name1")) {
				Assert.assertEquals(description, "PlayList desc1");
			}
		}
		
		logger.info("************ Getting Users Playlists completed *************");
		
	}
	
	
}
