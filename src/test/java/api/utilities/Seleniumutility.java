package api.utilities;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Seleniumutility {

    public static Map<String, String> getQueryParams(String url) throws Exception 
    {
        @SuppressWarnings("deprecation")
		URL u = new URL(url);
        String query = u.getQuery();
        String[] pairs = query.split("&");
        
        Map<String, String> map = new HashMap<>();
        
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        System.out.println(map);
        return map;
    }
}
