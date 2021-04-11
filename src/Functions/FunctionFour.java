package Functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FunctionFour extends Function {
    @Override
    public void execute() throws IOException {
        StringBuilder result = new StringBuilder();
        String urlToVisit = "https://dev.beepbeep.tech/v1/sample_customer";
        URL url = new URL(urlToVisit);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("accept", "application/json");
        try (var reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        System.out.println(result.toString());
    }
}
