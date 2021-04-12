package Functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FunctionFour extends Function {

    private static class Customer {
        private String name;
        private String email;

        public Customer(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    private static class Promotion {
        private final String title;
        private final String quantity;
        private final String type;
        private final String discount;

        public Promotion(String title, String quantity, String type, String discount) {
            this.title = title;
            this.quantity = quantity;
            this.type = type;
            this.discount = discount;
        }

        @Override
        public String toString() {
//            if (discount != null) {
//                return "{\n" +
//                        "\t\t\t\"title\":\"" + title + "\",\n" +
//                        "\t\t\t\"quantity\":\"" + quantity + "\",\n" +
//                        "\t\t\t\"type\":\"" + type + "\",\n" +
//                        "\t\t\t\"discount\":\"" + discount + "\"\n" +
//                        "\t\t}";
//            } else {
//                return "{\n" +
//                        "\t\t\t\"title\":\"" + title + "\",\n" +
//                        "\t\t\t\"quantity\":\"" + quantity + "\",\n" +
//                        "\t\t\t\"type\":\"" + type + "\"\n" +
//                        "\t\t}";
//            }
            if (discount != null) {
                return "{\n" +
                        "\t\"title\":\"" + title + "\",\n" +
                        "\t\"quantity\":\"" + quantity + "\",\n" +
                        "\t\"type\":\"" + type + "\",\n" +
                        "\t\"discount\":\"" + discount + "\"\n" +
                        "}";
            } else {
                return "{\n" +
                        "\t\"title\":\"" + title + "\",\n" +
                        "\t\"quantity\":\"" + quantity + "\",\n" +
                        "\t\"type\":\"" + type + "\"\n" +
                        "}";
            }
        }
    }

    static class MyJSONComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            String string1 = (String) o1.get("title");
            String string2 = (String) o2.get("title");
            return string1.compareTo(string2);
        }
    }

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

        String JSONString = result.toString();
        System.out.println(JSONString);

        String name = "";
        String email = "";

        JSONParser parser = new JSONParser();
        ArrayList<JSONObject> list = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(JSONString);
            name = jsonObject.get("name").toString();
            email = jsonObject.get("email").toString();

            JSONArray array = (JSONArray) jsonObject.get("promotions");

            for (Object value : array) {
                list.add((JSONObject) value);
            }
            list.sort(new MyJSONComparator());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(list);
        Customer customer = new Customer(name, email);

        String title = "";
        String quantity = "";
        String type = "";
        String discount = "";
        Promotion promotion;
        StringBuilder toBePrinted = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            JSONObject object = list.get(i);
            try {
                title = object.get("title").toString();
                quantity = object.get("quantity").toString();
                type = object.get("type").toString();
                discount = object.get("discount").toString();
                promotion = new Promotion(title, quantity, type, discount);
            } catch (NullPointerException e) {
                promotion = new Promotion(title, quantity, type, null);
            }
            toBePrinted.append(promotion);
            if (i != list.size() - 1) {
                toBePrinted.append(", \n");
            }
        }
        System.out.println(toBePrinted.toString());
    }
}
