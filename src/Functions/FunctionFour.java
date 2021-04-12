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

    // customer class to store name and email
    private static class Customer {
        private final String name;
        private final String email;
        private static int id = 0;

        public Customer(String name, String email) {
            this.name = name;
            this.email = email;
            id++;
        }

        @Override
        public String toString() {
            return "Customer " + id + ":\n\tName: " + name + "\n\tEmail: " + email + "\n\tPromotions:";
        }
    }

    // promotion class to store details related to promotions
    private static class Promotion {
        private final String title;
        private final String quantity;
        private final String type;
        private final String discount;
        private static int id = 0;

        public Promotion(String title, String quantity, String type, String discount) {
            this.title = title;
            this.quantity = quantity;
            this.type = type;
            this.discount = discount;
            id++;
        }

        @Override
        public String toString() {
            if (discount != null) { // assuming only discount can be present/not present
                return "\t\tPromotion " + id + ": " +
                        "\n\t\t\tTitle: " + title +
                        "\n\t\t\tQuantity: " + quantity +
                        "\n\t\t\tType: " + type +
                        "\n\t\t\tDiscount: " + discount;
            } else {
                return "\t\tPromotion " + id + ": " +
                        "\n\t\t\tTitle: " + title +
                        "\n\t\t\tQuantity: " + quantity +
                        "\n\t\t\tType: " + type;
            }
        }
    }

    // comparator to compare JSON for the descending order of title
    private static class MyJSONComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            String string1 = (String) o1.get("title");
            String string2 = (String) o2.get("title");
            return string2.compareTo(string1);
        }
    }

    // get promotions in an arraylist
    private ArrayList<JSONObject> getPromotions(String JSONString) {
        String name;
        String email;

        JSONParser parser = new JSONParser();
        ArrayList<JSONObject> list = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(JSONString);
            name = jsonObject.get("name").toString();
            email = jsonObject.get("email").toString();
            Customer customer = new Customer(name, email);
            System.out.println(customer);
            JSONArray array = (JSONArray) jsonObject.get("promotions");

            for (Object value : array) {
                list.add((JSONObject) value);
            }
            list.sort(new MyJSONComparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // print out the promotions
    private void printPromotions(ArrayList<JSONObject> promotions) {
        String title = "";
        String quantity = "";
        String type = "";
        String discount;
        Promotion promotion;
        StringBuilder toBePrinted = new StringBuilder();

        for (int i = 0; i < promotions.size(); i++) {
            JSONObject object = promotions.get(i);
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
            if (i != promotions.size() - 1) {
                toBePrinted.append("\n");
            }
        }
        System.out.println(toBePrinted.toString());
    }

    @Override
    public void execute() throws IOException {
        String urlToVisit = "https://dev.beepbeep.tech/v1/sample_customer";
        URL url = new URL(urlToVisit);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("accept", "application/json");

        StringBuilder result = new StringBuilder();
        try (var reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null;) {
                result.append(line);
            }
        }

        String JSONString = result.toString();
        ArrayList<JSONObject> promotions = getPromotions(JSONString);
        printPromotions(promotions);
    }
}
