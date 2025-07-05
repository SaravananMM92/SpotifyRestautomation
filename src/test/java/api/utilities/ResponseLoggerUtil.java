package api.utilities;

import io.restassured.response.Response;
import java.io.FileWriter;
import java.io.IOException;

public class ResponseLoggerUtil {

    public static void writeResponseToFile(Response response, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(response.getBody().asPrettyString());
            System.out.println("Response written to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
