package Functions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.DAYS;

public class FunctionFive extends Function {

    HashMap<String, String> orderIDAndSellerID = new HashMap<>();
    HashMap<String, PairOfSumAndReviews> sellerIDAndReviewScore = new HashMap<>();
    HashMap<String, Double> sellerIDAndAverageReviewScore = new HashMap<>();
    HashMap<String, Double> sortedSellerIDAndAverageReviewScore = new HashMap<>();

    HashMap<String, String> orderIDAndProductID = new HashMap<>();
    HashMap<String, Double> orderIDAndPaymentValue = new HashMap<>();
    HashMap<String, Double> productIDAndPaymentValue = new HashMap<>();
    HashMap<String, String> productIDAndName = new HashMap<>();
    HashMap<String, Double> productNameAndRevenue = new HashMap<>();
    HashMap<String, String> categoryNameAndEnglishName = new HashMap<>();
    HashMap<String, Double> englishNameAndRevenue = new HashMap<>();
    HashMap<String, Double> sortedEnglishNameAndRevenue = new HashMap<>();

    HashMap<String, Double> sellerIDAndPaymentValue = new HashMap<>();
    HashMap<String, LocalDate> orderIDAndOrderApprovedAt = new HashMap<>();
    HashMap<String, PairOfFirstAndLastDate> sellerIDAndDates = new HashMap<>();
    HashMap<String, Integer> sellerIDAndDays = new HashMap<>();
    HashMap<String, Double> sortedSellerIDAndPaymentValue = new HashMap<>();

    HashMap<String, String> sellerIDAndSellerState = new HashMap<>();
    HashMap<String, Double> sellerStateAndPaymentValue = new HashMap<>();
    HashMap<String, Integer> sellerStateAndMonths = new HashMap<>();

    String ORDER_ITEMS_PATH = "data/order_items.csv";
    String ORDER_PAYMENTS_PATH = "data/order_payments.csv";
    String ORDER_REVIEWS_PATH = "data/order_reviews.csv";
    String ORDERS_PATH = "data/orders.csv";
    String PRODUCT_CATEGORY_NAME_TRANSLATION_PATH = "data/product_category_name_translation.csv";
    String PRODUCTS_PATH = "data/products.csv";
    String SELLERS_PATH = "data/sellers.csv";

    @Override
    public void execute() throws IOException {
        topTenSellersWithHighestReviewScore();
        topTenProductWithHighestSales();
        dailyAverageRevenueByTopTenSellers();
        monthlyRevenueInEachState();
        respond();
    }

    private static class PairOfSumAndReviews {
        private final int sum;
        private final int numberOfReviews;

        public PairOfSumAndReviews(int sum, int numberOfReviews) {
            this.sum = sum;
            this.numberOfReviews = numberOfReviews;
        }

        public int getSum() {
            return this.sum;
        }

        public int getNumberOfReviews() {
            return this.numberOfReviews;
        }
    }

    private static class PairOfFirstAndLastDate {
        private final LocalDate firstDate;
        private final LocalDate lastDate;

        public PairOfFirstAndLastDate(LocalDate firstDate, LocalDate lastDate) {
            this.firstDate = firstDate;
            this.lastDate = lastDate;
        }

        public LocalDate getFirstDate() {
            return this.firstDate;
        }

        public LocalDate getLastDate() {
            return this.lastDate;
        }
    }

    // sort the hashmap based on values, then keys if values are same
    private static HashMap<String, Double> sortByValue(HashMap<String, Double> unsortedMap) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    private void topTenSellersWithHighestReviewScore() throws IOException {
        File file = new File(ORDER_ITEMS_PATH);
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        int count = 0;
        for (String line : lines) {
            if (count > 0) {
                String[] array = line.split(",");
                String orderID = array[0].replace("\"", "");
                String sellerID = array[3].replace("\"", "");
                orderIDAndSellerID.put(orderID, sellerID);
            }
            count++;
        }

        File file1 = new File(ORDER_REVIEWS_PATH);
        List<String> lines1 = Files.readAllLines(file1.toPath(), StandardCharsets.UTF_8);
        for (String line : lines1) {
            try {
                String[] array = line.split(",");
                String orderID = array[1].replace("\"", "");
                String sellerID = "";
                if (orderID.length() == 32) {
                    sellerID = orderIDAndSellerID.get(orderID);
                    if (sellerID == null) { // for those that didn't get a review
                        sellerID = "";
                    }
                }
                int reviewScore = Integer.parseInt(array[2]);

                if (!sellerIDAndReviewScore.containsKey(sellerID)) {
                    sellerIDAndReviewScore.put(sellerID, new PairOfSumAndReviews(reviewScore, 1));
                } else {
                    PairOfSumAndReviews pairOfSumAndReviews = sellerIDAndReviewScore.get(sellerID);
                    int currentSum = pairOfSumAndReviews.getSum();
                    int currentNumberOfReviews = pairOfSumAndReviews.getNumberOfReviews();
                    currentSum += reviewScore;
                    currentNumberOfReviews++;
                    sellerIDAndReviewScore.put(sellerID, new PairOfSumAndReviews(currentSum, currentNumberOfReviews));
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                continue;
            }
        }

        Iterator<Map.Entry<String, PairOfSumAndReviews>> it = sellerIDAndReviewScore.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, PairOfSumAndReviews> pair = it.next();
            String sellerID = pair.getKey();
            int totalSum = pair.getValue().getSum();
            int totalNumberOfReviews = pair.getValue().getNumberOfReviews();
            double average = (double) totalSum / totalNumberOfReviews;
            sellerIDAndAverageReviewScore.put(sellerID, average);
        }

        sortedSellerIDAndAverageReviewScore = sortByValue(sellerIDAndAverageReviewScore);
        Iterator<Map.Entry<String, Double>> iterator = sortedSellerIDAndAverageReviewScore.entrySet().iterator();

        System.out.println("Top 10 sellers with high review scores:");
        count = 0;
        while (iterator.hasNext() && count < 10) {
            Map.Entry<String, Double> pair = iterator.next();
            String sellerID = pair.getKey();
            double averageReviewScore = pair.getValue();
            count++;
            System.out.println(count + ". Seller ID: " + sellerID + " Average Review Score: " + averageReviewScore);
        }
    }

    private void topTenProductWithHighestSales() throws IOException {
        File file = new File(ORDER_ITEMS_PATH);
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        int count = 0;
        for (String line : lines) {
            if (count > 0) {
                String[] array = line.split(",");
                String orderID = array[0].replace("\"", "");
                String productID = array[2].replace("\"", "");
                orderIDAndProductID.put(orderID, productID);
            }
            count++;
        }

        File file1 = new File(ORDER_PAYMENTS_PATH);
        List<String> lines1 = Files.readAllLines(file1.toPath(), StandardCharsets.UTF_8);
        count = 0;
        for (String line : lines1) {
            if (count > 0) {
                String[] array = line.split(",");
                String orderID = array[0].replace("\"", "");
                double paymentValue = Double.parseDouble(array[4]);
                orderIDAndPaymentValue.put(orderID, paymentValue);
            }
            count++;
        }

        Iterator<Map.Entry<String, String>> it1 = orderIDAndProductID.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry<String, String> pair1 = it1.next();
            String orderID = pair1.getKey();
            String productID = pair1.getValue();
            if (orderIDAndPaymentValue.get(orderID) != null) { // for those that actually paid
                double paymentValue = orderIDAndPaymentValue.get(orderID);
                if (!productIDAndPaymentValue.containsKey(productID)) {
                    productIDAndPaymentValue.put(productID, paymentValue);
                } else {
                    double currentRevenue = productIDAndPaymentValue.get(productID);
                    currentRevenue += paymentValue;
                    productIDAndPaymentValue.put(productID, currentRevenue);
                }
            }
        }

        File file2 = new File(PRODUCTS_PATH);
        List<String> lines2 = Files.readAllLines(file2.toPath(), StandardCharsets.UTF_8);
        for (String line : lines2) {
            try {
                String[] array = line.split(",");
                String productID = array[0].replace("\"", "");
                String productName = array[1];
                productIDAndName.put(productID, productName);
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }

        Iterator<Map.Entry<String, Double>> it2 = productIDAndPaymentValue.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<String, Double> pair1 = it2.next();
            String productID = pair1.getKey();
            double paymentValue = pair1.getValue();
            String productName = productIDAndName.get(productID);
            if (!productNameAndRevenue.containsKey(productName)) {
                productNameAndRevenue.put(productName, paymentValue);
            } else {
                double currentRevenue = productNameAndRevenue.get(productName);
                currentRevenue += paymentValue;
                productNameAndRevenue.put(productName, currentRevenue);
            }
        }

        File file3 = new File(PRODUCT_CATEGORY_NAME_TRANSLATION_PATH);
        List<String> lines3 = Files.readAllLines(file3.toPath(), StandardCharsets.UTF_8);
        for (String line : lines3) {
            String[] array = line.split(",");
            String categoryName = array[0];
            String englishName = array[1];
            categoryNameAndEnglishName.put(categoryName, englishName);
        }

        Iterator<Map.Entry<String, Double>> it3 = productNameAndRevenue.entrySet().iterator();

        while (it3.hasNext()) {
            Map.Entry<String, Double> pair1 = it3.next();
            String productName = pair1.getKey();
            double revenue = pair1.getValue();
            String englishName = categoryNameAndEnglishName.get(productName);
            if (!englishNameAndRevenue.containsKey(englishName)) {
                englishNameAndRevenue.put(englishName, revenue);
            } else {
                double currentRevenue = englishNameAndRevenue.get(englishName);
                currentRevenue += revenue;
                englishNameAndRevenue.put(englishName, currentRevenue);
            }
        }

        sortedEnglishNameAndRevenue = sortByValue(englishNameAndRevenue);
        Iterator<Map.Entry<String, Double>> iterator = sortedEnglishNameAndRevenue.entrySet().iterator();

        System.out.println("\nTop 10 product categories with high revenues:");
        count = 0;
        while (iterator.hasNext() && count < 10) {
            Map.Entry<String, Double> pair = iterator.next();
            String englishName = pair.getKey();
            double revenue = pair.getValue();
            count++;
            System.out.println(count + ". English name of product category: " + englishName +
                    " Revenue earned: " + String.format("%.2f", revenue));
        }
    }

    private void dailyAverageRevenueByTopTenSellers() throws IOException {
        Iterator<Map.Entry<String, String>> it1 = orderIDAndSellerID.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry<String, String> pair1 = it1.next();
            String orderID = pair1.getKey();
            String sellerID = pair1.getValue();
            if (orderIDAndPaymentValue.get(orderID) != null) {
                double paymentValue = orderIDAndPaymentValue.get(orderID);
                if (!sellerIDAndPaymentValue.containsKey(sellerID)) {
                    sellerIDAndPaymentValue.put(sellerID, paymentValue);
                } else {
                    double currentRevenue = sellerIDAndPaymentValue.get(sellerID);
                    currentRevenue += paymentValue;
                    sellerIDAndPaymentValue.put(sellerID, currentRevenue);
                }
            }
        }

        File file = new File(ORDERS_PATH);
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        int count = 0;
        for (String line : lines) {
            if (count > 0) {
                String[] array = line.split(",");
                String orderID = array[0].replace("\"", "");
                String[] splitDate = array[4].split(" ");
                if (!splitDate[0].isEmpty()) { // check for cancelled orders
                    LocalDate orderApprovedAt = LocalDate.parse(splitDate[0]);
                    orderIDAndOrderApprovedAt.put(orderID, orderApprovedAt);
                }
            }
            count++;
        }

        Iterator<Map.Entry<String, String>> it2 = orderIDAndSellerID.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<String, String> pair1 = it2.next();
            String orderID = pair1.getKey();
            String sellerID = pair1.getValue();
            if (orderIDAndOrderApprovedAt.get(orderID) != null) { // order ID not being approved
                LocalDate date = orderIDAndOrderApprovedAt.get(orderID);
                if (!sellerIDAndDates.containsKey(sellerID)) {
                    sellerIDAndDates.put(sellerID, new PairOfFirstAndLastDate(date, null));
                } else {
                    PairOfFirstAndLastDate pairOfFirstAndLastDate = sellerIDAndDates.get(sellerID);
                    LocalDate firstDate = pairOfFirstAndLastDate.getFirstDate();
                    LocalDate lastDate = pairOfFirstAndLastDate.getLastDate();
                    if (lastDate == null) {
                        if (firstDate.isBefore(date)) {
                            pairOfFirstAndLastDate = new PairOfFirstAndLastDate(firstDate, date);
                        } else {
                            pairOfFirstAndLastDate = new PairOfFirstAndLastDate(date, firstDate);
                        }
                    } else {
                        if (firstDate.isAfter(date)) {
                            pairOfFirstAndLastDate = new PairOfFirstAndLastDate(date, lastDate);
                        } else if (lastDate.isBefore(date)) {
                            pairOfFirstAndLastDate = new PairOfFirstAndLastDate(firstDate, date);
                        }
                    }
                    sellerIDAndDates.put(sellerID, pairOfFirstAndLastDate);
                }
            }
        }

        Iterator<Map.Entry<String, PairOfFirstAndLastDate>> it3 = sellerIDAndDates.entrySet().iterator();

        while (it3.hasNext()) {
            Map.Entry<String, PairOfFirstAndLastDate> pair1 = it3.next();
            String sellerID = pair1.getKey();
            LocalDate first = pair1.getValue().getFirstDate();
            LocalDate last = pair1.getValue().getLastDate();
            if (last == null) { // meaning seller only has 1 order in total
                last = first;
            }
            int numberOfDays = (int) first.until(last, DAYS.toChronoUnit());
            sellerIDAndDays.put(sellerID, numberOfDays);
        }

        sortedSellerIDAndPaymentValue = sortByValue(sellerIDAndPaymentValue);
        Iterator<Map.Entry<String, Double>> it4 = sortedSellerIDAndPaymentValue.entrySet().iterator();

        System.out.println("\nDaily average revenue by top 10 sellers:");
        count = 0;
        while (it4.hasNext() && count < 10) {
            Map.Entry<String, Double> pair1 = it4.next();
            String sellerID = pair1.getKey();
            int days = sellerIDAndDays.get(sellerID);
            double payment = pair1.getValue();
            double dailyAverageRevenue = payment / days;
            count++;
            System.out.println(count + ". Seller ID: " + sellerID + " Daily average revenue: "
                    + String.format("%.2f", dailyAverageRevenue));
        }
    }

    private void monthlyRevenueInEachState() throws IOException {
        File file = new File(SELLERS_PATH);
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        int count = 0;
        for (String line : lines) {
            if (count > 0) {
                String[] array = line.split(",");
                String sellerID = array[0].replace("\"", "");
                String sellerState = array[array.length - 1];
                sellerIDAndSellerState.put(sellerID, sellerState);
            }
            count++;
        }

        Iterator<Map.Entry<String, Double>> it1 = sellerIDAndPaymentValue.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry<String, Double> pair1 = it1.next();
            String sellerID = pair1.getKey();
            String sellerState = sellerIDAndSellerState.get(sellerID);
            double paymentValue = pair1.getValue();
            if (!sellerStateAndPaymentValue.containsKey(sellerState)) {
                sellerStateAndPaymentValue.put(sellerState, paymentValue);
            } else {
                double currentRevenue = sellerStateAndPaymentValue.get(sellerState);
                currentRevenue += paymentValue;
                sellerStateAndPaymentValue.put(sellerState, currentRevenue);
            }
        }

        Iterator<Map.Entry<String, String>> it2 = sellerIDAndSellerState.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<String, String> pair1 = it2.next();
            String sellerID = pair1.getKey();
            if (sellerIDAndDays.get(sellerID) != null) { // seller ID did not sell anything
                int months = sellerIDAndDays.get(sellerID) / 30; // assuming one month is on average 30 days
                String sellerState = pair1.getValue();
                if (!sellerStateAndMonths.containsKey(sellerState)) {
                    sellerStateAndMonths.put(sellerState, months);
                } else {
                    int currentMonths = sellerStateAndMonths.get(sellerState);
                    currentMonths += months;
                    sellerStateAndMonths.put(sellerState, currentMonths);
                }
            }
        }

        Iterator<Map.Entry<String, Double>> it3 = sellerStateAndPaymentValue.entrySet().iterator();

        System.out.println("\nMonthly revenue in each state:");
        while (it3.hasNext()) {
            Map.Entry<String, Double> pair1 = it3.next();
            String sellerState = pair1.getKey();
            double payment = pair1.getValue();
            int months = sellerStateAndMonths.get(sellerState);
            double monthlyRevenue = payment / months;
            if (months == 0) { // did not reach 30 days
                monthlyRevenue = payment;
            }
            System.out.println("Seller state: " + sellerState + " Monthly revenue: "
                    + String.format("%.2f", monthlyRevenue));
        }
    }

    private void respond() {
        System.out.println("\nIf I am a seller, I will sell health and beauty products because it is the top product " +
                "category with high revenue.\nI am going to open a store in MA because it has the highest monthly revenue." +
                "\nHowever, all these data are not representative of everything and it really depends on the consumer" +
                "\nneeds and wants at different points in time and also the economic situations.\n");
    }
}
